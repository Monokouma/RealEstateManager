package com.despaircorp.data.picture.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.despaircorp.domain.picture.model.EstatePictureType

@Entity(tableName = "picture_table")
class PictureDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imagePath: String,
    val description: EstatePictureType,
    val estateId: Int
)