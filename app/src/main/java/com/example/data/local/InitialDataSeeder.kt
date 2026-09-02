package com.example.data.local

import com.example.data.model.PlatformCategory
import com.example.data.model.Product
import com.example.data.model.Store
import com.example.data.model.StoreCategory
import com.example.data.model.StoreReview
import com.example.data.model.UserProfile

object InitialDataSeeder {

    suspend fun seedDatabase(database: AppDatabase) {
        val platformCatDao = database.platformCategoryDao()
        val storeDao = database.storeDao()
        val storeCatDao = database.storeCategoryDao()
        val productDao = database.productDao()
        val reviewDao = database.storeReviewDao()
        val userDao = database.userProfileDao()

        // 1. Seed Platform Categories
        val categories = listOf(
            PlatformCategory("phones_electronics", "الجوالات والإلكترونيات", "Phones & Electronics", "📱", "#0D9488", 1),
            PlatformCategory("watches_accessories", "الساعات والإكسسوارات", "Watches & Accessories", "⌚", "#F59E0B", 2),
            PlatformCategory("fashion_clothing", "الملابس والأزياء", "Fashion & Clothing", "👕", "#EC4899", 3),
            PlatformCategory("shoes_bags", "الأحذية والحقائب", "Shoes & Bags", "👟", "#8B5CF6", 4),
            PlatformCategory("home_living", "المنزل والديكور", "Home & Living", "🏠", "#10B981", 5),
            PlatformCategory("gaming_entertainment", "الألعاب والترفيه", "Gaming & Toys", "🎮", "#6366F1", 6),
            PlatformCategory("computers_tech", "الكمبيوتر والملحقات", "Computers & Tech", "💻", "#0284C7", 7),
            PlatformCategory("beauty_care", "العناية والجمال", "Beauty & Care", "💄", "#F43F5E", 8),
            PlatformCategory("automotive", "السيارات والدراجات", "Automotive", "🛵", "#64748B", 9),
            PlatformCategory("pets_supplies", "الحيوانات ومستلزماتها", "Pets & Supplies", "🐱", "#D97706", 10),
            PlatformCategory("food_gourmet", "الأغذية والمشروبات", "Food & Gourmet", "🍽️", "#84CC16", 11),
            PlatformCategory("books_stationery", "الكتب والقرطاسية", "Books & Stationery", "📚", "#14B8A6", 12)
        )
        platformCatDao.insertAll(categories)

        // 2. Seed Default User
        val defaultUser = UserProfile(
            name = "عبدالله التاجر",
            email = "abdullah@souqna.app",
            phone = "+966501234567",
            isMerchant = true,
            isAdmin = true,
            storeId = 1L
        )
        userDao.insertUser(defaultUser)

        // 3. Seed Stores
        val store1 = Store(
            id = 1L,
            name = "متجر محمد للإلكترونيات والساعات",
            slug = "mohamed-store",
            description = "متجر متخصص في بيع الساعات الأصلية بما فيها ساعات كاسيو الشهيرة، والإلكترونيات الذكية بضمان معتمد وتوصيل فوري لجميع المدن.",
            city = "الرياض",
            area = "حي العليا، طريق الملك فهد",
            address = "مركز العليا التجاري، الطابق الأول",
            latitude = 24.7136,
            longitude = 46.6753,
            phone = "+966551122334",
            whatsapp = "+966551122334",
            instagram = "@mohamed_store_sa",
            telegram = "@mohamed_store",
            isFeatured = true,
            isVerified = true,
            isActive = true,
            rating = 4.9,
            reviewCount = 28,
            ownerId = 1L,
            ownerName = "محمد القحطاني"
        )

        val store2 = Store(
            id = 2L,
            name = "أناقة الشرق للأزياء",
            slug = "anaqa-east",
            description = "أرقى تشكيلات الملابس الرجالية والنسائية، أثواب فاخرة وعبايات بتطريز يدوي وتصاميم عصرية تمزج بين الأصالة والحداثة.",
            city = "جدة",
            area = "شارع التحلية",
            address = "مجمع التحلية سنتر",
            latitude = 21.5433,
            longitude = 39.1728,
            phone = "+966562233445",
            whatsapp = "+966562233445",
            instagram = "@anaqa_east",
            isFeatured = true,
            isVerified = true,
            isActive = true,
            rating = 4.8,
            reviewCount = 19,
            ownerId = 2L,
            ownerName = "سارة العتيبي"
        )

        val store3 = Store(
            id = 3L,
            name = "ركني للقهوة ومستلزماتها",
            slug = "rokn-coffee",
            description = "محمصتك ومصدرك الأول لمكائن الاسبريسو، أدوات التقطير V60، وأجود محاصيل البن المختص الفاخر من أفضل مزارع العالم.",
            city = "الخبر",
            area = "الكورنيش الشمالي",
            address = "مجمع الواجهة البحرية",
            latitude = 26.2886,
            longitude = 50.2084,
            phone = "+966543344556",
            whatsapp = "+966543344556",
            instagram = "@rokn_coffee_sa",
            isFeatured = true,
            isVerified = true,
            isActive = true,
            rating = 4.95,
            reviewCount = 34,
            ownerId = 3L,
            ownerName = "خالد الدوسري"
        )

        val store4 = Store(
            id = 4L,
            name = "سنيكرز سبورت الرياض",
            slug = "sneakers-sport-riyadh",
            description = "أحدث وأفضل الأحذية الرياضية الأصلية والماركات العالمية للجري والتمارين اليومية بأفضل الأسعار وبمقاسات متنوعة.",
            city = "الرياض",
            area = "حي النخيل",
            address = "طريق الإمام سعود",
            latitude = 24.7500,
            longitude = 46.6500,
            phone = "+966574455667",
            whatsapp = "+966574455667",
            instagram = "@sneakers_sport_sa",
            isFeatured = false,
            isVerified = true,
            isActive = true,
            rating = 4.7,
            reviewCount = 15,
            ownerId = 4L,
            ownerName = "فهد الشمري"
        )

        val store5 = Store(
            id = 5L,
            name = "عالم الألعاب والجيمنج Pro",
            slug = "gaming-world-pro",
            description = "تجميعات بي سي قيمنق، شاشات ألعاب سريعة التردد، كراسي مريحة، وملحقات البلايستيشن والإكس بوكس الأصلية.",
            city = "الدمام",
            area = "حي الشاطئ",
            address = "طريق الخليج",
            latitude = 26.4344,
            longitude = 50.1033,
            phone = "+966585566778",
            whatsapp = "+966585566778",
            instagram = "@gaming_pro_dammam",
            isFeatured = true,
            isVerified = true,
            isActive = true,
            rating = 4.85,
            reviewCount = 22,
            ownerId = 5L,
            ownerName = "سلطان الحربي"
        )

        val store6 = Store(
            id = 6L,
            name = "عبير العود والجمال",
            slug = "abeer-oud-beauty",
            description = "أفخر أنواع العطور الشرقية والفرنسية، دهن العود المعتق، مستحضرات العناية بالبشرة والجمال الطبيعي المعتمد.",
            city = "مكة المكرمة",
            area = "حي العوالي",
            address = "شارع إبراهيم الجفالي",
            latitude = 21.3891,
            longitude = 39.8579,
            phone = "+966596677889",
            whatsapp = "+966596677889",
            instagram = "@abeer_oud_sa",
            isFeatured = false,
            isVerified = true,
            isActive = true,
            rating = 4.9,
            reviewCount = 41,
            ownerId = 6L,
            ownerName = "عبير ميرغني"
        )

        storeDao.insertStore(store1)
        storeDao.insertStore(store2)
        storeDao.insertStore(store3)
        storeDao.insertStore(store4)
        storeDao.insertStore(store5)
        storeDao.insertStore(store6)

        // 4. Seed Store Categories (Internal sections)
        // Store 1 sections
        val s1Cat1 = StoreCategory(id = 1L, storeId = 1L, name = "ساعات كاسيو الأصلية", displayOrder = 1)
        val s1Cat2 = StoreCategory(id = 2L, storeId = 1L, name = "ساعات ذكية وإلكترونيات", displayOrder = 2)
        val s1Cat3 = StoreCategory(id = 3L, storeId = 1L, name = "إكسسوارات الساعات", displayOrder = 3)
        storeCatDao.insertCategory(s1Cat1)
        storeCatDao.insertCategory(s1Cat2)
        storeCatDao.insertCategory(s1Cat3)

        // Store 2 sections
        val s2Cat1 = StoreCategory(id = 4L, storeId = 2L, name = "أثواب رجالية فاخرة", displayOrder = 1)
        val s2Cat2 = StoreCategory(id = 5L, storeId = 2L, name = "عبايات وشيلات", displayOrder = 2)
        storeCatDao.insertCategory(s2Cat1)
        storeCatDao.insertCategory(s2Cat2)

        // Store 3 sections
        val s3Cat1 = StoreCategory(id = 6L, storeId = 3L, name = "مكائن الإسبريسو", displayOrder = 1)
        val s3Cat2 = StoreCategory(id = 7L, storeId = 3L, name = "بن ومحاصيل مختصة", displayOrder = 2)
        storeCatDao.insertCategory(s3Cat1)
        storeCatDao.insertCategory(s3Cat2)

        // 5. Seed Products (Mock demo products removed - Merchants can add their own products)
        // No demo products seeded; store owners add real products via the Merchant Dashboard.

        // 6. Seed Reviews
        val reviews = listOf(
            StoreReview(storeId = 1L, userName = "سعود الشمري", rating = 5, comment = "وصلتني ساعة كاسيو في أقل من 24 ساعة، التغليف ممتاز والساعة أصلية مع كرت الضمان."),
            StoreReview(storeId = 1L, userName = "ريما الحربي", rating = 5, comment = "تعامل راقي وسرعة رد على الواتساب، اشتريت كاسيو جي شوك وطلعت روعة."),
            StoreReview(storeId = 1L, userName = "ماجد الغامدي", rating = 4, comment = "المتجر ممتاز والمنتجات أصلية، أنصح بالتعامل معه."),
            StoreReview(storeId = 2L, userName = "هند العيسى", rating = 5, comment = "خامة العباية تجنن والتطريز متقن جداً، شكراً لكم."),
            StoreReview(storeId = 3L, userName = "فيصل الدوسري", rating = 5, comment = "ماكينة الاسبريسو وصلت بسلام، والمحصول الإثيوبي نكهته استثنائية!")
        )
        reviews.forEach { reviewDao.insertReview(it) }

        // 7. Seed Initial Animated Banner Ads
        val bannerDao = database.bannerAdDao()
        val initialBanners = listOf(
            com.example.data.model.BannerAd(
                id = 1L,
                title = "مهرجان إطلاق منصة سوقنا الإلكترونية 🚀",
                subtitle = "تسوّق مباشرة من أبرز المتاجر الموثقة في المملكة مع تواصل فوري عبر واتساب",
                badgeText = "عرض الافتتاح 🔥",
                imageUrl = "https://images.unsplash.com/photo-1607082348824-0a96f2a4b9da?w=800&auto=format&fit=crop",
                actionText = "استكشف المتاجر",
                targetType = "SEARCH",
                targetPayload = "",
                gradientStartHex = "#059669",
                gradientEndHex = "#065F46",
                isActive = true,
                isAnimated = true,
                displayOrder = 1
            ),
            com.example.data.model.BannerAd(
                id = 2L,
                title = "متجر المملكة للساعات الكلاسيكية ⌚",
                subtitle = "أرقى موديلات الساعات اليابانية والسويسرية الأصلية بأسعار منافسة وضمان شامل",
                badgeText = "متجر موثق ⭐",
                imageUrl = "https://images.unsplash.com/photo-1524805444758-089113d48a6d?w=800&auto=format&fit=crop",
                actionText = "زيارة المتجر",
                targetType = "STORE",
                targetPayload = "1",
                gradientStartHex = "#1E40AF",
                gradientEndHex = "#1E1B4B",
                isActive = true,
                isAnimated = true,
                displayOrder = 2
            ),
            com.example.data.model.BannerAd(
                id = 3L,
                title = "افتح متجرك الإلكتروني مجاناً اليوم 🏪",
                subtitle = "انضم لمئات التجار وابدأ في عرض منتجاتك واستقبال طلبات الزبائن مباشرة",
                badgeText = "فرصة للتجار 🎁",
                imageUrl = "https://images.unsplash.com/photo-1472851294608-062f824d29cc?w=800&auto=format&fit=crop",
                actionText = "أنشئ متجرك",
                targetType = "SPECIAL_OFFER",
                targetPayload = "create_store",
                gradientStartHex = "#D97706",
                gradientEndHex = "#92400E",
                isActive = true,
                isAnimated = true,
                displayOrder = 3
            )
        )
        initialBanners.forEach { bannerDao.insertBanner(it) }
    }
}
