package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.Store
import com.example.ui.components.InteractiveMapCanvas
import com.example.ui.components.StoreCard
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.util.SouqnaUtils
import com.example.ui.viewmodel.SouqnaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteractiveMapScreen(
    viewModel: SouqnaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val allStores by viewModel.allActiveStores.collectAsStateWithLifecycle()

    var selectedCity by remember { mutableStateOf<String?>(null) }
    var selectedStore by remember { mutableStateOf<Store?>(null) }

    val cities = listOf("الرياض", "جدة", "الخبر", "الدمام", "مكة المكرمة")
    val filteredStores = if (selectedCity == null) allStores else allStores.filter { it.city == selectedCity }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "خريطة المتاجر التفاعلية 🗺️",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
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
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Full Screen Map Canvas
            InteractiveMapCanvas(
                stores = filteredStores,
                selectedStore = selectedStore,
                onStoreSelected = { store ->
                    selectedStore = store
                },
                modifier = Modifier.fillMaxSize()
            )

            // Top City Filter Chips
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 12.dp)
            ) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedCity == null,
                            onClick = { selectedCity = null },
                            label = { Text("جميع المدن (${allStores.size})") }
                        )
                    }
                    items(cities) { city ->
                        val count = allStores.count { it.city == city }
                        FilterChip(
                            selected = selectedCity == city,
                            onClick = { selectedCity = if (selectedCity == city) null else city },
                            label = { Text("$city ($count)") }
                        )
                    }
                }
            }

            // Bottom Selected Store Card Preview or Carousel
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 20.dp, start = 16.dp, end = 16.dp)
            ) {
                if (selectedStore != null) {
                    StoreCard(
                        store = selectedStore!!,
                        onStoreClick = { viewModel.selectStore(selectedStore!!.id) },
                        onWhatsAppClick = {
                            SouqnaUtils.openWhatsApp(
                                context = context,
                                phone = selectedStore!!.whatsapp,
                                message = "مرحباً ${selectedStore!!.name}، شاهدت موقع متجركم على خريطة سوقنا."
                            )
                        }
                    )
                } else {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                        shadowElevation = 4.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = EmeraldPrimary
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "انقر واسحب الخريطة لاستعراض المتاجر، أو انقر على أي متجر أدناه لمعاينته",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredStores) { store ->
                            Box(modifier = Modifier.fillParentMaxWidth(0.85f)) {
                                StoreCard(
                                    store = store,
                                    onStoreClick = { viewModel.selectStore(store.id) },
                                    onWhatsAppClick = {
                                        SouqnaUtils.openWhatsApp(
                                            context = context,
                                            phone = store.whatsapp,
                                            message = "مرحباً ${store.name}، شاهدت متجركم على خريطة سوقنا."
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
