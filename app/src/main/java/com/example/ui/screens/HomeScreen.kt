package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.CategoryChip
import com.example.ui.components.InteractiveMapCanvas
import com.example.ui.components.ProductGridCard
import com.example.ui.components.StoreCard
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldPrimaryDark
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.OnEmeraldContainer
import com.example.ui.util.SouqnaUtils
import com.example.ui.viewmodel.ScreenNav
import com.example.ui.viewmodel.SearchTab
import com.example.ui.viewmodel.SouqnaViewModel

@Composable
fun HomeScreen(
    viewModel: SouqnaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val categories by viewModel.platformCategories.collectAsStateWithLifecycle()
    val featuredStores by viewModel.featuredStores.collectAsStateWithLifecycle()
    val allActiveStores by viewModel.allActiveStores.collectAsStateWithLifecycle()
    val newStores by viewModel.newStores.collectAsStateWithLifecycle()
    val allProductsWithStore by viewModel.allProductsWithStore.collectAsStateWithLifecycle()

    val featuredProducts = allProductsWithStore.filter { it.product.isFeatured }
    val latestProducts = allProductsWithStore.sortedByDescending { it.product.createdAt }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // 1. Top Header & Search Bar
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                EmeraldPrimaryDark,
                                EmeraldPrimary
                            )
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column {
                    // Logo and Title Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.2f),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.ShoppingBag,
                                        contentDescription = "سوقنا",
                                        tint = GoldSecondary,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "سوقنا | Souqna",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                                Text(
                                    text = "منصة المتاجر والتسوق الموحد",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            }
                        }

                        // Create Store Button in Header
                        Button(
                            onClick = { viewModel.navigateTo(ScreenNav.CreateStore) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldSecondary,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("create_store_header_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddBusiness,
                                contentDescription = "أنشئ متجرك",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "أنشئ متجرك",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Prominent Unified Search Bar
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 4.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                viewModel.navigateTo(ScreenNav.Search)
                            }
                            .testTag("home_search_bar_trigger")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "بحث",
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "ابحث عن ساعة، ملابس، إلكترونيات، أو متجر...",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "تصفية",
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // 2. Banner: Free Store Registration & Unified Search
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { viewModel.navigateTo(ScreenNav.CreateStore) }
                    .testTag("free_store_banner"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = EmeraldContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Surface(
                            color = EmeraldPrimary,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "مجاناً لفترة محدودة 🎁",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "هل تملك متجراً أو منتجات؟",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnEmeraldContainer
                        )
                        Text(
                            text = "افتح متجرك في سوقنا في دقيقة واحصل على رابط خاص",
                            fontSize = 11.sp,
                            color = OnEmeraldContainer.copy(alpha = 0.8f)
                        )
                    }
                    Button(
                        onClick = { viewModel.navigateTo(ScreenNav.CreateStore) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmeraldPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "سجل متجرك", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 3. Platform Categories Grid / Row
        item {
            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "التصنيفات الرئيسية 🏷️",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        CategoryChip(
                            category = category,
                            isSelected = false,
                            onClick = { viewModel.selectCategory(category) }
                        )
                    }
                }
            }
        }

        // 4. Featured Stores Spotlight
        item {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "المتاجر المميزة ⭐",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    TextButton(onClick = {
                        viewModel.setSearchTab(SearchTab.STORES)
                        viewModel.navigateTo(ScreenNav.Search)
                    }) {
                        Text(
                            text = "عرض الكل",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(featuredStores) { store ->
                        Box(modifier = Modifier.width(300.dp)) {
                            StoreCard(
                                store = store,
                                onStoreClick = { viewModel.selectStore(store.id) },
                                onWhatsAppClick = {
                                    SouqnaUtils.openWhatsApp(
                                        context = context,
                                        phone = store.whatsapp,
                                        message = "مرحباً متجر ${store.name}، تواصلت معكم عبر منصة سوقنا."
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        // 5. Featured Products Spotlight
        item {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 20.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "المنتجات المميزة 💎",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    TextButton(onClick = {
                        viewModel.setSearchTab(SearchTab.PRODUCTS)
                        viewModel.navigateTo(ScreenNav.Search)
                    }) {
                        Text(
                            text = "تصفح المنتجات",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(featuredProducts) { item ->
                        Box(modifier = Modifier.width(180.dp)) {
                            ProductGridCard(
                                productWithStore = item,
                                onProductClick = { viewModel.selectProduct(item.product.id) },
                                onStoreClick = { viewModel.selectStore(item.store.id) }
                            )
                        }
                    }
                }
            }
        }

        // 6. Interactive Map Shortcut & Nearby Stores
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "خريطة المتاجر القريبة 🗺️",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    TextButton(onClick = { viewModel.navigateTo(ScreenNav.InteractiveMap) }) {
                        Text(
                            text = "توسيع الخريطة",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { viewModel.navigateTo(ScreenNav.InteractiveMap) }
                ) {
                    InteractiveMapCanvas(
                        stores = allActiveStores,
                        onStoreSelected = { store -> viewModel.selectStore(store.id) },
                        modifier = Modifier.fillMaxSize()
                    )
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                        shadowElevation = 3.dp,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Explore,
                                contentDescription = "استكشف",
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "انقر لفتح الخريطة التفاعلية والبحث بالموقع",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // 7. Latest Products Feed
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "أحدث المنتجات في المنصة 🛍️",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        val chunkedProducts = latestProducts.chunked(2)
        items(chunkedProducts) { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (item in rowItems) {
                    Box(modifier = Modifier.weight(1f)) {
                        ProductGridCard(
                            productWithStore = item,
                            onProductClick = { viewModel.selectProduct(item.product.id) },
                            onStoreClick = { viewModel.selectStore(item.store.id) }
                        )
                    }
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        // 8. New Stores
        item {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 20.dp)) {
                Text(
                    text = "متاجر انضمت حديثاً 🆕",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(newStores) { store ->
                        Box(modifier = Modifier.width(280.dp)) {
                            StoreCard(
                                store = store,
                                onStoreClick = { viewModel.selectStore(store.id) },
                                onWhatsAppClick = {
                                    SouqnaUtils.openWhatsApp(
                                        context = context,
                                        phone = store.whatsapp,
                                        message = "مرحباً ${store.name}، رأيت متجركم في سوقنا وأود الاستفسار."
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        // 9. Website Footer (Souqna.app Web Experience)
        item {
            Spacer(modifier = Modifier.height(24.dp))
            com.example.ui.components.WebFooter(viewModel = viewModel)
        }
    }
}
