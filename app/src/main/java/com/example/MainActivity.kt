package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.WebBrowserBar
import com.example.ui.components.WebHeaderNavbar
import com.example.ui.screens.AddEditProductScreen
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.CategoryProductsScreen
import com.example.ui.screens.CreateStoreScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.InteractiveMapScreen
import com.example.ui.screens.MerchantDashboardScreen
import com.example.ui.screens.MonetizationScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.StoreDetailScreen
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.OnEmeraldContainer
import com.example.ui.theme.SouqnaTheme
import com.example.ui.viewmodel.ScreenNav
import com.example.ui.viewmodel.SouqnaViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: SouqnaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SouqnaTheme {
                // Ensure complete Arabic Right-to-Left (RTL) Layout
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    SouqnaApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun SouqnaApp(viewModel: SouqnaViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val merchantStore by viewModel.myMerchantStore.collectAsStateWithLifecycle()
    val isWebMode by viewModel.isWebMode.collectAsStateWithLifecycle()

    // Handle System Back Press
    BackHandler {
        viewModel.navigateBack()
    }

    // Determine active bottom navigation item
    val isBottomBarVisible = when (currentScreen) {
        is ScreenNav.Home, is ScreenNav.Search, is ScreenNav.InteractiveMap, is ScreenNav.Profile, is ScreenNav.MerchantDashboard -> true
        else -> false
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                // Interactive Browser Address & Control Bar
                WebBrowserBar(viewModel = viewModel)

                // Web Portal Top Navigation & Breadcrumbs
                if (isWebMode) {
                    WebHeaderNavbar(viewModel = viewModel)
                }
            }
        },
        bottomBar = {
            if (isBottomBarVisible) {
                Surface(
                    shadowElevation = 8.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = EmeraldPrimary,
                        tonalElevation = 0.dp
                    ) {
                        // 1. Home
                        NavigationBarItem(
                            selected = currentScreen is ScreenNav.Home,
                            onClick = { viewModel.navigateToTab(ScreenNav.Home) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "الرئيسية",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = "الرئيسية",
                                    fontSize = 11.sp,
                                    fontWeight = if (currentScreen is ScreenNav.Home) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = EmeraldPrimary,
                                selectedTextColor = EmeraldPrimary,
                                indicatorColor = EmeraldContainer
                            ),
                            modifier = Modifier.testTag("nav_home")
                        )

                        // 2. Unified Search
                        NavigationBarItem(
                            selected = currentScreen is ScreenNav.Search,
                            onClick = { viewModel.navigateToTab(ScreenNav.Search) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "البحث الموحد",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = "البحث",
                                    fontSize = 11.sp,
                                    fontWeight = if (currentScreen is ScreenNav.Search) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = EmeraldPrimary,
                                selectedTextColor = EmeraldPrimary,
                                indicatorColor = EmeraldContainer
                            ),
                            modifier = Modifier.testTag("nav_search")
                        )

                        // 3. Map
                        NavigationBarItem(
                            selected = currentScreen is ScreenNav.InteractiveMap,
                            onClick = { viewModel.navigateToTab(ScreenNav.InteractiveMap) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Map,
                                    contentDescription = "الخريطة",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = "الخريطة",
                                    fontSize = 11.sp,
                                    fontWeight = if (currentScreen is ScreenNav.InteractiveMap) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = EmeraldPrimary,
                                selectedTextColor = EmeraldPrimary,
                                indicatorColor = EmeraldContainer
                            ),
                            modifier = Modifier.testTag("nav_map")
                        )

                        // 4. Merchant Dashboard / Create Store
                        val isStoreTab = currentScreen is ScreenNav.MerchantDashboard || currentScreen is ScreenNav.CreateStore
                        NavigationBarItem(
                            selected = isStoreTab,
                            onClick = {
                                if (merchantStore != null) {
                                    viewModel.navigateToTab(ScreenNav.MerchantDashboard)
                                } else {
                                    viewModel.navigateTo(ScreenNav.CreateStore)
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (merchantStore != null) Icons.Default.Storefront else Icons.Default.AddBusiness,
                                    contentDescription = "متجري",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = if (merchantStore != null) "متجري" else "أنشئ متجر",
                                    fontSize = 11.sp,
                                    fontWeight = if (isStoreTab) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = EmeraldPrimary,
                                selectedTextColor = EmeraldPrimary,
                                indicatorColor = EmeraldContainer
                            ),
                            modifier = Modifier.testTag("nav_store")
                        )

                        // 5. Profile & Settings
                        NavigationBarItem(
                            selected = currentScreen is ScreenNav.Profile,
                            onClick = { viewModel.navigateToTab(ScreenNav.Profile) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "حسابي",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = "حسابي",
                                    fontSize = 11.sp,
                                    fontWeight = if (currentScreen is ScreenNav.Profile) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = EmeraldPrimary,
                                selectedTextColor = EmeraldPrimary,
                                indicatorColor = EmeraldContainer
                            ),
                            modifier = Modifier.testTag("nav_profile")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "ScreenTransition"
            ) { targetScreen ->
                when (targetScreen) {
                    is ScreenNav.Home -> HomeScreen(viewModel = viewModel)
                    is ScreenNav.Search -> SearchScreen(viewModel = viewModel)
                    is ScreenNav.StoreDetail -> StoreDetailScreen(viewModel = viewModel)
                    is ScreenNav.ProductDetail -> ProductDetailScreen(viewModel = viewModel)
                    is ScreenNav.CategoryProducts -> CategoryProductsScreen(
                        categoryKey = targetScreen.categoryKey,
                        categoryName = targetScreen.categoryName,
                        viewModel = viewModel
                    )
                    is ScreenNav.CreateStore -> CreateStoreScreen(viewModel = viewModel)
                    is ScreenNav.MerchantDashboard -> MerchantDashboardScreen(viewModel = viewModel)
                    is ScreenNav.AddEditProduct -> AddEditProductScreen(
                        productId = targetScreen.productId,
                        viewModel = viewModel
                    )
                    is ScreenNav.AdminPanel -> AdminPanelScreen(viewModel = viewModel)
                    is ScreenNav.InteractiveMap -> InteractiveMapScreen(viewModel = viewModel)
                    is ScreenNav.Monetization -> MonetizationScreen(viewModel = viewModel)
                    is ScreenNav.Profile -> ProfileScreen(viewModel = viewModel)
                }
            }
        }
    }
}
