package com.despaircorp.domain.estate.model

import com.despaircorp.domain.picture.model.EstatePicture

data class EstateWithPictureEntity(
    val estateEntity: EstateEntity,
    val pictures: List<EstatePicture>
)