package com.despaircorp.ui.main.activity.entity

import android.os.Bundle
import androidx.compose.runtime.saveable.Saver
import androidx.core.os.bundleOf
import com.despaircorp.domain.estate.model.EstateWithPictureEntity

class EstateWithPictureViewEntity(val id: Int, val estate: EstateWithPictureEntity?) {
    companion object {
        val Saver: Saver<EstateWithPictureViewEntity?, Bundle> = Saver(
            save = { bundleOf("id" to it?.id, "estate" to it?.estate?.estateEntity?.id) },
            restore = { EstateWithPictureViewEntity(it.getInt("id"), it.getParcelable("estate")) }
        )
    }
}