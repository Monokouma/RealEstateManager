package com.despaircorp.data.estate.dto

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import com.despaircorp.data.picture.dto.PictureDto

@Entity
data class EstateWithPictureDto(
    @Embedded val estateDto: EstateDto,
    @Relation(
        parentColumn = "id",
        entityColumn = "estateId"
    )
    
    val pictureList: List<PictureDto>
)