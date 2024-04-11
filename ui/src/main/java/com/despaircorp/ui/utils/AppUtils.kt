package com.despaircorp.ui.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun getEuroFromDollar(dollars: String): String {
    val dollarsInt = dollars.replace(".", "").toInt()
    val rate = 0.9307
    
    return DecimalFormat("#,###,###")
        .format((dollarsInt * rate))
        .replace(",", "")
}

fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    return context.contentResolver.openInputStream(uri)?.use { inputStream ->
        BitmapFactory.decodeStream(inputStream)
    }
}

fun getDate(): String =
    LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

