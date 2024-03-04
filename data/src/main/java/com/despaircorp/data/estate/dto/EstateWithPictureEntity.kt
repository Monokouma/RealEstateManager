package com.despaircorp.data.estate.dto

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import com.despaircorp.data.picture.dto.PictureDto

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = PictureDto::class,
            parentColumns = ["id"],
            childColumns = ["estateId"]
        )
    ]
)

data class EstateWithPictureEntity(
    @Embedded val estateDto: EstateDto,
    val pictureList: List<PictureDto>
)