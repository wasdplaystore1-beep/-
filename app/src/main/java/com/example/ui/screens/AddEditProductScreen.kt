package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.Product
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldSecondary
import com.example.ui.viewmodel.SouqnaViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEditProductScreen(
    productId: Long? = null,
    viewModel: SouqnaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val merchantStoreWithProducts by viewModel.myMerchantStore.collectAsStateWithLifecycle()
    val platformCategories by viewModel.platformCategories.collectAsStateWithLifecycle()

    val isEditing = productId != null && productId != 0L
    val existingProduct = merchantStoreWithProducts?.products?.find { it.id == productId }

    var title by remember { mutableStateOf(existingProduct?.title ?: "") }
    var slug by remember { mutableStateOf(existingProduct?.slug ?: "") }
    var priceText by remember { mutableStateOf(existingProduct?.price?.let { if (it % 1 == 0.0) it.toInt().toString() else it.toString() } ?: "") }
    var originalPriceText by remember { mutableStateOf(existingProduct?.originalPrice?.let { if (it % 1 == 0.0) it.toInt().toString() else it.toString() } ?: "") }
    var description by remember { mutableStateOf(existingProduct?.description ?: "") }
    var selectedStoreCatId by remember { mutableStateOf(existingProduct?.storeCategoryId) }
    var selectedPlatformCatKey by remember { mutableStateOf(existingProduct?.platformCategoryKey ?: "electronics") }
    var imageUrl by remember { mutableStateOf(existingProduct?.imageUrl ?: "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&q=80") }
    var isFeatured by remember { mutableStateOf(existingProduct?.isFeatured ?: false) }
    var inStock by remember { mutableStateOf(existingProduct?.inStock ?: true) }

    // Sample preset product photos merchants can pick quickly
    val sampleImagePresets = listOf(
        "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&q=80",
        "https://images.unsplash.com/photo-1546868871-7041f2a55e12?w=600&q=80",
        "https://images.unsplash.com/photo-1588850561407-ed78c282e89b?w=600&q=80",
        "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&q=80",
        "https://images.unsplash.com/photo-1608231387042-66d1773070a5?w=600&q=80",
        "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600&q=80"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditing) "تعديل المنتج" else "إضافة منتج جديد 📦",
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
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val store = merchantStoreWithProducts?.store

            if (store == null) {
                Text("يرجى إنشاء متجر أولاً لتتمكن من إضافة المنتجات.")
                return@Column
            }

            // 1. Basic Info
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "1. تفاصيل المنتج الأساسية",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                            if (slug.isBlank() || slug.startsWith("product-")) {
                                slug = it.trim().replace(" ", "-").lowercase()
                            }
                        },
                        label = { Text("اسم أو عنوان المنتج *") },
                        placeholder = { Text("مثال: ساعة كاسيو إيديفيس جلد كلاسيك") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("product_title_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = slug,
                        onValueChange = { slug = it.replace(" ", "-").lowercase() },
                        label = { Text("رابط المنتج الخاص (Slug)") },
                        placeholder = { Text("casio-edifice-classic") },
                        prefix = {
                            Text(
                                text = "souqna.app/product/",
                                color = EmeraldPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("product_slug_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = priceText,
                            onValueChange = { priceText = it },
                            label = { Text("السعر (ر.س) *") },
                            placeholder = { Text("250") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("product_price_input"),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp)
                        )

                        OutlinedTextField(
                            value = originalPriceText,
                            onValueChange = { originalPriceText = it },
                            label = { Text("السعر قبل الخصم") },
                            placeholder = { Text("320") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("وصف المنتج ومواصفاته *") },
                        placeholder = { Text("اكتب تفاصيل المنتج، اللون، الحجم، الضمان، إلخ...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("product_desc_input"),
                        maxLines = 4,
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }

            // 2. Image Selection & Preview
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "2. صورة المنتج 📸",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Image Preview Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "معاينة الصورة",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    OutlinedTextField(
                        value = imageUrl,
                        onValueChange = { imageUrl = it },
                        label = { Text("رابط صورة المنتج (URL)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Text(
                        text = "أو اختر صورة سريعة من النماذج المتاحة:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Presets
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        sampleImagePresets.forEachIndexed { idx, url ->
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(
                                        width = if (imageUrl == url) 2.dp else 1.dp,
                                        color = if (imageUrl == url) EmeraldPrimary else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { imageUrl = url }
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(context).data(url).crossfade(true).build(),
                                    contentDescription = "نموذج $idx",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }

            // 3. Category & Section Selection
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "3. تصنيف المنتج وأقسام المتجر 🏷️",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Platform Global Category
                    Text(
                        text = "التصنيف العام في منصة سوقنا:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        platformCategories.forEach { cat ->
                            FilterChip(
                                selected = selectedPlatformCatKey == cat.key,
                                onClick = { selectedPlatformCatKey = cat.key },
                                label = { Text("${cat.iconEmoji} ${cat.nameAr}") }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Store Internal Section
                    val storeCategories = merchantStoreWithProducts?.categories ?: emptyList()
                    if (storeCategories.isNotEmpty()) {
                        Text(
                            text = "القسم الداخلي في متجرك:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            storeCategories.forEach { section ->
                                FilterChip(
                                    selected = selectedStoreCatId == section.id,
                                    onClick = { selectedStoreCatId = if (selectedStoreCatId == section.id) null else section.id },
                                    label = { Text(section.name) }
                                )
                            }
                        }
                    }
                }
            }

            // 4. Options (Featured & In Stock)
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "توفر المنتج في المخزون",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "إظهار المنتج كـ 'متوفر' للزبائن",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = inStock,
                            onCheckedChange = { inStock = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = EmeraldPrimary)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "منتج مميز ⭐",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "إبراز المنتج في واجهة المنصة وصفحة المتجر",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = isFeatured,
                            onCheckedChange = { isFeatured = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = GoldSecondary)
                        )
                    }
                }
            }

            // Save Button
            Button(
                onClick = {
                    val price = priceText.toDoubleOrNull()
                    if (title.isBlank()) {
                        Toast.makeText(context, "يرجى كتابة اسم المنتج", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (price == null || price <= 0) {
                        Toast.makeText(context, "يرجى كتابة سعر صحيح للمنتج", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val origPrice = originalPriceText.toDoubleOrNull()

                    viewModel.saveProduct(
                        productId = productId,
                        storeId = store.id,
                        storeCategoryId = selectedStoreCatId,
                        platformCategoryKey = selectedPlatformCatKey,
                        title = title,
                        slug = slug.ifBlank { "product-${System.currentTimeMillis() % 10000}" },
                        description = description.ifBlank { "منتج عالي الجودة متوفر لدى متجرنا." },
                        price = price,
                        originalPrice = origPrice,
                        imageUrl = imageUrl,
                        isFeatured = isFeatured,
                        inStock = inStock,
                        onSuccess = {
                            Toast.makeText(context, if (isEditing) "تم تعديل المنتج بنجاح" else "تمت إضافة المنتج بنجاح 🎉", Toast.LENGTH_SHORT).show()
                            viewModel.navigateBack()
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("save_product_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
            ) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isEditing) "حفظ التعديلات" else "نشر المنتج في المتجر",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
