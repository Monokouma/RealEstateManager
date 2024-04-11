package com.despaircorp.domain.picture.model

data class EstatePictureEntity(
    val id: Int,
    val imagePath: String,
    val description: EstatePictureType,
)