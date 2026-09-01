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

        // 5. Seed Products (Cross-store searchable products)
        val products = listOf(
            // Store 1: Watches & Electronics
            Product(
                id = 1L,
                storeId = 1L,
                storeCategoryId = 1L,
                platformCategoryKey = "watches_accessories",
                title = "ساعة كاسيو فينتاج كلاسيك ذهبية ديجيتال أصلية (Casio A168WG)",
                slug = "casio-vintage-gold-a168",
                description = "ساعة كاسيو الكلاسيكية باللون الذهبي الأيقوني المقاوم للصدأ، شاشة إلكترونية بإضاءة إلكترولومينيسنت، مقاومة للماء، منبه يومي ومؤقت دقيق. ضمان الوكيل سنتين.",
                price = 185.0,
                originalPrice = 240.0,
                imageUrl = "https://images.unsplash.com/photo-1524805444758-089113d48a6d?w=600&auto=format&fit=crop",
                isFeatured = true,
                inStock = true,
                viewsCount = 1420
            ),
            Product(
                id = 2L,
                storeId = 1L,
                storeCategoryId = 1L,
                platformCategoryKey = "watches_accessories",
                title = "ساعة كاسيو جي شوك عسكرية مضادة للصدمات (Casio G-Shock GA-2100)",
                slug = "casio-g-shock-ga2100-black",
                description = "ساعة كاسيو جي شوك الأصلية بهيكل كربوني فائق المتانة، مقاومة للماء حتى عمق 200 متر، تصميم ثماني أضلاع أسود مائل للأناقة والصلابة.",
                price = 450.0,
                originalPrice = 520.0,
                imageUrl = "https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?w=600&auto=format&fit=crop",
                isFeatured = true,
                inStock = true,
                viewsCount = 980
            ),
            Product(
                id = 3L,
                storeId = 1L,
                storeCategoryId = 1L,
                platformCategoryKey = "watches_accessories",
                title = "ساعة كاسيو إديفيس رياضية كرونوغراف ستانلس ستيل (Casio Edifice)",
                slug = "casio-edifice-chronograph-steel",
                description = "ساعة كاسيو إديفيس الفاخرة للرجال مع ميناء أزرق داكن وعقارب كرونوغراف دقيقة، زجاج كريستال ياقوتي وسوار من الفولاذ المقاوم للصدأ.",
                price = 380.0,
                originalPrice = 450.0,
                imageUrl = "https://images.unsplash.com/photo-1533139502658-0198f920d8e8?w=600&auto=format&fit=crop",
                isFeatured = false,
                inStock = true,
                viewsCount = 640
            ),
            Product(
                id = 4L,
                storeId = 1L,
                storeCategoryId = 2L,
                platformCategoryKey = "phones_electronics",
                title = "سماعات آبل ايربودز برو الجيل الثاني بمنفذ تايب سي (Apple AirPods Pro 2)",
                slug = "apple-airpods-pro-2-type-c",
                description = "سماعات لاسلكية بإلغاء الضوضاء النشط المحسّن بمقدار الضعف، ميزة الصوت المكاني المخصص وعلبة MagSafe المتطورة.",
                price = 849.0,
                originalPrice = 999.0,
                imageUrl = "https://images.unsplash.com/photo-1600294037681-c80b4cb5b434?w=600&auto=format&fit=crop",
                isFeatured = true,
                inStock = true,
                viewsCount = 2100
            ),
            // Store 2: Fashion
            Product(
                id = 5L,
                storeId = 2L,
                storeCategoryId = 4L,
                platformCategoryKey = "fashion_clothing",
                title = "ثوب الدفة الفاخر قطن ياباني سلك مريح وأنيق",
                slug = "thobe-daffah-japanese-cotton",
                description = "ثوب سعودي أصيل بخياطة دقيقة وياقة ملكية راقية، قماش بارد ومقاوم للتجعد ومثالي لجميع المناسبات والاجتماعات.",
                price = 220.0,
                originalPrice = 280.0,
                imageUrl = "https://images.unsplash.com/photo-1593030761757-71fae45fa0e7?w=600&auto=format&fit=crop",
                isFeatured = true,
                inStock = true,
                viewsCount = 850
            ),
            Product(
                id = 6L,
                storeId = 2L,
                storeCategoryId = 5L,
                platformCategoryKey = "fashion_clothing",
                title = "عباية كريب ملكي سوداء بقصة كلوش وتطريز أكمام ناعم",
                slug = "abaya-crepe-black-embroidered",
                description = "عباية صيفية انسيابية من خامة الكريب الملكي الكوري الفاخر مع طرحة مجانية متناسقة بقماش ليزر بارد.",
                price = 310.0,
                originalPrice = 390.0,
                imageUrl = "https://images.unsplash.com/photo-1583391733956-3750e0ff4e8b?w=600&auto=format&fit=crop",
                isFeatured = false,
                inStock = true,
                viewsCount = 670
            ),
            // Store 3: Coffee
            Product(
                id = 7L,
                storeId = 3L,
                storeCategoryId = 6L,
                platformCategoryKey = "home_living",
                title = "ماكينة قهوة اسبريسو بريفيل باريستا برو مع طاحونة مدمجة",
                slug = "breville-barista-pro-espresso",
                description = "أفضل ماكينة قهوة منزلية بنظام تسخين ThermoJet في 3 ثوانٍ فقط، شاشة LCD تفاعلية ومطحنة حبوب مدمجة مع 30 درجة طحن.",
                price = 3250.0,
                originalPrice = 3699.0,
                imageUrl = "https://images.unsplash.com/photo-1517668808822-9ebb02f2a0e6?w=600&auto=format&fit=crop",
                isFeatured = true,
                inStock = true,
                viewsCount = 1890
            ),
            Product(
                id = 8L,
                storeId = 3L,
                storeCategoryId = 7L,
                platformCategoryKey = "food_gourmet",
                title = "محصول قهوة مختصة إثيوبيا قوجي مجففة 250 جرام",
                slug = "ethiopian-coffee-guji-250g",
                description = "إيحاءات التوت الأسود، الخوخ، وحلاوة العسل مع قوام حريري ممتع، معالجة مجففة ومحمصة طازجة لمشروبات الفلتر والاسبريسو.",
                price = 58.0,
                originalPrice = 70.0,
                imageUrl = "https://images.unsplash.com/photo-1559056199-641a0ac8b55e?w=600&auto=format&fit=crop",
                isFeatured = false,
                inStock = true,
                viewsCount = 520
            ),
            // Store 4: Sneakers
            Product(
                id = 9L,
                storeId = 4L,
                platformCategoryKey = "shoes_bags",
                title = "حذاء نايكي أير زوم بيجاسوس الرياضي للجري والتمارين اليومية",
                slug = "nike-air-zoom-pegasus-shoes",
                description = "حذاء رياضي خفيف الوزن بوسائد هوائية مزدوجة Air Zoom واستجابة فورية تقلل الإجهاد وتمنحك ثباتاً رائعاً.",
                price = 490.0,
                originalPrice = 599.0,
                imageUrl = "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600&auto=format&fit=crop",
                isFeatured = true,
                inStock = true,
                viewsCount = 1350
            ),
            // Store 5: Gaming
            Product(
                id = 10L,
                storeId = 5L,
                platformCategoryKey = "gaming_entertainment",
                title = "جهاز بلايستيشن 5 سليم إصدار الأقراص مع يد تحكم دوال سينس",
                slug = "playstation-5-slim-disc-edition",
                description = "إصدار النحيف الجديد من PS5 بسعة تخزين 1 تيرابايت SSD فائق السرعة، دعم دقة 4K ومعدل تحديث يصل إلى 120 إطار بالثانية.",
                price = 2149.0,
                originalPrice = 2399.0,
                imageUrl = "https://images.unsplash.com/photo-1606813907291-d86efa9b94db?w=600&auto=format&fit=crop",
                isFeatured = true,
                inStock = true,
                viewsCount = 3100
            ),
            Product(
                id = 11L,
                storeId = 5L,
                platformCategoryKey = "computers_tech",
                title = "لوحة مفاتيح ميكانيكية قيمنق RGB سويتش أحمر صامت سريع",
                slug = "mechanical-keyboard-rgb-gaming",
                description = "كيبورد احترافي بأزرار ميكانيكية متجاوبة وإضاءة خلفية قابلة للتخصيص بأكثر من 16 مليون لون.",
                price = 299.0,
                originalPrice = 360.0,
                imageUrl = "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600&auto=format&fit=crop",
                isFeatured = false,
                inStock = true,
                viewsCount = 430
            ),
            // Store 6: Beauty
            Product(
                id = 12L,
                storeId = 6L,
                platformCategoryKey = "beauty_care",
                title = "دهن عود براشين ملكي قديم فاخر نخب أول معتق",
                slug = "oud-prachin-royal-aged",
                description = "دهن عود صافي وطبيعي 100% بنكهة سويتية بخورية هادئة وفوحان يدوم لأكثر من 24 ساعة على الثياب.",
                price = 320.0,
                originalPrice = 420.0,
                imageUrl = "https://images.unsplash.com/photo-1594035910387-fea47794261f?w=600&auto=format&fit=crop",
                isFeatured = true,
                inStock = true,
                viewsCount = 920
            )
        )

        products.forEach { productDao.insertProduct(it) }

        // 6. Seed Reviews
        val reviews = listOf(
            StoreReview(storeId = 1L, userName = "سعود الشمري", rating = 5, comment = "وصلتني ساعة كاسيو في أقل من 24 ساعة، التغليف ممتاز والساعة أصلية مع كرت الضمان."),
            StoreReview(storeId = 1L, userName = "ريما الحربي", rating = 5, comment = "تعامل راقي وسرعة رد على الواتساب، اشتريت كاسيو جي شوك وطلعت روعة."),
            StoreReview(storeId = 1L, userName = "ماجد الغامدي", rating = 4, comment = "المتجر ممتاز والمنتجات أصلية، أنصح بالتعامل معه."),
            StoreReview(storeId = 2L, userName = "هند العيسى", rating = 5, comment = "خامة العباية تجنن والتطريز متقن جداً، شكراً لكم."),
            StoreReview(storeId = 3L, userName = "فيصل الدوسري", rating = 5, comment = "ماكينة الاسبريسو وصلت بسلام، والمحصول الإثيوبي نكهته استثنائية!")
        )
        reviews.forEach { reviewDao.insertReview(it) }
    }
}
