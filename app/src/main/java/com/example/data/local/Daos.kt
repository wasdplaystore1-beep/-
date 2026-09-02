package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.PlatformCategory
import com.example.data.model.Product
import com.example.data.model.Store
import com.example.data.model.StoreCategory
import com.example.data.model.StoreReview
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreDao {
    @Query("SELECT * FROM stores WHERE isActive = 1 ORDER BY isFeatured DESC, rating DESC, createdAt DESC")
    fun getAllActiveStores(): Flow<List<Store>>

    @Query("SELECT * FROM stores ORDER BY createdAt DESC")
    fun getAllStoresAdmin(): Flow<List<Store>>

    @Query("SELECT * FROM stores WHERE isFeatured = 1 AND isActive = 1 ORDER BY rating DESC LIMIT 10")
    fun getFeaturedStores(): Flow<List<Store>>

    @Query("SELECT * FROM stores WHERE isActive = 1 ORDER BY createdAt DESC LIMIT 8")
    fun getNewStores(): Flow<List<Store>>

    @Query("SELECT * FROM stores WHERE id = :storeId LIMIT 1")
    fun getStoreById(storeId: Long): Flow<Store?>

    @Query("SELECT * FROM stores WHERE id = :storeId LIMIT 1")
    suspend fun getStoreByIdDirect(storeId: Long): Store?

    @Query("SELECT * FROM stores WHERE slug = :slug LIMIT 1")
    suspend fun getStoreBySlug(slug: String): Store?

    @Query("SELECT * FROM stores WHERE ownerId = :userId LIMIT 1")
    fun getStoreByOwnerId(userId: Long): Flow<Store?>

    @Query("""
        SELECT * FROM stores 
        WHERE isActive = 1 
        AND (name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR city LIKE '%' || :query || '%')
        ORDER BY isFeatured DESC, rating DESC
    """)
    fun searchStores(query: String): Flow<List<Store>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStore(store: Store): Long

    @Update
    suspend fun updateStore(store: Store)

    @Delete
    suspend fun deleteStore(store: Store)

    @Query("UPDATE stores SET isFeatured = :isFeatured WHERE id = :storeId")
    suspend fun updateFeaturedStatus(storeId: Long, isFeatured: Boolean)

    @Query("UPDATE stores SET isActive = :isActive WHERE id = :storeId")
    suspend fun updateActiveStatus(storeId: Long, isActive: Boolean)

    @Query("SELECT COUNT(*) FROM stores")
    fun getStoresCount(): Flow<Int>
}

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE inStock = 1 ORDER BY isFeatured DESC, createdAt DESC")
    fun getAllAvailableProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products ORDER BY createdAt DESC")
    fun getAllProductsAdmin(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE isFeatured = 1 AND inStock = 1 ORDER BY createdAt DESC LIMIT 12")
    fun getFeaturedProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE inStock = 1 ORDER BY createdAt DESC LIMIT 12")
    fun getLatestProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE storeId = :storeId ORDER BY createdAt DESC")
    fun getProductsByStore(storeId: Long): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE storeId = :storeId AND storeCategoryId = :categoryId ORDER BY createdAt DESC")
    fun getProductsByStoreAndCategory(storeId: Long, categoryId: Long): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE platformCategoryKey = :categoryKey AND inStock = 1 ORDER BY isFeatured DESC, createdAt DESC")
    fun getProductsByPlatformCategory(categoryKey: String): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    fun getProductById(productId: Long): Flow<Product?>

    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    suspend fun getProductByIdDirect(productId: Long): Product?

    @Query("""
        SELECT * FROM products 
        WHERE inStock = 1 
        AND (title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%')
        ORDER BY isFeatured DESC, createdAt DESC
    """)
    fun searchProducts(query: String): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("UPDATE products SET isFeatured = :isFeatured WHERE id = :productId")
    suspend fun updateProductFeaturedStatus(productId: Long, isFeatured: Boolean)

    @Query("UPDATE products SET viewsCount = viewsCount + 1 WHERE id = :productId")
    suspend fun incrementViews(productId: Long)

    @Query("SELECT COUNT(*) FROM products")
    fun getProductsCount(): Flow<Int>
}

@Dao
interface StoreCategoryDao {
    @Query("SELECT * FROM store_categories WHERE storeId = :storeId ORDER BY displayOrder ASC, id ASC")
    fun getCategoriesByStore(storeId: Long): Flow<List<StoreCategory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: StoreCategory): Long

    @Update
    suspend fun updateCategory(category: StoreCategory)

    @Delete
    suspend fun deleteCategory(category: StoreCategory)

    @Query("DELETE FROM store_categories WHERE id = :categoryId")
    suspend fun deleteCategoryById(categoryId: Long)
}

@Dao
interface PlatformCategoryDao {
    @Query("SELECT * FROM platform_categories ORDER BY displayOrder ASC")
    fun getAllPlatformCategories(): Flow<List<PlatformCategory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<PlatformCategory>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: PlatformCategory)
}

@Dao
interface StoreReviewDao {
    @Query("SELECT * FROM store_reviews WHERE storeId = :storeId ORDER BY createdAt DESC")
    fun getReviewsForStore(storeId: Long): Flow<List<StoreReview>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: StoreReview): Long

    @Query("SELECT AVG(rating) FROM store_reviews WHERE storeId = :storeId")
    suspend fun getAverageRating(storeId: Long): Double?

    @Query("SELECT COUNT(*) FROM store_reviews WHERE storeId = :storeId")
    suspend fun getReviewCount(storeId: Long): Int
}

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles LIMIT 1")
    fun getCurrentUser(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profiles LIMIT 1")
    suspend fun getCurrentUserDirect(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserProfile): Long

    @Update
    suspend fun updateUser(user: UserProfile)
}

@Dao
interface BannerAdDao {
    @Query("SELECT * FROM banner_ads WHERE isActive = 1 ORDER BY displayOrder ASC, id DESC")
    fun getActiveBanners(): Flow<List<com.example.data.model.BannerAd>>

    @Query("SELECT * FROM banner_ads ORDER BY displayOrder ASC, id DESC")
    fun getAllBannersAdmin(): Flow<List<com.example.data.model.BannerAd>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBanner(banner: com.example.data.model.BannerAd): Long

    @Update
    suspend fun updateBanner(banner: com.example.data.model.BannerAd)

    @Delete
    suspend fun deleteBanner(banner: com.example.data.model.BannerAd)

    @Query("UPDATE banner_ads SET isActive = :isActive WHERE id = :bannerId")
    suspend fun updateBannerStatus(bannerId: Long, isActive: Boolean)

    @Query("UPDATE banner_ads SET viewsCount = viewsCount + 1 WHERE id = :bannerId")
    suspend fun incrementBannerViews(bannerId: Long)
}
