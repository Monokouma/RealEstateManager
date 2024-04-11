package com.despaircorp.domain.picture

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import javax.inject.Inject

class TransformUriToBitmapUseCase @Inject constructor(
    private val application: Application
) {
    suspend fun invoke(uri: Uri): Bitmap? =
        application.contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }
}