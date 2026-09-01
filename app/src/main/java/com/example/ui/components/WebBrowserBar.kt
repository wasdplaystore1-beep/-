package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Http
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldPrimaryDark
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.OnEmeraldContainer
import com.example.ui.viewmodel.ScreenNav
import com.example.ui.viewmodel.SouqnaViewModel

@Composable
fun WebBrowserBar(
    viewModel: SouqnaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val isWebMode by viewModel.isWebMode.collectAsStateWithLifecycle()

    val currentUrl = viewModel.getWebUrlForScreen(currentScreen)
    var urlText by remember(currentUrl) { mutableStateOf(currentUrl) }
    var showUrlSuggestions by remember { mutableStateOf(false) }
    var showSecurityDialog by remember { mutableStateOf(false) }

    LaunchedEffect(currentUrl) {
        urlText = currentUrl
        viewModel.updateWebUrlInput(currentUrl)
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color(0xFF1E293B), // Dark sleek browser chrome header
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            // Top mini tab indicator with Browser Tab Look
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Active Web Tab
                Surface(
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                    color = Color(0xFF334155),
                    modifier = Modifier.padding(bottom = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF10B981))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "سوقنا | Souqna.app",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "🌐 موقع إلكتروني",
                            color = GoldSecondary,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Mode switcher / Quick indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isWebMode) EmeraldPrimary.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.1f),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isWebMode) EmeraldPrimary else Color.White.copy(alpha = 0.2f)
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                viewModel.toggleWebMode()
                                Toast.makeText(
                                    context,
                                    if (!isWebMode) "تم تفعيل وضع الموقع الإلكتروني الكامل (Web Mode)" else "تم التبديل لوضع التطبيق",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isWebMode) Icons.Default.Web else Icons.Default.Language,
                                contentDescription = "وضع الويب",
                                tint = if (isWebMode) EmeraldPrimary else Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isWebMode) "وضع الموقع: مفعل" else "عرض الويب",
                                color = if (isWebMode) EmeraldPrimary else Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Browser URL & Control Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Back Button
                IconButton(
                    onClick = { viewModel.navigateBack() },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "للخلف",
                        tint = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Refresh Button
                IconButton(
                    onClick = {
                        Toast.makeText(context, "جاري تحديث الصفحة...", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "إعادة تحميل",
                        tint = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Interactive Web Address Bar
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp),
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFF0F172A),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // SSL Lock Badge (Clickable for security info)
                        IconButton(
                            onClick = { showSecurityDialog = true },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "اتصال آمن وموثق",
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        // Domain & URL Input Field
                        OutlinedTextField(
                            value = urlText,
                            onValueChange = {
                                urlText = it
                                showUrlSuggestions = true
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                            keyboardActions = KeyboardActions(
                                onGo = {
                                    viewModel.navigateToWebUrl(urlText)
                                    showUrlSuggestions = false
                                }
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color(0xFFE2E8F0),
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                cursorColor = EmeraldPrimary
                            ),
                            textStyle = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("web_url_input")
                        )

                        // Go Button
                        if (urlText != currentUrl) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = EmeraldPrimary,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        viewModel.navigateToWebUrl(urlText)
                                        showUrlSuggestions = false
                                    }
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "انتقل",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Copy Link Button
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Souqna URL", currentUrl)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "تم نسخ رابط الموقع: $currentUrl", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(26.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "نسخ الرابط",
                                tint = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                // Direct Home Web Button
                IconButton(
                    onClick = { viewModel.navigateToTab(ScreenNav.Home) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "الرئيسية",
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Quick Web Navigation Suggestion Chips
            AnimatedVisibility(
                visible = showUrlSuggestions || isWebMode,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "روابط الموقع:",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )

                    WebQuickLinkChip(label = "🏠 الرئيسية", path = "/") {
                        viewModel.navigateToWebUrl("/")
                        showUrlSuggestions = false
                    }
                    WebQuickLinkChip(label = "🔍 البحث الموحد", path = "/search") {
                        viewModel.navigateToWebUrl("/search")
                        showUrlSuggestions = false
                    }
                    WebQuickLinkChip(label = "🗺️ خريطة المتاجر", path = "/map") {
                        viewModel.navigateToWebUrl("/map")
                        showUrlSuggestions = false
                    }
                    WebQuickLinkChip(label = "➕ إنشاء متجر", path = "/merchant/create-store") {
                        viewModel.navigateToWebUrl("/merchant/create-store")
                        showUrlSuggestions = false
                    }
                    WebQuickLinkChip(label = "🏪 لوحة التاجر", path = "/merchant/dashboard") {
                        viewModel.navigateToWebUrl("/merchant/dashboard")
                        showUrlSuggestions = false
                    }
                    WebQuickLinkChip(label = "💎 باقات الاشتراك", path = "/pricing/plans") {
                        viewModel.navigateToWebUrl("/pricing/plans")
                        showUrlSuggestions = false
                    }
                    WebQuickLinkChip(label = "⚙️ لوحة الإدارة", path = "/admin/portal") {
                        viewModel.navigateToWebUrl("/admin/portal")
                        showUrlSuggestions = false
                    }
                }
            }
        }
    }

    // SSL Security Dialog
    if (showSecurityDialog) {
        AlertDialog(
            onDismissRequest = { showSecurityDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color(0xFF10B981),
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = "اتصال الموقع آمن وموثق (SSL)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "أنت تتصفح منصة سوقنا عبر نطاق الويب المعتمد:",
                        fontSize = 13.sp
                    )
                    Surface(
                        color = Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = currentUrl,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Monospace,
                            color = EmeraldPrimaryDark,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Text(
                        text = "• التشفير: 256-bit TLS/SSL\n• المعاملات: تواصل مباشر مع أصحاب المتاجر عبر واتساب بدون وسيط\n• المتاجر: معتمدة وموثقة برقم الترخيص والسجل التجاري",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showSecurityDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("تم الفهم")
                }
            }
        )
    }
}

@Composable
private fun WebQuickLinkChip(
    label: String,
    path: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF334155),
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
