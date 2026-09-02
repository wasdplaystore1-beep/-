package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.BannerAd
import com.example.data.model.PlatformCategory
import com.example.data.model.Product
import com.example.data.model.ProductWithStore
import com.example.data.model.Store
import com.example.data.model.StoreCategory
import com.example.data.model.StoreReview
import com.example.data.model.StoreWithProducts
import com.example.data.model.UserProfile
import com.example.data.repository.ProductFilterCriteria
import com.example.data.repository.ProductSortOption
import com.example.data.repository.SouqnaRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ScreenNav {
    object Home : ScreenNav()
    object Search : ScreenNav()
    data class StoreDetail(val storeId: Long) : ScreenNav()
    data class ProductDetail(val productId: Long) : ScreenNav()
    data class CategoryProducts(val categoryKey: String, val categoryName: String) : ScreenNav()
    object CreateStore : ScreenNav()
    object MerchantDashboard : ScreenNav()
    data class AddEditProduct(val productId: Long? = null) : ScreenNav()
    object AdminPanel : ScreenNav()
    object InteractiveMap : ScreenNav()
    object Monetization : ScreenNav()
    object Profile : ScreenNav()
}

enum class SearchTab {
    ALL, PRODUCTS, STORES
}

class SouqnaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SouqnaRepository = SouqnaRepository(
        AppDatabase.getInstance(application)
    )

    // Navigation Stack
    private val _navStack = MutableStateFlow<List<ScreenNav>>(listOf(ScreenNav.Home))
    val currentScreen: StateFlow<ScreenNav> = _navStack.flatMapLatest { stack ->
        flowOf(stack.lastOrNull() ?: ScreenNav.Home)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ScreenNav.Home)

    // Web Experience State
    private val _isWebMode = MutableStateFlow(true)
    val isWebMode: StateFlow<Boolean> = _isWebMode.asStateFlow()

    private val _webUrlInput = MutableStateFlow("https://souqna.app/")
    val webUrlInput: StateFlow<String> = _webUrlInput.asStateFlow()

    fun toggleWebMode() {
        _isWebMode.value = !_isWebMode.value
    }

    fun setWebMode(enabled: Boolean) {
        _isWebMode.value = enabled
    }

    fun updateWebUrlInput(url: String) {
        _webUrlInput.value = url
    }

    fun getWebUrlForScreen(screen: ScreenNav): String {
        val base = "https://souqna.app"
        return when (screen) {
            is ScreenNav.Home -> "$base/"
            is ScreenNav.Search -> "$base/search"
            is ScreenNav.InteractiveMap -> "$base/map"
            is ScreenNav.CreateStore -> "$base/merchant/create-store"
            is ScreenNav.MerchantDashboard -> "$base/merchant/dashboard"
            is ScreenNav.AdminPanel -> "$base/admin/portal"
            is ScreenNav.Monetization -> "$base/pricing/plans"
            is ScreenNav.Profile -> "$base/user/profile"
            is ScreenNav.AddEditProduct -> "$base/merchant/products/editor"
            is ScreenNav.CategoryProducts -> "$base/category/${screen.categoryKey}"
            is ScreenNav.StoreDetail -> {
                val store = allActiveStores.value.find { it.id == screen.storeId }
                if (store != null) "$base/store/${store.slug}" else "$base/store/${screen.storeId}"
            }
            is ScreenNav.ProductDetail -> "$base/product/${screen.productId}"
        }
    }

    fun navigateToWebUrl(rawUrl: String) {
        var cleanUrl = rawUrl.trim()
        if (cleanUrl.startsWith("https://souqna.app")) {
            cleanUrl = cleanUrl.removePrefix("https://souqna.app")
        } else if (cleanUrl.startsWith("http://souqna.app")) {
            cleanUrl = cleanUrl.removePrefix("http://souqna.app")
        } else if (cleanUrl.startsWith("souqna.app")) {
            cleanUrl = cleanUrl.removePrefix("souqna.app")
        }

        if (cleanUrl.isEmpty() || cleanUrl == "/") {
            navigateToTab(ScreenNav.Home)
            return
        }

        when {
            cleanUrl.startsWith("/search") -> navigateToTab(ScreenNav.Search)
            cleanUrl.startsWith("/map") || cleanUrl.startsWith("/stores/map") -> navigateToTab(ScreenNav.InteractiveMap)
            cleanUrl.startsWith("/merchant/create-store") || cleanUrl.startsWith("/create-store") -> navigateTo(ScreenNav.CreateStore)
            cleanUrl.startsWith("/merchant/dashboard") || cleanUrl.startsWith("/merchant") -> navigateTo(ScreenNav.MerchantDashboard)
            cleanUrl.startsWith("/admin") -> navigateTo(ScreenNav.AdminPanel)
            cleanUrl.startsWith("/pricing") || cleanUrl.startsWith("/monetization") -> navigateTo(ScreenNav.Monetization)
            cleanUrl.startsWith("/profile") || cleanUrl.startsWith("/user") -> navigateTo(ScreenNav.Profile)
            cleanUrl.startsWith("/category/") -> {
                val catKey = cleanUrl.removePrefix("/category/").trim().split("/").firstOrNull() ?: ""
                val cat = platformCategories.value.find { it.key.equals(catKey, ignoreCase = true) }
                if (cat != null) {
                    selectCategory(cat)
                } else {
                    navigateTo(ScreenNav.CategoryProducts(catKey, "قسم $catKey"))
                }
            }
            cleanUrl.startsWith("/store/") -> {
                val slugOrId = cleanUrl.removePrefix("/store/").trim().split("/").firstOrNull() ?: ""
                val store = allActiveStores.value.find { it.slug.equals(slugOrId, ignoreCase = true) }
                    ?: allStoresAdmin.value.find { it.slug.equals(slugOrId, ignoreCase = true) }
                    ?: slugOrId.toLongOrNull()?.let { id -> allStoresAdmin.value.find { it.id == id } }
                if (store != null) {
                    selectStore(store.id)
                } else {
                    slugOrId.toLongOrNull()?.let { selectStore(it) } ?: navigateToTab(ScreenNav.Home)
                }
            }
            cleanUrl.startsWith("/product/") -> {
                val prodIdStr = cleanUrl.removePrefix("/product/").trim().split("/").firstOrNull() ?: ""
                val prodId = prodIdStr.toLongOrNull() ?: 1L
                selectProduct(prodId)
            }
            else -> {
                // If it's a search term
                updateSearchQuery(cleanUrl.removePrefix("/"))
                navigateToTab(ScreenNav.Search)
            }
        }
    }

    fun navigateTo(screen: ScreenNav) {
        val current = _navStack.value.toMutableList()
        current.add(screen)
        _navStack.value = current
    }

    fun navigateBack(): Boolean {
        val current = _navStack.value.toMutableList()
        return if (current.size > 1) {
            current.removeAt(current.size - 1)
            _navStack.value = current
            true
        } else {
            false
        }
    }

    fun navigateToTab(screen: ScreenNav) {
        _navStack.value = listOf(screen)
    }

    // Platform Data
    val platformCategories: StateFlow<List<PlatformCategory>> = repository.platformCategories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val featuredStores: StateFlow<List<Store>> = repository.featuredStores
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allActiveStores: StateFlow<List<Store>> = repository.allActiveStores
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val newStores: StateFlow<List<Store>> = repository.newStores
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val featuredProducts: StateFlow<List<Product>> = repository.featuredProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val latestProducts: StateFlow<List<Product>> = repository.latestProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allProductsWithStore: StateFlow<List<ProductWithStore>> = repository.getProductsWithStore()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeBanners: StateFlow<List<BannerAd>> = repository.activeBanners
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBannersAdmin: StateFlow<List<BannerAd>> = repository.allBannersAdmin
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentUser: StateFlow<UserProfile?> = repository.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Search State
    private val _searchFilter = MutableStateFlow(ProductFilterCriteria())
    val searchFilter = _searchFilter.asStateFlow()

    private val _searchTab = MutableStateFlow(SearchTab.ALL)
    val searchTab = _searchTab.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val productSearchResults: StateFlow<List<ProductWithStore>> = _searchFilter
        .flatMapLatest { criteria ->
            repository.searchProductsUnified(criteria)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val storeSearchResults: StateFlow<List<Store>> = _searchFilter
        .flatMapLatest { criteria ->
            if (criteria.query.isBlank()) repository.allActiveStores
            else repository.searchStores(criteria.query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearchQuery(query: String) {
        _searchFilter.value = _searchFilter.value.copy(query = query)
    }

    fun updateSearchFilter(criteria: ProductFilterCriteria) {
        _searchFilter.value = criteria
    }

    fun setSearchTab(tab: SearchTab) {
        _searchTab.value = tab
    }

    fun clearSearchFilter() {
        _searchFilter.value = ProductFilterCriteria()
    }

    // Selected Store Details
    private val _selectedStoreId = MutableStateFlow<Long?>(null)
    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedStoreDetails: StateFlow<StoreWithProducts?> = _selectedStoreId
        .flatMapLatest { id ->
            if (id != null) repository.getStoreDetails(id) else flowOf(null)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun selectStore(storeId: Long) {
        _selectedStoreId.value = storeId
        navigateTo(ScreenNav.StoreDetail(storeId))
    }

    // Selected Product Details
    private val _selectedProductId = MutableStateFlow<Long?>(null)
    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedProductDetails: StateFlow<ProductWithStore?> = _selectedProductId
        .flatMapLatest { id ->
            if (id != null) repository.getProductDetailsWithStore(id) else flowOf(null)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun selectProduct(productId: Long) {
        _selectedProductId.value = productId
        viewModelScope.launch {
            repository.incrementProductViews(productId)
        }
        navigateTo(ScreenNav.ProductDetail(productId))
    }

    // Selected Category Products
    private val _selectedCategoryKey = MutableStateFlow<String?>(null)
    @OptIn(ExperimentalCoroutinesApi::class)
    val categoryProducts: StateFlow<List<ProductWithStore>> = _selectedCategoryKey
        .flatMapLatest { key ->
            if (key != null) repository.getProductsByPlatformCategory(key) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectCategory(category: PlatformCategory) {
        _selectedCategoryKey.value = category.key
        navigateTo(ScreenNav.CategoryProducts(category.key, category.nameAr))
    }

    // Merchant Store Management
    @OptIn(ExperimentalCoroutinesApi::class)
    val myMerchantStore: StateFlow<StoreWithProducts?> = currentUser
        .flatMapLatest { user ->
            if (user?.storeId != null) repository.getStoreDetails(user.storeId)
            else flowOf(null)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun createMerchantStore(
        name: String,
        slug: String,
        description: String,
        city: String,
        area: String,
        address: String,
        latitude: Double,
        longitude: Double,
        phone: String,
        whatsapp: String,
        instagram: String,
        logoUrl: String = "",
        bannerUrl: String = "",
        onSuccess: (Long) -> Unit
    ) {
        viewModelScope.launch {
            val user = currentUser.value
            val ownerId = user?.id ?: 1L
            val ownerName = user?.name ?: "التاجر"

            val cleanSlug = slug.trim().lowercase().replace(" ", "-").ifEmpty { "store-${System.currentTimeMillis() % 10000}" }

            val store = Store(
                name = name,
                slug = cleanSlug,
                description = description,
                city = city,
                area = area,
                address = address,
                latitude = latitude,
                longitude = longitude,
                phone = phone,
                whatsapp = whatsapp,
                instagram = instagram,
                logoUrl = logoUrl,
                bannerUrl = bannerUrl,
                isFeatured = false,
                isVerified = true,
                isActive = true,
                rating = 5.0,
                reviewCount = 1,
                ownerId = ownerId,
                ownerName = ownerName
            )
            val storeId = repository.createStore(store)

            // Add default initial categories for this store
            repository.insertStoreCategory(StoreCategory(storeId = storeId, name = "المنتجات المميزة", displayOrder = 1))
            repository.insertStoreCategory(StoreCategory(storeId = storeId, name = "وصل حديثاً", displayOrder = 2))

            // Update user profile
            if (user != null) {
                repository.updateUser(user.copy(isMerchant = true, storeId = storeId))
            }

            onSuccess(storeId)
        }
    }

    fun updateStoreProfile(store: Store) {
        viewModelScope.launch {
            repository.updateStore(store)
        }
    }

    fun addStoreCategory(storeId: Long, name: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.insertStoreCategory(
                StoreCategory(storeId = storeId, name = name.trim(), displayOrder = 0)
            )
        }
    }

    fun deleteStoreCategory(categoryId: Long) {
        viewModelScope.launch {
            repository.deleteStoreCategoryById(categoryId)
        }
    }

    fun saveProduct(
        productId: Long? = null,
        storeId: Long,
        storeCategoryId: Long?,
        platformCategoryKey: String,
        title: String,
        slug: String,
        description: String,
        price: Double,
        originalPrice: Double?,
        imageUrl: String,
        isFeatured: Boolean,
        inStock: Boolean,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val cleanSlug = slug.trim().lowercase().replace(" ", "-").ifEmpty { "product-${System.currentTimeMillis() % 10000}" }
            val product = Product(
                id = productId ?: 0L,
                storeId = storeId,
                storeCategoryId = storeCategoryId,
                platformCategoryKey = platformCategoryKey,
                title = title.trim(),
                slug = cleanSlug,
                description = description.trim(),
                price = price,
                originalPrice = originalPrice,
                imageUrl = imageUrl,
                isFeatured = isFeatured,
                inStock = inStock
            )
            if (productId == null || productId == 0L) {
                repository.insertProduct(product)
            } else {
                repository.updateProduct(product)
            }
            onSuccess()
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }

    fun toggleProductFeatured(productId: Long, isFeatured: Boolean) {
        viewModelScope.launch {
            repository.toggleProductFeatured(productId, isFeatured)
        }
    }

    // Customer Reviews
    fun addStoreReview(storeId: Long, userName: String, rating: Int, comment: String) {
        viewModelScope.launch {
            repository.addReview(
                StoreReview(
                    storeId = storeId,
                    userName = userName.ifBlank { "عميل موثق" },
                    rating = rating.coerceIn(1, 5),
                    comment = comment.trim()
                )
            )
        }
    }

    // Admin Panel Management
    val allStoresAdmin: StateFlow<List<Store>> = repository.allStoresAdmin
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allProductsAdmin: StateFlow<List<Product>> = repository.allProductsAdmin
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalStoresCount: StateFlow<Int> = repository.totalStoresCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 6)

    val totalProductsCount: StateFlow<Int> = repository.totalProductsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 12)

    fun adminToggleStoreFeatured(storeId: Long, isFeatured: Boolean) {
        viewModelScope.launch {
            repository.toggleStoreFeatured(storeId, isFeatured)
        }
    }

    fun adminToggleStoreActive(storeId: Long, isActive: Boolean) {
        viewModelScope.launch {
            repository.toggleStoreActive(storeId, isActive)
        }
    }

    fun adminDeleteStore(store: Store) {
        viewModelScope.launch {
            repository.deleteStore(store)
        }
    }

    fun adminDeleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }

    // Banner Ads Management
    fun createBannerAd(
        title: String,
        subtitle: String,
        badgeText: String,
        imageUrl: String,
        actionText: String,
        targetType: String,
        targetPayload: String,
        gradientStartHex: String = "#059669",
        gradientEndHex: String = "#047857",
        isAnimated: Boolean = true
    ) {
        viewModelScope.launch {
            repository.insertBanner(
                BannerAd(
                    title = title.trim(),
                    subtitle = subtitle.trim(),
                    badgeText = badgeText.trim().ifBlank { "إعلان مميز ✨" },
                    imageUrl = imageUrl.trim(),
                    actionText = actionText.trim().ifBlank { "تسوق الآن" },
                    targetType = targetType,
                    targetPayload = targetPayload.trim(),
                    gradientStartHex = gradientStartHex,
                    gradientEndHex = gradientEndHex,
                    isActive = true,
                    isAnimated = isAnimated,
                    displayOrder = 0
                )
            )
        }
    }

    fun updateBannerAd(banner: BannerAd) {
        viewModelScope.launch {
            repository.updateBanner(banner)
        }
    }

    fun deleteBannerAd(banner: BannerAd) {
        viewModelScope.launch {
            repository.deleteBanner(banner)
        }
    }

    fun toggleBannerStatus(bannerId: Long, isActive: Boolean) {
        viewModelScope.launch {
            repository.toggleBannerStatus(bannerId, isActive)
        }
    }

    fun toggleBannerStatus(banner: BannerAd) {
        toggleBannerStatus(banner.id, !banner.isActive)
    }

    fun handleBannerClick(banner: BannerAd) {
        viewModelScope.launch {
            repository.incrementBannerViews(banner.id)
        }
        when (banner.targetType) {
            "STORE" -> {
                val storeId = banner.targetPayload.toLongOrNull()
                if (storeId != null) {
                    selectStore(storeId)
                } else {
                    navigateTo(ScreenNav.Search)
                }
            }
            "CATEGORY" -> {
                val catKey = banner.targetPayload.ifBlank { "watches_accessories" }
                val cat = platformCategories.value.find { it.key == catKey }
                if (cat != null) {
                    selectCategory(cat)
                } else {
                    navigateTo(ScreenNav.Search)
                }
            }
            "SEARCH" -> {
                updateSearchQuery(banner.targetPayload)
                setSearchTab(SearchTab.ALL)
                navigateTo(ScreenNav.Search)
            }
            "SPECIAL_OFFER" -> {
                if (banner.targetPayload == "create_store") {
                    navigateTo(ScreenNav.CreateStore)
                } else {
                    navigateTo(ScreenNav.Monetization)
                }
            }
            "EXTERNAL" -> {
                if (banner.targetPayload.startsWith("http")) {
                    navigateToWebUrl(banner.targetPayload)
                } else {
                    navigateTo(ScreenNav.Search)
                }
            }
            else -> {
                navigateTo(ScreenNav.Search)
            }
        }
    }
}
