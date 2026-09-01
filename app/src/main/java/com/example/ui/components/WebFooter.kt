package com.example.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.ui.viewmodel.ScreenNav
import com.example.ui.viewmodel.SouqnaViewModel

@Composable
fun WebFooter(
    viewModel: SouqnaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color(0xFF0F172A) // Sleek Dark Slate Web Footer
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            // Main Brand Section & Value Proposition
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
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
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "سوقنا | Souqna.app",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = EmeraldPrimary.copy(alpha = 0.3f)
                        ) {
                            Text(
                                text = "بوابة الويب الرسمية",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "منصة التجارة الإلكترونية متعددة المتاجر - تواصل وطلب مباشر دون وسيط",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            // Highlights Grid (3 Badges)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FooterFeatureCard(
                    icon = Icons.Default.Storefront,
                    title = "متاجر مستقلة",
                    subtitle = "رابط وهوية خاصة لكل تاجر",
                    modifier = Modifier.weight(1f)
                )
                FooterFeatureCard(
                    icon = Icons.Default.Chat,
                    title = "شراء مباشر",
                    subtitle = "تواصل عبر واتساب فوراً",
                    modifier = Modifier.weight(1f)
                )
                FooterFeatureCard(
                    icon = Icons.Default.Security,
                    title = "أمان وثقة",
                    subtitle = "متاجر موثقة ورسوم 0%",
                    modifier = Modifier.weight(1f)
                )
            }

            HorizontalDivider(color = Color(0xFF1E293B), thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))

            // Multi-column Quick Links
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Column 1: Customer Links
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "تصفح المنصة",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    FooterLinkItem("الرئيسية") { viewModel.navigateToTab(ScreenNav.Home) }
                    FooterLinkItem("البحث الموحد") { viewModel.navigateToTab(ScreenNav.Search) }
                    FooterLinkItem("خريطة المتاجر") { viewModel.navigateToTab(ScreenNav.InteractiveMap) }
                    FooterLinkItem("حسابي") { viewModel.navigateToTab(ScreenNav.Profile) }
                }

                // Column 2: Merchant Links
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "حلول التجار",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    FooterLinkItem("إنشاء متجر جديد") { viewModel.navigateTo(ScreenNav.CreateStore) }
                    FooterLinkItem("لوحة التحكم") { viewModel.navigateTo(ScreenNav.MerchantDashboard) }
                    FooterLinkItem("باقات الاشتراك") { viewModel.navigateTo(ScreenNav.Monetization) }
                    FooterLinkItem("إدارة المنصة") { viewModel.navigateTo(ScreenNav.AdminPanel) }
                }

                // Column 3: Trust & Policies
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "الأمان والدعم",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    FooterLinkItem("الشراء عبر واتساب") {
                        Toast.makeText(context, "الطلب المباشر: تتواصل مع التاجر على رقم واتساب المسجل لديه", Toast.LENGTH_LONG).show()
                    }
                    FooterLinkItem("سياسة الخصوصية") {
                        Toast.makeText(context, "بياناتك مشفرة ومحمية وفق أعلى معايير الخصوصية", Toast.LENGTH_SHORT).show()
                    }
                    FooterLinkItem("شروط الاستخدام") {
                        Toast.makeText(context, "منصة وسيطة لتسهيل العرض والتواصل بين البائع والمشتري", Toast.LENGTH_SHORT).show()
                    }
                    FooterLinkItem("توثيق المتاجر") {
                        Toast.makeText(context, "المتاجر تخضع للمراجعة والتحقق من السجل التجاري", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = Color(0xFF1E293B), thickness = 1.dp)
            Spacer(modifier = Modifier.height(14.dp))

            // Bottom bar with Copyright & Web Domain info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "جميع الحقوق محفوظة © 2025 موقع سوقنا - Souqna.app",
                    color = Color(0xFF64748B),
                    fontSize = 10.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "SSL Secure",
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "SSL 256-bit Encrypted",
                        color = Color(0xFF94A3B8),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun FooterFeatureCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFF1E293B),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = GoldSecondary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = Color(0xFF94A3B8),
                fontSize = 8.sp,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun FooterLinkItem(
    title: String,
    onClick: () -> Unit
) {
    Text(
        text = "• $title",
        color = Color(0xFF94A3B8),
        fontSize = 11.sp,
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = 1.dp)
    )
}
