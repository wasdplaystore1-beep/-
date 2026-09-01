package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldPrimaryDark
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.OnEmeraldContainer
import com.example.ui.viewmodel.SouqnaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonetizationScreen(
    viewModel: SouqnaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "باقات الاشتراك والترقية للمتاجر ✨",
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
            // Header Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.verticalGradient(listOf(EmeraldPrimaryDark, EmeraldPrimary)))
                    .padding(20.dp)
            ) {
                Column {
                    Surface(
                        color = GoldSecondary,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "نموذج العمل والنمو 🚀",
                            color = Color.Black,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "طوّر مبيعات متجرك مع باقات سوقنا",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "احصل على ميزات حصرية وظهور مميز لمنتجاتك أمام آلاف الزوار يومياً",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            // 1. Free Starter Tier
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "الباقة المجانية (الانطلاق) 🎁",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "مثالية للتجربة وبدء النشاط التجاري",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "0 ر.س",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = EmeraldPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    MonetizationFeatureRow("إنشاء متجر برابط مخصص (/store/slug)")
                    MonetizationFeatureRow("إضافة حتى 20 منتج مع الصور والأسعار")
                    MonetizationFeatureRow("تواصل مباشر وفوري مع الزبائن عبر واتساب")
                    MonetizationFeatureRow("ظهور المتجر والمنتجات في نتائج البحث الموحد")

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { Toast.makeText(context, "أنت بالفعل على الباقة المجانية الحالية", Toast.LENGTH_SHORT).show() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurfaceVariant),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("الباقة الحالية النشطة", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // 2. Pro Merchant Tier
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(2.dp, EmeraldPrimary),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "باقة التاجر المحترف 💼",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = EmeraldPrimary,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "الأكثر طلباً",
                                        color = Color.White,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "للمتاجر المتنامية وأصحاب الأعمال",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "99 ر.س",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = EmeraldPrimary
                            )
                            Text(text = "/ شهرياً", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    MonetizationFeatureRow("عدد غير محدود من المنتجات والأقسام")
                    MonetizationFeatureRow("شارة المتجر الموثق والمعتمد (Verified)")
                    MonetizationFeatureRow("أولوية في نتائج البحث والتصفية")
                    MonetizationFeatureRow("إمكانية تصدير إحصائيات المبيعات والمشاهدات")
                    MonetizationFeatureRow("دعم فني مخصص عبر واتساب")

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { Toast.makeText(context, "سيتم تفعيل الدفع الإلكتروني في المرحلة القادمة", Toast.LENGTH_LONG).show() },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("الترقية إلى باقة المحترف", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // 3. VIP Featured Store Tier
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldSecondary),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "باقة المتاجر المميزة VIP 👑",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "أقصى انتشار وظهور في المنصة",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "199 ر.س",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = GoldSecondary
                            )
                            Text(text = "/ شهرياً", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    MonetizationFeatureRow("جميع مميزات باقة المحترف")
                    MonetizationFeatureRow("ظهور دائم في قسم 'المتاجر المميزة' بالواجهة الرئيسية")
                    MonetizationFeatureRow("تمييز 10 منتجات كـ 'منتجات مميزة' في الصفحة الأولى")
                    MonetizationFeatureRow("إعلانات ممولة في أعلى نتائج البحث")
                    MonetizationFeatureRow("مدير حساب مخصص لمساعدة المتجر على النمو")

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { Toast.makeText(context, "سيتم تفعيل الدفع الإلكتروني في المرحلة القادمة", Toast.LENGTH_LONG).show() },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldSecondary, contentColor = Color.Black),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("الترقية إلى باقة VIP ⭐", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun MonetizationFeatureRow(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = EmeraldPrimary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
