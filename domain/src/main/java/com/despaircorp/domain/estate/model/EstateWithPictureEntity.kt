package com.despaircorp.domain.estate.model

import com.despaircorp.domain.picture.model.EstatePictureEntity

data class EstateWithPictureEntity(
    val estateEntity: EstateEntity,
    val pictures: List<EstatePictureEntity>
)