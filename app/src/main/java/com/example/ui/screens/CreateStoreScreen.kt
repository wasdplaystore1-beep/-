package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.InteractiveMapCanvas
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.OnEmeraldContainer
import com.example.ui.viewmodel.ScreenNav
import com.example.ui.viewmodel.SouqnaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateStoreScreen(
    viewModel: SouqnaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var storeName by remember { mutableStateOf("") }
    var storeSlug by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var ownerName by remember { mutableStateOf("محمد التاجر") }
    var phone by remember { mutableStateOf("+966 50 123 4567") }
    var whatsapp by remember { mutableStateOf("+966501234567") }
    var instagram by remember { mutableStateOf("@souqna_store") }
    var city by remember { mutableStateOf("الرياض") }
    var area by remember { mutableStateOf("العليا") }
    var address by remember { mutableStateOf("طريق الملك فهد") }
    var latitude by remember { mutableDoubleStateOf(24.7136) }
    var longitude by remember { mutableDoubleStateOf(46.6753) }

    val cities = listOf("الرياض", "جدة", "الخبر", "الدمام", "مكة المكرمة", "المدينة المنورة", "أبها")
    var cityExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "إنشاء متجر جديد في سوقنا 🏪",
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
            // Header Info Banner
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
                        imageVector = Icons.Default.AddBusiness,
                        contentDescription = null,
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "ابدأ تجارتك الرقمية مجاناً",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnEmeraldContainer
                        )
                        Text(
                            text = "سجل متجرك الآن، أضف منتجاتك، وتواصل مع الزبائن مباشرة عبر واتساب ورابط متجرك المخصص.",
                            fontSize = 11.sp,
                            color = OnEmeraldContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // 1. Basic Store Info Card
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "1. بيانات المتجر الأساسية",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = storeName,
                        onValueChange = {
                            storeName = it
                            if (storeSlug.isBlank() || storeSlug.startsWith("store-")) {
                                storeSlug = it.trim().replace(" ", "-").lowercase()
                            }
                        },
                        label = { Text("اسم المتجر *") },
                        placeholder = { Text("مثال: متجر الساعات الفاخرة") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("store_name_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = storeSlug,
                        onValueChange = { storeSlug = it.replace(" ", "-").lowercase() },
                        label = { Text("رابط المتجر الخاص (Slug) *") },
                        placeholder = { Text("luxury-watches") },
                        prefix = {
                            Text(
                                text = "souqna.app/store/",
                                color = EmeraldPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("store_slug_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("وصف المتجر ومجال النشاط *") },
                        placeholder = { Text("اكتب نبذة عن متجرك، ماذا تبيع، وجودة المنتجات...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("store_desc_input"),
                        maxLines = 3,
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = ownerName,
                        onValueChange = { ownerName = it },
                        label = { Text("اسم المالك أو المسؤول") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }

            // 2. Contact & WhatsApp (Critical for direct communication)
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "2. وسائل التواصل والمحادثة المباشرة 💬",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = whatsapp,
                        onValueChange = { whatsapp = it },
                        label = { Text("رقم الواتساب لاستقبال الطلبات *") },
                        placeholder = { Text("+966501234567") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("store_whatsapp_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("رقم الهاتف للاتصال") },
                        placeholder = { Text("+966 50 123 4567") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = instagram,
                        onValueChange = { instagram = it },
                        label = { Text("حساب انستغرام أو تويتر") },
                        placeholder = { Text("@souqna_store") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }

            // 3. Location & Interactive Map Picking
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "3. موقع المتجر والمدينة 📍",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // City Dropdown
                    ExposedDropdownMenuBox(
                        expanded = cityExpanded,
                        onExpandedChange = { cityExpanded = !cityExpanded }
                    ) {
                        OutlinedTextField(
                            value = city,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("المدينة *") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cityExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = cityExpanded,
                            onDismissRequest = { cityExpanded = false }
                        ) {
                            cities.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item) },
                                    onClick = {
                                        city = item
                                        cityExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = area,
                            onValueChange = { area = it },
                            label = { Text("الحي") },
                            placeholder = { Text("العليا") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp)
                        )

                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text("العنوان / الشارع") },
                            placeholder = { Text("طريق الملك فهد") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    Text(
                        text = "حدد موقع متجرك على الخريطة التفاعلية:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    // Location Picker Map
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        InteractiveMapCanvas(
                            stores = emptyList(),
                            isPickerMode = true,
                            pickedLocation = Pair(latitude, longitude),
                            onLocationPicked = { lat, lng ->
                                latitude = lat
                                longitude = lng
                                Toast.makeText(context, "تم تحديد الإحداثيات: (${String.format("%.4f", lat)}, ${String.format("%.4f", lng)})", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            // Submit Button
            Button(
                onClick = {
                    if (storeName.isBlank()) {
                        Toast.makeText(context, "يرجى كتابة اسم المتجر", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (whatsapp.isBlank()) {
                        Toast.makeText(context, "يرجى كتابة رقم الواتساب", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    viewModel.createMerchantStore(
                        name = storeName,
                        slug = storeSlug.ifBlank { "store-${System.currentTimeMillis() % 10000}" },
                        description = description.ifBlank { "متجر معتمد على منصة سوقنا يقدم أفضل المنتجات بأسعار منافسة." },
                        city = city,
                        area = area.ifBlank { "الوسطى" },
                        address = address.ifBlank { "الشارع العام" },
                        latitude = latitude,
                        longitude = longitude,
                        phone = phone.ifBlank { whatsapp },
                        whatsapp = whatsapp,
                        instagram = instagram,
                        onSuccess = {
                            Toast.makeText(context, "تم إنشاء متجرك بنجاح! أهلاً بك في سوقنا 🎉", Toast.LENGTH_LONG).show()
                            viewModel.navigateTo(ScreenNav.MerchantDashboard)
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("submit_create_store_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
            ) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "إنشاء المتجر وتفعيل لوحة التحكم 🚀",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
