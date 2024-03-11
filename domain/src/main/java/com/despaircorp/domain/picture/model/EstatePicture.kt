package com.despaircorp.domain.picture.model

import android.graphics.Bitmap

data class EstatePicture(
    val id: Int,
    val bitmapImage: Bitmap,
    val description: EstatePictureType,
)