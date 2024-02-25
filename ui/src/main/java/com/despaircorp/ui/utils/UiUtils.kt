package com.despaircorp.ui.utils

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