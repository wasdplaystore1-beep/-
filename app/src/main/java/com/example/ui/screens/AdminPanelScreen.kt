package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.BannerAd
import com.example.data.model.Product
import com.example.data.model.Store
import com.example.ui.components.AddEditBannerAdDialog
import com.example.ui.components.AnimatedBannerCard
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.OnEmeraldContainer
import com.example.ui.viewmodel.ScreenNav
import com.example.ui.viewmodel.SouqnaViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    viewModel: SouqnaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val allStores by viewModel.allStoresAdmin.collectAsStateWithLifecycle()
    val allProducts by viewModel.allProductsAdmin.collectAsStateWithLifecycle()
    val categories by viewModel.platformCategories.collectAsStateWithLifecycle()
    val allBanners by viewModel.allBannersAdmin.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) }
    var storeToDelete by remember { mutableStateOf<Store?>(null) }
    var productToDelete by remember { mutableStateOf<Product?>(null) }
    var bannerToDelete by remember { mutableStateOf<BannerAd?>(null) }
    var bannerToEdit by remember { mutableStateOf<BannerAd?>(null) }
    var showAddBannerDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = EmeraldPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "لوحة تحكم إدارة منصة سوقنا",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "رجوع")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Platform Metric KPIs
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "نظرة عامة على أداء المنصة 📊",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Stores Metric
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = EmeraldContainer)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(text = "المتاجر", fontSize = 11.sp, color = OnEmeraldContainer)
                                Text(
                                    text = "${allStores.size}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = EmeraldPrimary
                                )
                            }
                        }

                        // Products Metric
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(text = "المنتجات", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = "${allProducts.size}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        // Categories Metric
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = GoldSecondary.copy(alpha = 0.15f))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(text = "التصنيفات", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                                Text(
                                    text = "${categories.size}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = GoldSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Tabs
            PrimaryTabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = EmeraldPrimary
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("المتاجر (${allStores.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("المنتجات (${allProducts.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("الإعلانات (${allBanners.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
                Tab(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    text = { Text("الأرباح والاشتراك", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
            }

            when (selectedTab) {
                0 -> {
                    // Stores Management Tab (Activate/Deactivate, Feature, Delete)
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(allStores) { store ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = store.name,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "(${store.city})",
                                                    fontSize = 11.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                            Text(
                                                text = "/store/${store.slug} • التاجر: ${store.ownerName}",
                                                fontSize = 11.sp,
                                                color = EmeraldPrimary
                                            )
                                        }

                                        IconButton(onClick = { storeToDelete = store }) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "حذف المتجر",
                                                tint = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Controls: Toggle Active & Toggle Featured
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text("الحالة: ", fontSize = 12.sp)
                                            Text(
                                                text = if (store.isActive) "مفعّل" else "معطل",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (store.isActive) EmeraldPrimary else MaterialTheme.colorScheme.error
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Switch(
                                                checked = store.isActive,
                                                onCheckedChange = { viewModel.adminToggleStoreActive(store.id, it) },
                                                colors = SwitchDefaults.colors(checkedThumbColor = EmeraldPrimary)
                                            )
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text("مميز ⭐: ", fontSize = 12.sp)
                                            Switch(
                                                checked = store.isFeatured,
                                                onCheckedChange = { viewModel.adminToggleStoreFeatured(store.id, it) },
                                                colors = SwitchDefaults.colors(checkedThumbColor = GoldSecondary)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> {
                    // Products Moderation Tab
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(allProducts) { prod ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = prod.title,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "السعر: ${prod.price.roundToInt()} ر.س | المشاهدات: ${prod.viewsCount}",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("مميز: ", fontSize = 11.sp)
                                        Switch(
                                            checked = prod.isFeatured,
                                            onCheckedChange = { viewModel.toggleProductFeatured(prod.id, it) },
                                            colors = SwitchDefaults.colors(checkedThumbColor = GoldSecondary)
                                        )
                                    }

                                    IconButton(onClick = { productToDelete = prod }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "حذف",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // Animated Banners & Ads Management Tab
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            Button(
                                onClick = { showAddBannerDialog = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("admin_add_banner_btn"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("إضافة إعلان متحرك جديد 📢", fontWeight = FontWeight.Bold)
                            }
                        }

                        if (allBanners.isEmpty()) {
                            item {
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(32.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Campaign,
                                            contentDescription = null,
                                            tint = EmeraldPrimary,
                                            modifier = Modifier.size(48.dp)
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(
                                            text = "لا توجد إعلانات متحركة حالياً",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "أضف إعلانات لتظهر للمتسوقين في الصفحة الرئيسية مع حركة تفاعلية",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        } else {
                            items(allBanners) { banner ->
                                Card(
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        // Visual Preview Card
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(140.dp)
                                        ) {
                                            AnimatedBannerCard(
                                                banner = banner,
                                                onClick = {}
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        // Info Row
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text(
                                                    text = "الوجهة: ${
                                                        when (banner.targetType) {
                                                            "STORE" -> "متجر (#${banner.targetPayload})"
                                                            "CATEGORY" -> "تصنيف (${banner.targetPayload})"
                                                            "SEARCH" -> "بحث: ${banner.targetPayload}"
                                                            "SPECIAL_OFFER" -> "عرض خاص"
                                                            else -> "رابط خارجي"
                                                        }
                                                    }",
                                                    fontSize = 12.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                Text(
                                                    text = "المشاهدات: ${banner.viewsCount} | الحركة: ${if (banner.isAnimated) "مفعلة ⚡" else "ثابت"}",
                                                    fontSize = 11.sp,
                                                    color = EmeraldPrimary,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }

                                            // Status Toggle Switch
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = if (banner.isActive) "نشط" else "معطل",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (banner.isActive) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Switch(
                                                    checked = banner.isActive,
                                                    onCheckedChange = { viewModel.toggleBannerStatus(banner) },
                                                    colors = SwitchDefaults.colors(checkedThumbColor = EmeraldPrimary)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Actions Row
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            TextButton(onClick = { bannerToEdit = banner }) {
                                                Icon(
                                                    imageVector = Icons.Default.Edit,
                                                    contentDescription = "تعديل",
                                                    tint = EmeraldPrimary,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("تعديل", color = EmeraldPrimary, fontSize = 12.sp)
                                            }

                                            Spacer(modifier = Modifier.width(8.dp))

                                            TextButton(onClick = { bannerToDelete = banner }) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "حذف",
                                                    tint = MaterialTheme.colorScheme.error,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("حذف", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                3 -> {
                    // Monetization & Subscription Plans Hub
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = EmeraldContainer,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MonetizationOn,
                                        contentDescription = null,
                                        tint = EmeraldPrimary,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "نموذج العمل والاشتراكات المستقبلية",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = OnEmeraldContainer
                                        )
                                        Text(
                                            text = "تم تجهيز الباقات الترويجية والاشتراكات الشهرية وتفعيل الميزات المدفوعة",
                                            fontSize = 11.sp,
                                            color = OnEmeraldContainer.copy(alpha = 0.8f)
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Button(
                                onClick = { viewModel.navigateTo(ScreenNav.Monetization) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                            ) {
                                Text("عرض باقات الاشتراك والترقية للمتاجر ✨", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    // Delete Store Confirmation
    if (storeToDelete != null) {
        AlertDialog(
            onDismissRequest = { storeToDelete = null },
            title = { Text("تأكيد حذف المتجر") },
            text = { Text("هل أنت متأكد من رغبتك في حذف متجر \"${storeToDelete?.name}\" وكافة منتجاته وأقسامه؟") },
            confirmButton = {
                Button(
                    onClick = {
                        storeToDelete?.let { viewModel.adminDeleteStore(it) }
                        storeToDelete = null
                        Toast.makeText(context, "تم حذف المتجر", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("تأكيد الحذف")
                }
            },
            dismissButton = {
                TextButton(onClick = { storeToDelete = null }) {
                    Text("إلغاء")
                }
            }
        )
    }

    // Delete Product Confirmation
    if (productToDelete != null) {
        AlertDialog(
            onDismissRequest = { productToDelete = null },
            title = { Text("تأكيد حذف المنتج") },
            text = { Text("هل أنت متأكد من حذف المنتج \"${productToDelete?.title}\"؟") },
            confirmButton = {
                Button(
                    onClick = {
                        productToDelete?.let { viewModel.adminDeleteProduct(it) }
                        productToDelete = null
                        Toast.makeText(context, "تم حذف المنتج", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("حذف")
                }
            },
            dismissButton = {
                TextButton(onClick = { productToDelete = null }) {
                    Text("إلغاء")
                }
            }
        )
    }

    // Delete Banner Confirmation
    if (bannerToDelete != null) {
        AlertDialog(
            onDismissRequest = { bannerToDelete = null },
            title = { Text("تأكيد حذف الإعلان المتحرك") },
            text = { Text("هل أنت متأكد من حذف هذا الإعلان \"${bannerToDelete?.title}\" نهائياً؟") },
            confirmButton = {
                Button(
                    onClick = {
                        bannerToDelete?.let { viewModel.deleteBannerAd(it) }
                        bannerToDelete = null
                        Toast.makeText(context, "تم حذف الإعلان بنجاح", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("حذف الإعلان")
                }
            },
            dismissButton = {
                TextButton(onClick = { bannerToDelete = null }) {
                    Text("إلغاء")
                }
            }
        )
    }

    // Add Banner Ad Dialog
    if (showAddBannerDialog) {
        AddEditBannerAdDialog(
            availableStores = allStores,
            availableCategories = categories,
            onDismiss = { showAddBannerDialog = false },
            onSave = { title, subtitle, badgeText, imageUrl, actionText, targetType, targetPayload, gradStart, gradEnd, isAnim ->
                viewModel.createBannerAd(
                    title = title,
                    subtitle = subtitle,
                    badgeText = badgeText,
                    imageUrl = imageUrl,
                    actionText = actionText,
                    targetType = targetType,
                    targetPayload = targetPayload,
                    gradientStartHex = gradStart,
                    gradientEndHex = gradEnd,
                    isAnimated = isAnim
                )
                showAddBannerDialog = false
            }
        )
    }

    // Edit Banner Ad Dialog
    if (bannerToEdit != null) {
        AddEditBannerAdDialog(
            existingBanner = bannerToEdit,
            availableStores = allStores,
            availableCategories = categories,
            onDismiss = { bannerToEdit = null },
            onSave = { title, subtitle, badgeText, imageUrl, actionText, targetType, targetPayload, gradStart, gradEnd, isAnim ->
                bannerToEdit?.let { existing ->
                    viewModel.updateBannerAd(
                        existing.copy(
                            title = title,
                            subtitle = subtitle,
                            badgeText = badgeText,
                            imageUrl = imageUrl,
                            actionText = actionText,
                            targetType = targetType,
                            targetPayload = targetPayload,
                            gradientStartHex = gradStart,
                            gradientEndHex = gradEnd,
                            isAnimated = isAnim
                        )
                    )
                }
                bannerToEdit = null
            }
        )
    }
}
