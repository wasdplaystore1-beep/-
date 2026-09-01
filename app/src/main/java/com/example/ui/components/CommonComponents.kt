package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.PlatformCategory
import com.example.data.model.Product
import com.example.data.model.ProductWithStore
import com.example.data.model.Store
import com.example.data.repository.ProductFilterCriteria
import com.example.data.repository.ProductSortOption
import com.example.ui.theme.AccentSuccess
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.OnEmeraldContainer
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.util.SouqnaUtils
import kotlin.math.roundToInt

@Composable
fun ProductGridCard(
    productWithStore: ProductWithStore,
    onProductClick: () -> Unit,
    onStoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val product = productWithStore.product
    val store = productWithStore.store

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onProductClick() }
            .testTag("product_card_${product.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Product Image Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Featured Badge
                if (product.isFeatured) {
                    Surface(
                        color = GoldSecondary,
                        shape = RoundedCornerShape(topStart = 0.dp, bottomStart = 8.dp, topEnd = 0.dp, bottomEnd = 0.dp),
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Text(
                            text = "⭐ مميز",
                            color = Color.Black,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                // Discount Badge if available
                if (product.originalPrice != null && product.originalPrice > product.price) {
                    val discountPercent = (((product.originalPrice - product.price) / product.originalPrice) * 100).roundToInt()
                    Surface(
                        color = MaterialTheme.colorScheme.error,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "-$discountPercent%",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Product Details
            Column(modifier = Modifier.padding(12.dp)) {
                // Merchant Store Header Tag (Clickable to visit store)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onStoreClick() }
                        .background(EmeraldContainer.copy(alpha = 0.45f))
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Storefront,
                        contentDescription = "المتجر",
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = store.name,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OnEmeraldContainer,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = store.city,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Product Title
                Text(
                    text = product.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Price Section
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "${product.price.roundToInt()} ",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = EmeraldPrimary
                            )
                            Text(
                                text = "ر.س",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary
                            )
                        }

                        if (product.originalPrice != null && product.originalPrice > product.price) {
                            Text(
                                text = "${product.originalPrice.roundToInt()} ر.س",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textDecoration = TextDecoration.LineThrough
                            )
                        }
                    }

                    // Quick view badge
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "عرض",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StoreCard(
    store: Store,
    onStoreClick: () -> Unit,
    onWhatsAppClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onStoreClick() }
            .testTag("store_card_${store.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Store Banner Header with Gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(EmeraldPrimary, EmeraldPrimary.copy(alpha = 0.7f))
                        )
                    )
            ) {
                // Background subtle art
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.1f),
                        radius = size.height,
                        center = Offset(size.width * 0.9f, 0f)
                    )
                }

                // Featured / Verified Badges
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (store.isFeatured) {
                        Surface(
                            color = GoldSecondary,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "⭐ متجر مميز",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    if (store.isVerified) {
                        Surface(
                            color = Color.White.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "موثق",
                                    tint = AccentSuccess,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "موثق",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary
                                )
                            }
                        }
                    }
                }
            }

            // Store Info with overlapping Logo
            Column(
                modifier = Modifier
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Overlapping Store Avatar
                    Box(
                        modifier = Modifier
                            .offset(y = (-24).dp)
                            .size(52.dp)
                            .shadow(4.dp, CircleShape)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Storefront,
                            contentDescription = store.name,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .offset(y = (-8).dp)
                    ) {
                        Text(
                            text = store.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "الموقع",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "${store.city} - ${store.area}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Rating Badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = GoldSecondary.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldSecondary.copy(alpha = 0.4f)),
                        modifier = Modifier.offset(y = (-8).dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "تقييم",
                                tint = GoldSecondary,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${store.rating}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // Description
                Text(
                    text = store.description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Action Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // WhatsApp Direct Contact Button
                    Button(
                        onClick = onWhatsAppClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = WhatsAppGreen,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                            .testTag("whatsapp_btn_${store.id}")
                    ) {
                        Text(
                            text = "واتساب 💬",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Visit Store Button
                    Button(
                        onClick = onStoreClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmeraldPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1.3f)
                            .height(38.dp)
                            .testTag("visit_store_btn_${store.id}")
                    ) {
                        Text(
                            text = "زيارة المتجر 🏬",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryChip(
    category: PlatformCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.surface,
        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
        shadowElevation = if (isSelected) 2.dp else 0.dp,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag("category_chip_${category.key}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = category.iconEmoji,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = category.nameAr,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun StarRatingBar(
    rating: Int,
    onRatingChanged: ((Int) -> Unit)? = null,
    maxStars: Int = 5,
    starSize: Int = 22,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxStars) {
            val isFilled = i <= rating
            Icon(
                imageVector = if (isFilled) Icons.Default.Star else Icons.Outlined.StarBorder,
                contentDescription = "نجمة $i",
                tint = if (isFilled) GoldSecondary else MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .size(starSize.dp)
                    .then(
                        if (onRatingChanged != null) {
                            Modifier.clickable { onRatingChanged(i) }
                        } else Modifier
                    )
            )
        }
    }
}

@Composable
fun InteractiveMapCanvas(
    stores: List<Store>,
    selectedStore: Store? = null,
    onStoreSelected: (Store) -> Unit = {},
    isPickerMode: Boolean = false,
    pickedLocation: Pair<Double, Double> = Pair(24.7136, 46.6753),
    onLocationPicked: (Double, Double) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    var panOffsetX by remember { mutableFloatStateOf(0f) }
    var panOffsetY by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE5E7EB))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    panOffsetX += dragAmount.x
                    panOffsetY += dragAmount.y
                }
            }
            .pointerInput(isPickerMode) {
                if (isPickerMode) {
                    detectTapGestures { offset ->
                        val lat = 24.7136 + (offset.y - 200f) * -0.001
                        val lng = 46.6753 + (offset.x - 200f) * 0.001
                        onLocationPicked(lat, lng)
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasW = size.width
            val canvasH = size.height

            drawRect(color = Color(0xFFF1F5F9))

            val roadColor = Color(0xFFE2E8F0)
            val mainRoadColor = Color(0xFFCBD5E1)

            for (y in 0..canvasH.toInt() step 60) {
                drawLine(
                    color = roadColor,
                    start = Offset(0f, y.toFloat() + (panOffsetY % 60)),
                    end = Offset(canvasW, y.toFloat() + (panOffsetY % 60)),
                    strokeWidth = 3f
                )
            }
            for (x in 0..canvasW.toInt() step 60) {
                drawLine(
                    color = roadColor,
                    start = Offset(x.toFloat() + (panOffsetX % 60), 0f),
                    end = Offset(x.toFloat() + (panOffsetX % 60), canvasH),
                    strokeWidth = 3f
                )
            }

            drawLine(
                color = mainRoadColor,
                start = Offset(0f, 0f + panOffsetY),
                end = Offset(canvasW, canvasH + panOffsetY),
                strokeWidth = 8f
            )
            drawLine(
                color = Color(0xFF93C5FD).copy(alpha = 0.5f),
                start = Offset(canvasW * 0.2f, canvasH),
                end = Offset(canvasW * 0.8f, 0f),
                strokeWidth = 6f
            )

            if (!isPickerMode) {
                stores.forEach { store ->
                    val cx = (canvasW * 0.5f) + ((store.longitude - 46.6753) * 1200f).toFloat() + panOffsetX
                    val cy = (canvasH * 0.5f) - ((store.latitude - 24.7136) * 1200f).toFloat() + panOffsetY

                    val isSelected = selectedStore?.id == store.id

                    drawCircle(
                        color = Color.Black.copy(alpha = 0.2f),
                        radius = 8f,
                        center = Offset(cx, cy + 12f)
                    )

                    drawCircle(
                        color = if (isSelected) GoldSecondary else EmeraldPrimary,
                        radius = if (isSelected) 18f else 14f,
                        center = Offset(cx, cy)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = if (isSelected) 8f else 6f,
                        center = Offset(cx, cy)
                    )
                }
            }
        }

        if (isPickerMode) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "الموقع المحدد",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(40.dp)
                )
            }
            Surface(
                color = Color.Black.copy(alpha = 0.75f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(8.dp)
            ) {
                Text(
                    text = "انقر على الخريطة لتحديد موقع المتجر بدقة",
                    color = Color.White,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White.copy(alpha = 0.9f),
                    shadowElevation = 2.dp
                ) {
                    Text(
                        text = "📍 ${stores.size} متاجر معتمدة",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
    initialCriteria: ProductFilterCriteria,
    stores: List<Store>,
    categories: List<PlatformCategory>,
    onApply: (ProductFilterCriteria) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedCat by remember { mutableStateOf(initialCriteria.categoryKey) }
    var selectedStoreId by remember { mutableStateOf(initialCriteria.storeId) }
    var selectedCity by remember { mutableStateOf(initialCriteria.city ?: "") }
    var minPrice by remember { mutableDoubleStateOf(initialCriteria.minPrice ?: 0.0) }
    var maxPrice by remember { mutableDoubleStateOf(initialCriteria.maxPrice ?: 5000.0) }
    var sortOption by remember { mutableStateOf(initialCriteria.sortOption) }

    val cities = listOf("الرياض", "جدة", "الخبر", "الدمام", "مكة المكرمة", "المدينة المنورة")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "تصفية وترتيب النتائج 🔍",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                TextButton(onClick = {
                    selectedCat = null
                    selectedStoreId = null
                    selectedCity = ""
                    minPrice = 0.0
                    maxPrice = 5000.0
                    sortOption = ProductSortOption.LATEST
                }) {
                    Text("إعادة ضبط", color = MaterialTheme.colorScheme.error)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "ترتيب حسب:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProductSortOption.values().forEach { option ->
                    FilterChip(
                        selected = sortOption == option,
                        onClick = { sortOption = option },
                        label = { Text(option.titleAr) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "التصنيف:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedCat == null,
                    onClick = { selectedCat = null },
                    label = { Text("جميع التصنيفات") }
                )
                categories.forEach { cat ->
                    FilterChip(
                        selected = selectedCat == cat.key,
                        onClick = { selectedCat = if (selectedCat == cat.key) null else cat.key },
                        label = { Text("${cat.iconEmoji} ${cat.nameAr}") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "المدينة:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedCity.isBlank(),
                    onClick = { selectedCity = "" },
                    label = { Text("جميع المدن") }
                )
                cities.forEach { city ->
                    FilterChip(
                        selected = selectedCity == city,
                        onClick = { selectedCity = if (selectedCity == city) "" else city },
                        label = { Text(city) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "المتجر:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedStoreId == null,
                    onClick = { selectedStoreId = null },
                    label = { Text("جميع المتاجر") }
                )
                stores.forEach { st ->
                    FilterChip(
                        selected = selectedStoreId == st.id,
                        onClick = { selectedStoreId = if (selectedStoreId == st.id) null else st.id },
                        label = { Text(st.name) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onApply(
                        initialCriteria.copy(
                            categoryKey = selectedCat,
                            storeId = selectedStoreId,
                            city = selectedCity.ifBlank { null },
                            minPrice = if (minPrice > 0) minPrice else null,
                            maxPrice = if (maxPrice < 5000) maxPrice else null,
                            sortOption = sortOption
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
            ) {
                Text(
                    text = "تطبيق التصفية",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ShareLinkDialog(
    title: String,
    urlPath: String,
    shareText: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val fullUrl = "https://souqna.app$urlPath"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "مشاركة الرابط 🔗",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = fullUrl,
                            fontSize = 12.sp,
                            color = EmeraldPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Souqna Link", fullUrl)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "تم نسخ الرابط إلى الحافظة", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "نسخ",
                                tint = EmeraldPrimary
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    SouqnaUtils.shareContent(
                        context = context,
                        title = title,
                        text = "$shareText\n\nتصفح الآن على سوقنا: $fullUrl"
                    )
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "مشاركة",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("مشاركة الرابط")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إغلاق")
            }
        }
    )
}

@Composable
fun AddReviewDialog(
    storeName: String,
    onSubmit: (rating: Int, comment: String, name: String) -> Unit,
    onDismiss: () -> Unit
) {
    var rating by remember { mutableIntStateOf(5) }
    var comment by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "تقييم $storeName ⭐",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "اختر التقييم بالنجوم:",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                StarRatingBar(
                    rating = rating,
                    onRatingChanged = { rating = it },
                    starSize = 32,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(14.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("اسمك (اختياري)") },
                    placeholder = { Text("مثال: عبدالمحسن") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("اكتب رأيك وتجربتك مع المتجر") },
                    placeholder = { Text("جودة المنتجات، سرعة الرد، التوصيل...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 90.dp),
                    shape = RoundedCornerShape(10.dp),
                    maxLines = 4
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (comment.isNotBlank() || rating > 0) {
                        onSubmit(rating, comment, name)
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
            ) {
                Text("إرسال التقييم")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}
