package com.example.ui.components

import android.widget.Toast
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MotionPhotosAuto
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.BannerAd
import com.example.data.model.PlatformCategory
import com.example.data.model.Store
import com.example.ui.theme.EmeraldPrimary

data class GradientPreset(
    val name: String,
    val startHex: String,
    val endHex: String,
    val previewColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditBannerAdDialog(
    existingBanner: BannerAd? = null,
    availableStores: List<Store>,
    availableCategories: List<PlatformCategory>,
    onDismiss: () -> Unit,
    onSave: (
        title: String,
        subtitle: String,
        badgeText: String,
        imageUrl: String,
        actionText: String,
        targetType: String,
        targetPayload: String,
        gradientStartHex: String,
        gradientEndHex: String,
        isAnimated: Boolean
    ) -> Unit
) {
    val context = LocalContext.current

    var title by remember { mutableStateOf(existingBanner?.title ?: "") }
    var subtitle by remember { mutableStateOf(existingBanner?.subtitle ?: "") }
    var badgeText by remember { mutableStateOf(existingBanner?.badgeText ?: "🔥 عرض حصري") }
    var imageUrl by remember { mutableStateOf(existingBanner?.imageUrl ?: "") }
    var actionText by remember { mutableStateOf(existingBanner?.actionText ?: "تسوق الآن") }
    var targetType by remember { mutableStateOf(existingBanner?.targetType ?: "STORE") }
    var targetPayload by remember { mutableStateOf(existingBanner?.targetPayload ?: (availableStores.firstOrNull()?.id?.toString() ?: "1")) }
    var selectedGradientStart by remember { mutableStateOf(existingBanner?.gradientStartHex ?: "#059669") }
    var selectedGradientEnd by remember { mutableStateOf(existingBanner?.gradientEndHex ?: "#064E3B") }
    var isAnimated by remember { mutableStateOf(existingBanner?.isAnimated ?: true) }

    var targetTypeExpanded by remember { mutableStateOf(false) }
    var storeDropdownExpanded by remember { mutableStateOf(false) }
    var categoryDropdownExpanded by remember { mutableStateOf(false) }

    val gradientPresets = listOf(
        GradientPreset("زمردي ملكي", "#059669", "#064E3B", Color(0xFF059669)),
        GradientPreset("أزرق بحري", "#1E40AF", "#172554", Color(0xFF1E40AF)),
        GradientPreset("ذهبي كهرماني", "#D97706", "#78350F", Color(0xFFD97706)),
        GradientPreset("ياقوتي أحمر", "#DC2626", "#7F1D1D", Color(0xFFDC2626)),
        GradientPreset("بنفسجي ملكي", "#7C3AED", "#4C1D95", Color(0xFF7C3AED)),
        GradientPreset("فحمي ليلي", "#334155", "#0F172A", Color(0xFF334155))
    )

    val badgePresets = listOf(
        "🔥 عرض حصري",
        "✨ إعلان مميز",
        "🏷️ خصم 50%",
        "📢 إعلان جديد",
        "🎁 مجاناً لفترة محدودة",
        "👑 حصري لسوقنا",
        "⭐ متجر موثق"
    )

    val sampleImages = listOf(
        "ساعات" to "https://images.unsplash.com/photo-1524805444758-089113d48a6d?w=800&auto=format&fit=crop",
        "أزياء" to "https://images.unsplash.com/photo-1445205170230-053b83016050?w=800&auto=format&fit=crop",
        "قهوة" to "https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb?w=800&auto=format&fit=crop",
        "إلكترونيات" to "https://images.unsplash.com/photo-1550009158-9ebf69173e03?w=800&auto=format&fit=crop",
        "عطور" to "https://images.unsplash.com/photo-1594035910387-fea47794261f?w=800&auto=format&fit=crop",
        "تسوق" to "https://images.unsplash.com/photo-1607082348824-0a96f2a4b9da?w=800&auto=format&fit=crop"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(vertical = 20.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = EmeraldPrimary.copy(alpha = 0.15f),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Campaign,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (existingBanner == null) "إضافة إعلان متحرك جديد 📢" else "تعديل الإعلان المتحرك ✏️",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق")
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Live Interactive Preview
                Text(
                    text = "معاينة حية للإعلان المتحرك ✨",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                ) {
                    AnimatedBannerCard(
                        banner = BannerAd(
                            id = 0L,
                            title = title.ifBlank { "عنوان الإعلان الترويجي هنا" },
                            subtitle = subtitle.ifBlank { "اكتب وصفاً جذاباً لعرضك أو متجرك لجذب المتسوقين" },
                            badgeText = badgeText.ifBlank { "🔥 عرض حصري" },
                            imageUrl = imageUrl,
                            actionText = actionText.ifBlank { "تسوق الآن" },
                            targetType = targetType,
                            targetPayload = targetPayload,
                            gradientStartHex = selectedGradientStart,
                            gradientEndHex = selectedGradientEnd,
                            isAnimated = isAnimated
                        ),
                        onClick = {}
                    )
                }

                // 1. Ad Title
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("عنوان الإعلان المتحرك *") },
                    placeholder = { Text("مثال: تخفيضات كبرى على الساعات والإلكترونيات") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ad_title_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                // 2. Ad Subtitle
                OutlinedTextField(
                    value = subtitle,
                    onValueChange = { subtitle = it },
                    label = { Text("الوصف الترويجي أو التفاصيل") },
                    placeholder = { Text("مثال: خصم فوري يصل إلى 40% مع شحن سريع لجميع المدن") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ad_subtitle_input"),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 2
                )

                // 3. Badge Text Presets & Custom Input
                Column {
                    Text(
                        text = "شارة الإعلان الترويجية (Badge):",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        badgePresets.forEach { preset ->
                            val isSelected = badgeText == preset
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { badgeText = preset }
                            ) {
                                Text(
                                    text = preset,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = badgeText,
                        onValueChange = { badgeText = it },
                        label = { Text("نص الشارة المخصص") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }

                // 4. Action Button Text
                OutlinedTextField(
                    value = actionText,
                    onValueChange = { actionText = it },
                    label = { Text("نص زر الإجراء (CTA)") },
                    placeholder = { Text("تسوق الآن / زيارة المتجر / أنشئ متجرك") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                // 5. Target Destination Type
                Column {
                    Text(
                        text = "وجهة النقر عند الضغط على الإعلان:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    ExposedDropdownMenuBox(
                        expanded = targetTypeExpanded,
                        onExpandedChange = { targetTypeExpanded = !targetTypeExpanded }
                    ) {
                        OutlinedTextField(
                            value = when (targetType) {
                                "STORE" -> "🏪 متجر محدد في سوقنا"
                                "CATEGORY" -> "📁 تصنيف من المنصة"
                                "SEARCH" -> "🔍 كلمة بحث في المنتجات"
                                "SPECIAL_OFFER" -> "🎁 صفحة عروض خاصة / إنشاء متجر"
                                else -> "🌐 رابط ويب مخصص"
                            },
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = targetTypeExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = targetTypeExpanded,
                            onDismissRequest = { targetTypeExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("🏪 متجر محدد في سوقنا") },
                                onClick = {
                                    targetType = "STORE"
                                    targetPayload = availableStores.firstOrNull()?.id?.toString() ?: "1"
                                    targetTypeExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("📁 تصنيف من المنصة") },
                                onClick = {
                                    targetType = "CATEGORY"
                                    targetPayload = availableCategories.firstOrNull()?.key ?: "watches_accessories"
                                    targetTypeExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("🔍 كلمة بحث في المنتجات") },
                                onClick = {
                                    targetType = "SEARCH"
                                    targetPayload = "كاسيو"
                                    targetTypeExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("🎁 صفحة عروض خاصة / إنشاء متجر") },
                                onClick = {
                                    targetType = "SPECIAL_OFFER"
                                    targetPayload = "create_store"
                                    targetTypeExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("🌐 رابط ويب خارجي") },
                                onClick = {
                                    targetType = "EXTERNAL"
                                    targetPayload = "https://souqna.app"
                                    targetTypeExpanded = false
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Secondary selection based on targetType
                    when (targetType) {
                        "STORE" -> {
                            ExposedDropdownMenuBox(
                                expanded = storeDropdownExpanded,
                                onExpandedChange = { storeDropdownExpanded = !storeDropdownExpanded }
                            ) {
                                val selectedStore = availableStores.find { it.id.toString() == targetPayload }
                                OutlinedTextField(
                                    value = selectedStore?.let { "${it.name} (${it.city})" } ?: "اختر المتجر",
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = storeDropdownExpanded) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                ExposedDropdownMenu(
                                    expanded = storeDropdownExpanded,
                                    onDismissRequest = { storeDropdownExpanded = false }
                                ) {
                                    availableStores.forEach { store ->
                                        DropdownMenuItem(
                                            text = { Text("${store.name} (${store.city})") },
                                            onClick = {
                                                targetPayload = store.id.toString()
                                                storeDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        "CATEGORY" -> {
                            ExposedDropdownMenuBox(
                                expanded = categoryDropdownExpanded,
                                onExpandedChange = { categoryDropdownExpanded = !categoryDropdownExpanded }
                            ) {
                                val selectedCat = availableCategories.find { it.key == targetPayload }
                                OutlinedTextField(
                                    value = selectedCat?.let { "${it.iconEmoji} ${it.nameAr}" } ?: "اختر التصنيف",
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                ExposedDropdownMenu(
                                    expanded = categoryDropdownExpanded,
                                    onDismissRequest = { categoryDropdownExpanded = false }
                                ) {
                                    availableCategories.forEach { cat ->
                                        DropdownMenuItem(
                                            text = { Text("${cat.iconEmoji} ${cat.nameAr}") },
                                            onClick = {
                                                targetPayload = cat.key
                                                categoryDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        "SEARCH" -> {
                            OutlinedTextField(
                                value = targetPayload,
                                onValueChange = { targetPayload = it },
                                label = { Text("كلمة البحث المستهدفة") },
                                placeholder = { Text("مثال: ساعات، ملابس، كاسيو") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                        else -> {
                            OutlinedTextField(
                                value = targetPayload,
                                onValueChange = { targetPayload = it },
                                label = { Text("الرابط أو الوجهة") },
                                placeholder = { Text("https://souqna.app/store/...") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }

                // 6. Color Gradient Presets
                Column {
                    Text(
                        text = "اختر تدرج لون الإعلان:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        gradientPresets.forEach { preset ->
                            val isSelected = selectedGradientStart == preset.startHex
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = preset.previewColor,
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(3.dp, Color.Black) else null,
                                modifier = Modifier
                                    .size(width = 90.dp, height = 44.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        selectedGradientStart = preset.startHex
                                        selectedGradientEnd = preset.endHex
                                    }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(3.dp))
                                        }
                                        Text(
                                            text = preset.name,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 7. Image Presets & Input
                Column {
                    Text(
                        text = "صورة الخلفية (اختياري):",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        sampleImages.forEach { (label, url) ->
                            val isSelected = imageUrl == url
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) EmeraldPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { imageUrl = url }
                            ) {
                                Text(
                                    text = "🖼️ $label",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = imageUrl,
                        onValueChange = { imageUrl = it },
                        label = { Text("رابط الصورة (URL)") },
                        placeholder = { Text("https://...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }

                // 8. Animation Switch
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MotionPhotosAuto,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "تفعيل حركة الإعلان التفاعلية ⚡",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "نبض الشارة والتمرير التلقائي لجذب الزبائن",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Switch(
                            checked = isAnimated,
                            onCheckedChange = { isAnimated = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = EmeraldPrimary)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isBlank()) {
                        Toast.makeText(context, "يرجى كتابة عنوان الإعلان أولاً", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    onSave(
                        title,
                        subtitle,
                        badgeText,
                        imageUrl,
                        actionText,
                        targetType,
                        targetPayload,
                        selectedGradientStart,
                        selectedGradientEnd,
                        isAnimated
                    )
                    Toast.makeText(context, "تم حفظ ونشر الإعلان المتحرك بنجاح 🚀", Toast.LENGTH_SHORT).show()
                    onDismiss()
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                modifier = Modifier.testTag("save_ad_button")
            ) {
                Text(
                    text = if (existingBanner == null) "نشر الإعلان المتحرك 🚀" else "حفظ التعديلات ✅",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("إلغاء")
            }
        }
    )
}
