package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldPrimaryDark
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.OnEmeraldContainer
import com.example.ui.viewmodel.ScreenNav
import com.example.ui.viewmodel.SouqnaViewModel

@Composable
fun WebHeaderNavbar(
    viewModel: SouqnaViewModel,
    modifier: Modifier = Modifier
) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val myStore by viewModel.myMerchantStore.collectAsStateWithLifecycle()
    val allActiveStores by viewModel.allActiveStores.collectAsStateWithLifecycle()
    val platformCategories by viewModel.platformCategories.collectAsStateWithLifecycle()

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Main Top Branding & Quick Actions Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo & Web Domain Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { viewModel.navigateToTab(ScreenNav.Home) }
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(EmeraldPrimary, EmeraldPrimaryDark)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "سوقنا",
                            tint = GoldSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "سوقنا",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimaryDark
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = EmeraldContainer
                            ) {
                                Text(
                                    text = ".app",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnEmeraldContainer,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = "منصة المتاجر الرقمية المتكاملة",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Action CTA Buttons (Web Navbar Actions)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Create Store CTA (If user has no store) or Merchant Dashboard
                    if (myStore != null) {
                        Button(
                            onClick = { viewModel.navigateToTab(ScreenNav.MerchantDashboard) },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Dashboard,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "لوحة التاجر", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = { viewModel.navigateTo(ScreenNav.CreateStore) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EmeraldPrimary
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddBusiness,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "أنشئ متجرك مجاناً", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Web Navigation Links Menu Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8FAFC))
                    .border(
                        width = 1.dp,
                        color = Color(0xFFE2E8F0)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                WebNavLink(
                    title = "الرئيسية",
                    icon = Icons.Default.Home,
                    isSelected = currentScreen is ScreenNav.Home,
                    onClick = { viewModel.navigateToTab(ScreenNav.Home) }
                )
                WebNavLink(
                    title = "البحث الموحد",
                    icon = Icons.Default.Search,
                    isSelected = currentScreen is ScreenNav.Search,
                    onClick = { viewModel.navigateToTab(ScreenNav.Search) }
                )
                WebNavLink(
                    title = "خريطة المتاجر",
                    icon = Icons.Default.Map,
                    isSelected = currentScreen is ScreenNav.InteractiveMap,
                    onClick = { viewModel.navigateToTab(ScreenNav.InteractiveMap) }
                )
                WebNavLink(
                    title = "باقات الاشتراك",
                    icon = Icons.Default.Diamond,
                    isSelected = currentScreen is ScreenNav.Monetization,
                    onClick = { viewModel.navigateTo(ScreenNav.Monetization) }
                )
                WebNavLink(
                    title = "لوحة الإدارة",
                    icon = Icons.Default.Security,
                    isSelected = currentScreen is ScreenNav.AdminPanel,
                    onClick = { viewModel.navigateTo(ScreenNav.AdminPanel) }
                )
                WebNavLink(
                    title = "حسابي",
                    icon = Icons.Default.Person,
                    isSelected = currentScreen is ScreenNav.Profile,
                    onClick = { viewModel.navigateToTab(ScreenNav.Profile) }
                )
            }

            // Website Breadcrumbs Trail
            val breadcrumbText = getBreadcrumbsText(currentScreen, allActiveStores, platformCategories)
            if (breadcrumbText.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF1F5F9))
                        .padding(horizontal = 14.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "مسار التصفح: ",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )
                    Text(
                        text = breadcrumbText,
                        fontSize = 11.sp,
                        color = EmeraldPrimaryDark,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun WebNavLink(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = if (isSelected) EmeraldContainer else Color.Transparent,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) EmeraldPrimary else Color(0xFF475569),
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) EmeraldPrimaryDark else Color(0xFF334155)
            )
        }
    }
}

private fun getBreadcrumbsText(
    screen: ScreenNav,
    stores: List<com.example.data.model.Store>,
    categories: List<com.example.data.model.PlatformCategory>
): String {
    return when (screen) {
        is ScreenNav.Home -> "الرئيسية (souqna.app)"
        is ScreenNav.Search -> "الرئيسية > البحث الموحد والفلترة"
        is ScreenNav.InteractiveMap -> "الرئيسية > خريطة المتاجر التفاعلية"
        is ScreenNav.CreateStore -> "الرئيسية > بوابة التجار > إنشاء متجر جديد"
        is ScreenNav.MerchantDashboard -> "الرئيسية > بوابة التجار > لوحة التحكم وإدارة المنتجات"
        is ScreenNav.AddEditProduct -> "الرئيسية > لوحة التاجر > إضافة / تعديل منتج"
        is ScreenNav.AdminPanel -> "الرئيسية > لوحة إدارة المنصة والرقابة"
        is ScreenNav.Monetization -> "الرئيسية > باقات الاشتراك والترقية"
        is ScreenNav.Profile -> "الرئيسية > الملف الشخصي والإعدادات"
        is ScreenNav.CategoryProducts -> "الرئيسية > الأقسام > ${screen.categoryName}"
        is ScreenNav.StoreDetail -> {
            val store = stores.find { it.id == screen.storeId }
            "الرئيسية > المتاجر > ${store?.name ?: "متجر"}"
        }
        is ScreenNav.ProductDetail -> "الرئيسية > المنتجات > تفاصيل المنتج"
    }
}
