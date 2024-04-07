package com.despaircorp.ui.utils

import android.content.Context
import android.content.res.Configuration
import com.despaircorp.shared.R


object ProfilePictureRandomizator {
    
    fun provideRandomProfilePicture(): Int {
        return when ((0..8).random()) {
            0 -> R.drawable.bard
            1 -> R.drawable.cassio
            2 -> R.drawable.darius
            4 -> R.drawable.jhin
            5 -> R.drawable.kindred
            6 -> R.drawable.lb
            7 -> R.drawable.lucian
            8 -> R.drawable.nilah
            9 -> R.drawable.syndra
            else -> R.drawable.lb
        }
    }
}

fun isNightMode(context: Context): Boolean {
    val nightModeFlags = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return when (nightModeFlags) {
        Configuration.UI_MODE_NIGHT_YES -> true
        Configuration.UI_MODE_NIGHT_NO -> false
        Configuration.UI_MODE_NIGHT_UNDEFINED -> false
        else -> false
    }
}


