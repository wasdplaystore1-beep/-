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
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.ProductWithStore
import com.example.ui.components.ProductGridCard
import com.example.ui.components.ShareLinkDialog
import com.example.ui.theme.AccentSuccess
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.OnEmeraldContainer
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.util.SouqnaUtils
import com.example.ui.viewmodel.SouqnaViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    viewModel: SouqnaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val productWithStore by viewModel.selectedProductDetails.collectAsStateWithLifecycle()
    val allProductsWithStore by viewModel.allProductsWithStore.collectAsStateWithLifecycle()

    var showShareDialog by remember { mutableStateOf(false) }

    if (productWithStore == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("جاري تحميل بيانات المنتج...")
        }
        return
    }

    val currentProduct = productWithStore!!.product
    val currentStore = productWithStore!!.store
    val relatedProducts = allProductsWithStore.filter {
        it.product.id != currentProduct.id && (it.product.storeId == currentStore.id || it.product.platformCategoryKey == currentProduct.platformCategoryKey)
    }.take(6)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = currentProduct.title,
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
                        Icon(Icons.Default.Share, contentDescription = "مشاركة رابط المنتج")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            // Direct Contact and Buy Bar (WhatsApp Direct Order)
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 10.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Price Preview
                    Column {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "${currentProduct.price.roundToInt()} ",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = EmeraldPrimary
                            )
                            Text(
                                text = "ر.س",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary
                            )
                        }
                        if (currentProduct.originalPrice != null && currentProduct.originalPrice > currentProduct.price) {
                            Text(
                                text = "${currentProduct.originalPrice.roundToInt()} ر.س",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textDecoration = TextDecoration.LineThrough
                            )
                        }
                    }

                    // Direct WhatsApp Order Button (Instant contact with seller)
                    Button(
                        onClick = {
                            val directMsg = "مرحباً ${currentStore.name}، أود شراء أو الاستفسار عن منتج \"${currentProduct.title}\" المعروض بسعر ${currentProduct.price.roundToInt()} ر.س على منصة سوقنا.\nالرابط: https://souqna.app/product/${currentProduct.slug}"
                            SouqnaUtils.openWhatsApp(
                                context = context,
                                phone = currentStore.whatsapp,
                                message = directMsg
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = WhatsAppGreen,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("whatsapp_order_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = "طلب عبر واتساب",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "طلب عبر واتساب 💬",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Call Seller Button
                    IconButton(
                        onClick = { SouqnaUtils.openPhoneCall(context, currentStore.phone) },
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(EmeraldContainer)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "اتصال هاتفي",
                            tint = EmeraldPrimary
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 30.dp)
        ) {
            // 1. High Resolution Product Image
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.15f)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(currentProduct.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = currentProduct.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Top Badges
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (currentProduct.originalPrice != null && currentProduct.originalPrice > currentProduct.price) {
                            val discountPercent = (((currentProduct.originalPrice - currentProduct.price) / currentProduct.originalPrice) * 100).roundToInt()
                            Surface(
                                color = MaterialTheme.colorScheme.error,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "خصم $discountPercent%",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.size(1.dp))
                        }

                        if (currentProduct.isFeatured) {
                            Surface(
                                color = GoldSecondary,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "⭐ منتج مميز",
                                    color = Color.Black,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 2. Main Product Info & Details
            item {
                Card(
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        // Title
                        Text(
                            text = currentProduct.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 26.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Custom slug identifier
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = EmeraldContainer.copy(alpha = 0.5f)
                        ) {
                            Text(
                                text = "souqna.app/product/${currentProduct.slug}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = EmeraldPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Price and Views Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "${currentProduct.price.roundToInt()} ",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = EmeraldPrimary
                                )
                                Text(
                                    text = "ريال سعودي",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary
                                )
                                if (currentProduct.originalPrice != null && currentProduct.originalPrice > currentProduct.price) {
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "${currentProduct.originalPrice.roundToInt()} ر.س",
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textDecoration = TextDecoration.LineThrough
                                    )
                                }
                            }

                            // Views and Stock Badge
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    color = if (currentProduct.inStock) EmeraldContainer else MaterialTheme.colorScheme.errorContainer,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = if (currentProduct.inStock) "متوفر بالمخزون ✓" else "نفذت الكمية",
                                        color = if (currentProduct.inStock) OnEmeraldContainer else MaterialTheme.colorScheme.onErrorContainer,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Visibility,
                                        contentDescription = "المشاهدات",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "${currentProduct.viewsCount}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Product Description
                        Text(
                            text = "وصف ومواصفات المنتج:",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = currentProduct.description,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Direct Communication Notice (Phase 1 rule)
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = EmeraldContainer.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = null,
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "الشراء والتواصل مباشر مع التاجر عبر واتساب دون عمولات أو وسطاء في المرحلة الحالية.",
                                    fontSize = 12.sp,
                                    color = OnEmeraldContainer,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            // 3. Merchant & Store Profile Card
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { viewModel.selectStore(currentStore.id) }
                        .testTag("merchant_info_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "معلومات المتجر والبائع 🏬",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(EmeraldContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Storefront,
                                        contentDescription = currentStore.name,
                                        tint = EmeraldPrimary,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = currentStore.name,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        if (currentStore.isVerified) {
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(
                                                imageVector = Icons.Default.Verified,
                                                contentDescription = "موثق",
                                                tint = AccentSuccess,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        text = "${currentStore.city} - ${currentStore.area}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "المالك: ${currentStore.ownerName}",
                                        fontSize = 11.sp,
                                        color = EmeraldPrimary
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                contentDescription = "زيارة المتجر",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // 4. Related Products from same store / category
            if (relatedProducts.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "منتجات أخرى قد تعجبك 🛍️",
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
                        items(relatedProducts) { item ->
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
        }
    }

    // Share Dialog
    if (showShareDialog) {
        ShareLinkDialog(
            title = currentProduct.title,
            urlPath = "/product/${currentProduct.slug}",
            shareText = "شاهد منتج \"${currentProduct.title}\" بسعر ${currentProduct.price.roundToInt()} ر.س على منصة سوقنا:",
            onDismiss = { showShareDialog = false }
        )
    }
}
