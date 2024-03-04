package com.despaircorp.data.picture.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "picture_table")
class PictureDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val image: String,
    val description: String,
    val estateId: Int
)