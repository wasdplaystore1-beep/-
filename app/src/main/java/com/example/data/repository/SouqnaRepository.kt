package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.model.PlatformCategory
import com.example.data.model.Product
import com.example.data.model.ProductWithStore
import com.example.data.model.Store
import com.example.data.model.StoreCategory
import com.example.data.model.StoreReview
import com.example.data.model.StoreWithProducts
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

enum class ProductSortOption(val titleAr: String) {
    LATEST("الأحدث أولاً"),
    PRICE_LOW("الأقل سعراً"),
    PRICE_HIGH("الأعلى سعراً"),
    MOST_VIEWED("الأكثر مشاهدة")
}

data class ProductFilterCriteria(
    val query: String = "",
    val categoryKey: String? = null,
    val storeId: Long? = null,
    val city: String? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val sortOption: ProductSortOption = ProductSortOption.LATEST
)

class SouqnaRepository(private val database: AppDatabase) {

    private val storeDao = database.storeDao()
    private val productDao = database.productDao()
    private val storeCategoryDao = database.storeCategoryDao()
    private val platformCategoryDao = database.platformCategoryDao()
    private val storeReviewDao = database.storeReviewDao()
    private val userProfileDao = database.userProfileDao()

    // 1. Stores
    val allActiveStores: Flow<List<Store>> = storeDao.getAllActiveStores()
    val allStoresAdmin: Flow<List<Store>> = storeDao.getAllStoresAdmin()
    val featuredStores: Flow<List<Store>> = storeDao.getFeaturedStores()
    val newStores: Flow<List<Store>> = storeDao.getNewStores()

    fun getStoreById(storeId: Long): Flow<Store?> = storeDao.getStoreById(storeId)

    suspend fun getStoreByIdDirect(storeId: Long): Store? = storeDao.getStoreByIdDirect(storeId)

    suspend fun getStoreBySlug(slug: String): Store? = storeDao.getStoreBySlug(slug)

    fun getStoreByOwnerId(userId: Long): Flow<Store?> = storeDao.getStoreByOwnerId(userId)

    fun searchStores(query: String): Flow<List<Store>> = storeDao.searchStores(query)

    suspend fun createStore(store: Store): Long = storeDao.insertStore(store)

    suspend fun updateStore(store: Store) = storeDao.updateStore(store)

    suspend fun deleteStore(store: Store) = storeDao.deleteStore(store)

    suspend fun toggleStoreFeatured(storeId: Long, isFeatured: Boolean) =
        storeDao.updateFeaturedStatus(storeId, isFeatured)

    suspend fun toggleStoreActive(storeId: Long, isActive: Boolean) =
        storeDao.updateActiveStatus(storeId, isActive)

    // Store Full Details (Store + Products + Categories + Reviews)
    fun getStoreDetails(storeId: Long): Flow<StoreWithProducts?> {
        return combine(
            storeDao.getStoreById(storeId),
            productDao.getProductsByStore(storeId),
            storeCategoryDao.getCategoriesByStore(storeId),
            storeReviewDao.getReviewsForStore(storeId)
        ) { store, products, categories, reviews ->
            if (store == null) null
            else StoreWithProducts(
                store = store,
                products = products,
                categories = categories,
                reviews = reviews
            )
        }
    }

    // 2. Products & Unified Search
    val allProducts: Flow<List<Product>> = productDao.getAllAvailableProducts()
    val allProductsAdmin: Flow<List<Product>> = productDao.getAllProductsAdmin()
    val featuredProducts: Flow<List<Product>> = productDao.getFeaturedProducts()
    val latestProducts: Flow<List<Product>> = productDao.getLatestProducts()

    // Combines products with their merchant store data for cross-store browsing
    fun getProductsWithStore(): Flow<List<ProductWithStore>> {
        return combine(productDao.getAllAvailableProducts(), storeDao.getAllActiveStores()) { products, stores ->
            val storeMap = stores.associateBy { it.id }
            products.mapNotNull { product ->
                storeMap[product.storeId]?.let { store ->
                    ProductWithStore(product = product, store = store)
                }
            }
        }
    }

    fun getProductDetailsWithStore(productId: Long): Flow<ProductWithStore?> {
        return combine(productDao.getProductById(productId), storeDao.getAllActiveStores()) { product, stores ->
            if (product == null) null
            else {
                val store = stores.find { it.id == product.storeId } 
                    ?: storeDao.getStoreByIdDirect(product.storeId)
                if (store != null) ProductWithStore(product, store) else null
            }
        }
    }

    fun getProductsByStore(storeId: Long): Flow<List<Product>> = productDao.getProductsByStore(storeId)

    fun getProductsByStoreAndCategory(storeId: Long, categoryId: Long): Flow<List<Product>> =
        productDao.getProductsByStoreAndCategory(storeId, categoryId)

    fun getProductsByPlatformCategory(categoryKey: String): Flow<List<ProductWithStore>> {
        return combine(
            productDao.getProductsByPlatformCategory(categoryKey),
            storeDao.getAllActiveStores()
        ) { products, stores ->
            val storeMap = stores.associateBy { it.id }
            products.mapNotNull { product ->
                storeMap[product.storeId]?.let { store ->
                    ProductWithStore(product = product, store = store)
                }
            }
        }
    }

    // Unified Search across all stores with filters and sorting
    fun searchProductsUnified(criteria: ProductFilterCriteria): Flow<List<ProductWithStore>> {
        return combine(
            if (criteria.query.isBlank()) productDao.getAllAvailableProducts() else productDao.searchProducts(criteria.query),
            storeDao.getAllActiveStores()
        ) { products, stores ->
            val storeMap = stores.associateBy { it.id }
            var filtered = products.mapNotNull { product ->
                storeMap[product.storeId]?.let { store ->
                    ProductWithStore(product = product, store = store)
                }
            }

            // Apply category filter
            if (!criteria.categoryKey.isNullOrBlank()) {
                filtered = filtered.filter { it.product.platformCategoryKey == criteria.categoryKey }
            }

            // Apply store filter
            if (criteria.storeId != null && criteria.storeId > 0) {
                filtered = filtered.filter { it.product.storeId == criteria.storeId }
            }

            // Apply city filter
            if (!criteria.city.isNullOrBlank()) {
                filtered = filtered.filter { it.store.city.contains(criteria.city, ignoreCase = true) }
            }

            // Apply price min
            if (criteria.minPrice != null) {
                filtered = filtered.filter { it.product.price >= criteria.minPrice }
            }

            // Apply price max
            if (criteria.maxPrice != null) {
                filtered = filtered.filter { it.product.price <= criteria.maxPrice }
            }

            // Apply sorting
            when (criteria.sortOption) {
                ProductSortOption.LATEST -> filtered.sortedByDescending { it.product.createdAt }
                ProductSortOption.PRICE_LOW -> filtered.sortedBy { it.product.price }
                ProductSortOption.PRICE_HIGH -> filtered.sortedByDescending { it.product.price }
                ProductSortOption.MOST_VIEWED -> filtered.sortedByDescending { it.product.viewsCount }
            }
        }
    }

    suspend fun insertProduct(product: Product): Long = productDao.insertProduct(product)

    suspend fun updateProduct(product: Product) = productDao.updateProduct(product)

    suspend fun deleteProduct(product: Product) = productDao.deleteProduct(product)

    suspend fun toggleProductFeatured(productId: Long, isFeatured: Boolean) =
        productDao.updateProductFeaturedStatus(productId, isFeatured)

    suspend fun incrementProductViews(productId: Long) = productDao.incrementViews(productId)

    // 3. Store Categories / Sections
    fun getStoreCategories(storeId: Long): Flow<List<StoreCategory>> =
        storeCategoryDao.getCategoriesByStore(storeId)

    suspend fun insertStoreCategory(category: StoreCategory): Long =
        storeCategoryDao.insertCategory(category)

    suspend fun updateStoreCategory(category: StoreCategory) =
        storeCategoryDao.updateCategory(category)

    suspend fun deleteStoreCategory(category: StoreCategory) =
        storeCategoryDao.deleteCategory(category)

    suspend fun deleteStoreCategoryById(categoryId: Long) =
        storeCategoryDao.deleteCategoryById(categoryId)

    // 4. Platform Categories
    val platformCategories: Flow<List<PlatformCategory>> =
        platformCategoryDao.getAllPlatformCategories()

    // 5. Reviews
    fun getReviewsForStore(storeId: Long): Flow<List<StoreReview>> =
        storeReviewDao.getReviewsForStore(storeId)

    suspend fun addReview(review: StoreReview): Long {
        val reviewId = storeReviewDao.insertReview(review)
        // recalculate store rating
        val avg = storeReviewDao.getAverageRating(review.storeId) ?: 5.0
        val count = storeReviewDao.getReviewCount(review.storeId)
        val store = storeDao.getStoreByIdDirect(review.storeId)
        if (store != null) {
            val updatedStore = store.copy(
                rating = (Math.round(avg * 10.0) / 10.0),
                reviewCount = count
            )
            storeDao.updateStore(updatedStore)
        }
        return reviewId
    }

    // 6. User Profile
    val currentUser: Flow<UserProfile?> = userProfileDao.getCurrentUser()

    suspend fun updateUser(user: UserProfile) = userProfileDao.updateUser(user)

    // 7. Platform Admin Counts
    val totalStoresCount: Flow<Int> = storeDao.getStoresCount()
    val totalProductsCount: Flow<Int> = productDao.getProductsCount()
}
