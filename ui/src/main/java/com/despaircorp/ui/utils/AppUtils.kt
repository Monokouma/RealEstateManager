package com.despaircorp.ui.utils

import com.despaircorp.ui.R

fun randomProfilePicturinator(): Int {
    
    return when ((0..5).random()) {
        0 -> R.drawable.happy
        1 -> R.drawable.bartender
        2 -> R.drawable.ice_skating
        3 -> R.drawable.old_man
        4 -> R.drawable.young_man
        5 -> R.drawable.gamer
        else -> R.drawable.happy
    }
}