package com.example.ui.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object SouqnaUtils {

    fun openWhatsApp(context: Context, phone: String, message: String) {
        try {
            val cleanPhone = phone.replace("+", "").replace(" ", "").replace("-", "")
            val encodedMsg = Uri.encode(message)
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$cleanPhone&text=$encodedMsg")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "تعذر فتح تطبيق واتساب، رقم الهاتف: $phone", Toast.LENGTH_LONG).show()
        }
    }

    fun openPhoneCall(context: Context, phone: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "تعذر إجراء المكالمة: $phone", Toast.LENGTH_SHORT).show()
        }
    }

    fun openMapDirections(context: Context, lat: Double, lng: Double, label: String) {
        try {
            val uri = Uri.parse("geo:$lat,$lng?q=$lat,$lng($label)")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        } catch (e: Exception) {
            val webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$lat,$lng")
            val webIntent = Intent(Intent.ACTION_VIEW, webUri)
            context.startActivity(webIntent)
        }
    }

    fun shareContent(context: Context, title: String, text: String) {
        try {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                putExtra(Intent.EXTRA_TITLE, title)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, "مشاركة عبر منصة سوقنا")
            context.startActivity(shareIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "تم نسخ الرابط", Toast.LENGTH_SHORT).show()
        }
    }
}
