package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.ProductWithStore
import com.example.data.model.StoreReview
import com.example.ui.components.AddReviewDialog
import com.example.ui.components.InteractiveMapCanvas
import com.example.ui.components.ProductGridCard
import com.example.ui.components.ShareLinkDialog
import com.example.ui.components.StarRatingBar
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldPrimaryDark
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.OnEmeraldContainer
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.util.SouqnaUtils
import com.example.ui.viewmodel.SouqnaViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreDetailScreen(
    viewModel: SouqnaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val storeWithDetails by viewModel.selectedStoreDetails.collectAsStateWithLifecycle()

    var selectedSectionId by remember { mutableStateOf<Long?>(null) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var showShareDialog by remember { mutableStateOf(false) }
    var showAddReviewDialog by remember { mutableStateOf(false) }

    val store = storeWithDetails?.store
    val categories = storeWithDetails?.categories ?: emptyList()
    val allProducts = storeWithDetails?.products ?: emptyList()
    val reviews = storeWithDetails?.reviews ?: emptyList()

    val filteredProducts = if (selectedSectionId == null) {
        allProducts
    } else {
        allProducts.filter { it.storeCategoryId == selectedSectionId }
    }

    if (store == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("جاري تحميل بيانات المتجر...")
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = store.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "رجوع")
                    }
                },
                actions = {
                    IconButton(onClick = { showShareDialog = true }) {
                        Icon(Icons.Default.Share, contentDescription = "مشاركة رابط المتجر")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // 1. Store Hero Header & Banner
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    // Header Banner background
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .background(
                                Brush.verticalGradient(
                                    listOf(EmeraldPrimaryDark, EmeraldPrimary)
                                )
                            )
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawCircle(
                                color = Color.White.copy(alpha = 0.08f),
                                radius = size.width * 0.4f,
                                center = Offset(size.width * 0.8f, size.height * 0.2f)
                            )
                        }
                    }

                    // Store Profile Card
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 65.dp, start = 16.dp, end = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                // Store Logo Avatar
                                Box(
                                    modifier = Modifier
                                        .offset(y = (-36).dp)
                                        .size(72.dp)
                                        .shadow(6.dp, CircleShape)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surface)
                                        .border(3.dp, Color.White, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (store.logoUrl.isBlank()) {
                                        Icon(
                                            imageVector = Icons.Default.Storefront,
                                            contentDescription = store.name,
                                            tint = EmeraldPrimary,
                                            modifier = Modifier.size(36.dp)
                                        )
                                    } else {
                                        AsyncImage(
                                            model = ImageRequest.Builder(context)
                                                .data(store.logoUrl)
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = store.name,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }

                                // Badges
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (store.isFeatured) {
                                        Surface(
                                            color = GoldSecondary,
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = "⭐ متجر مميز",
                                                color = Color.Black,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    if (store.isVerified) {
                                        Surface(
                                            color = EmeraldContainer,
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Verified,
                                                    contentDescription = null,
                                                    tint = EmeraldPrimary,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                                Spacer(modifier = Modifier.width(3.dp))
                                                Text(
                                                    text = "موثق",
                                                    color = OnEmeraldContainer,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // Store Name and Handle
                            Text(
                                text = store.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.offset(y = (-14).dp)
                            )

                            // Custom slug identifier
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = EmeraldContainer.copy(alpha = 0.5f),
                                modifier = Modifier.offset(y = (-10).dp)
                            ) {
                                Text(
                                    text = "souqna.app/store/${store.slug}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = EmeraldPrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }

                            // Location and Rating Row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .offset(y = (-4).dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = EmeraldPrimary,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${store.city} - ${store.area}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    StarRatingBar(rating = store.rating.toInt(), starSize = 14)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "(${store.reviewCount} تقييم)",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Store Description
                            Text(
                                text = store.description,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Direct Contact & Action Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        SouqnaUtils.openWhatsApp(
                                            context = context,
                                            phone = store.whatsapp,
                                            message = "مرحباً متجر ${store.name}، تواصلت معكم من خلال صفحتكم على منصة سوقنا."
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = WhatsAppGreen,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                        .testTag("store_whatsapp_contact_btn")
                                ) {
                                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("واتساب", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = { SouqnaUtils.openPhoneCall(context, store.phone) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = EmeraldPrimary,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                ) {
                                    Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("اتصال", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = {
                                        SouqnaUtils.openMapDirections(
                                            context = context,
                                            lat = store.latitude,
                                            lng = store.longitude,
                                            label = store.name
                                        )
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                ) {
                                    Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("الخريطة", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // 2. Tabs: Products / About Store & Location / Reviews
            item {
                Spacer(modifier = Modifier.height(12.dp))
                PrimaryTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = EmeraldPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp).clip(RoundedCornerShape(10.dp))
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("المنتجات (${allProducts.size})", fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("الموقع والخريطة", fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("التقييمات (${reviews.size})", fontWeight = FontWeight.Bold) }
                    )
                }
            }

            when (selectedTab) {
                0 -> {
                    // Store Internal Categories / Sections Chips
                    if (categories.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                item {
                                    FilterChip(
                                        selected = selectedSectionId == null,
                                        onClick = { selectedSectionId = null },
                                        label = { Text("جميع الأقسام (${allProducts.size})") }
                                    )
                                }
                                items(categories) { section ->
                                    val count = allProducts.count { it.storeCategoryId == section.id }
                                    FilterChip(
                                        selected = selectedSectionId == section.id,
                                        onClick = {
                                            selectedSectionId = if (selectedSectionId == section.id) null else section.id
                                        },
                                        label = { Text("${section.name} ($count)") }
                                    )
                                }
                            }
                        }
                    }

                    if (filteredProducts.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "لا توجد منتجات مضافة في هذا القسم حالياً",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    } else {
                        val chunked = filteredProducts.chunked(2)
                        items(chunked) { rowItems ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                for (prod in rowItems) {
                                    Box(modifier = Modifier.weight(1f)) {
                                        ProductGridCard(
                                            productWithStore = ProductWithStore(product = prod, store = store),
                                            onProductClick = { viewModel.selectProduct(prod.id) },
                                            onStoreClick = { /* Already in store */ }
                                        )
                                    }
                                }
                                if (rowItems.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
                1 -> {
                    // Store Map & Physical Location Details Tab
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "موقع المتجر الجغرافي 📍",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${store.city}، ${store.area} - ${store.address}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Store Mini Map
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                    ) {
                                        InteractiveMapCanvas(
                                            stores = listOf(store),
                                            selectedStore = store,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Button(
                                        onClick = {
                                            SouqnaUtils.openMapDirections(
                                                context = context,
                                                lat = store.latitude,
                                                lng = store.longitude,
                                                label = store.name
                                            )
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("فتح الموقع في تطبيق الخرائط")
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // Reviews & Ratings Tab
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            // Review Summary & Add Review button
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "${store.rating}",
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = GoldSecondary
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            StarRatingBar(rating = store.rating.toInt(), starSize = 16)
                                        }
                                        Text(
                                            text = "بناءً على ${store.reviewCount} تقييم من العملاء",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Button(
                                        onClick = { showAddReviewDialog = true },
                                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Icon(Icons.Default.RateReview, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("أضف تقييمك", fontSize = 12.sp)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    if (reviews.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "كن أول من يكتب تقييماً وتجربة لمتجر ${store.name}!",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    } else {
                        items(reviews) { review ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = review.userName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                        StarRatingBar(rating = review.rating, starSize = 12)
                                    }
                                    if (review.comment.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = review.comment,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Web Footer
            item {
                Spacer(modifier = Modifier.height(24.dp))
                com.example.ui.components.WebFooter(viewModel = viewModel)
            }
        }
    }

    // Share Dialog
    if (showShareDialog) {
        ShareLinkDialog(
            title = store.name,
            urlPath = "/store/${store.slug}",
            shareText = "تصفح متجر ${store.name} على منصة سوقنا وشاهد كافة منتجاتنا المميزة:",
            onDismiss = { showShareDialog = false }
        )
    }

    // Add Review Dialog
    if (showAddReviewDialog) {
        AddReviewDialog(
            storeName = store.name,
            onSubmit = { rating, comment, name ->
                viewModel.addStoreReview(
                    storeId = store.id,
                    userName = name,
                    rating = rating,
                    comment = comment
                )
                showAddReviewDialog = false
                Toast.makeText(context, "شكراً لك! تم نشر تقييمك بنجاح.", Toast.LENGTH_SHORT).show()
            },
            onDismiss = { showAddReviewDialog = false }
        )
    }
}
