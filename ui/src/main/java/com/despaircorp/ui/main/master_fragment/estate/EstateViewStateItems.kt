package com.despaircorp.ui.main.master_fragment.estate

import android.graphics.Bitmap

data class EstateViewStateItems(
    val id: Int,
    val picture: Bitmap,
    val city: String,
    val type: String,
    val price: String,
    val isSelected: Boolean,
    
    )