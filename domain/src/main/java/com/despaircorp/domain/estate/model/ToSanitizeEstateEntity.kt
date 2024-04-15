package com.despaircorp.domain.estate.model

import com.despaircorp.domain.picture.model.EstatePictureEntity

data class ToSanitizeEstateEntity(
    val estateSurface: String,
    val estateDescription: String,
    val estateRoomNumber: String,
    val estateBedroomNumber: String,
    val estateBathRoomNumber: String,
    val estateAddress: String,
    val estateCity: String,
    val estatePrice: String,
    val estateType: String,
    val estatePointOfInterests: List<PointOfInterestEntity>,
    val isEstateSold: Boolean,
    val estateEntryDate: String,
    val estateSoldDate: String,
    val estatePictures: List<EstatePictureEntity>,
)
