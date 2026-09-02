package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "stores",
    indices = [Index(value = ["slug"], unique = true)]
)
data class Store(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val slug: String,
    val description: String,
    val logoUrl: String = "",
    val bannerUrl: String = "",
    val city: String,
    val area: String,
    val address: String = "",
    val latitude: Double = 24.7136, // Default Riyadh center coords
    val longitude: Double = 46.6753,
    val phone: String = "",
    val whatsapp: String = "",
    val instagram: String = "",
    val telegram: String = "",
    val isFeatured: Boolean = false,
    val isVerified: Boolean = true,
    val isActive: Boolean = true,
    val rating: Double = 4.8,
    val reviewCount: Int = 12,
    val ownerId: Long = 1,
    val ownerName: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "store_categories",
    foreignKeys = [
        ForeignKey(
            entity = Store::class,
            parentColumns = ["id"],
            childColumns = ["storeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("storeId")]
)
data class StoreCategory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val storeId: Long,
    val name: String,
    val displayOrder: Int = 0
)

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = Store::class,
            parentColumns = ["id"],
            childColumns = ["storeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("storeId"),
        Index("platformCategoryKey"),
        Index(value = ["slug"], unique = false)
    ]
)
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val storeId: Long,
    val storeCategoryId: Long? = null,
    val platformCategoryKey: String,
    val title: String,
    val slug: String,
    val description: String,
    val price: Double,
    val originalPrice: Double? = null,
    val imageUrl: String = "",
    val isFeatured: Boolean = false,
    val inStock: Boolean = true,
    val viewsCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "platform_categories")
data class PlatformCategory(
    @PrimaryKey
    val key: String,
    val nameAr: String,
    val nameEn: String,
    val iconEmoji: String,
    val colorHex: String,
    val displayOrder: Int = 0
)

@Entity(
    tableName = "store_reviews",
    foreignKeys = [
        ForeignKey(
            entity = Store::class,
            parentColumns = ["id"],
            childColumns = ["storeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("storeId")]
)
data class StoreReview(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val storeId: Long,
    val userName: String,
    val userAvatar: String = "",
    val rating: Int, // 1 to 5
    val comment: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val email: String,
    val phone: String = "",
    val avatarUrl: String = "",
    val isMerchant: Boolean = false,
    val isAdmin: Boolean = false,
    val storeId: Long? = null
)

@Entity(tableName = "banner_ads")
data class BannerAd(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val subtitle: String = "",
    val badgeText: String = "إعلان مميز ✨",
    val imageUrl: String = "",
    val actionText: String = "تسوق الآن",
    val targetType: String = "STORE", // "STORE", "CATEGORY", "SEARCH", "SPECIAL_OFFER", "EXTERNAL"
    val targetPayload: String = "1", // storeId, categoryKey, search query, or url
    val gradientStartHex: String = "#059669",
    val gradientEndHex: String = "#047857",
    val isActive: Boolean = true,
    val isAnimated: Boolean = true,
    val displayOrder: Int = 0,
    val viewsCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

// Combined UI data structures for Unified Search & Store/Product Browsing
data class ProductWithStore(
    val product: Product,
    val store: Store
)

data class StoreWithProducts(
    val store: Store,
    val products: List<Product>,
    val categories: List<StoreCategory>,
    val reviews: List<StoreReview>
)
