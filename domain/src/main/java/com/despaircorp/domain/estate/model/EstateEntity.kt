package com.despaircorp.domain.estate.model

import com.despaircorp.domain.picture.model.EstatePicture
import com.google.android.gms.maps.model.LatLng


data class EstateEntity(
    val id: Int,
    val picturesUrl: List<EstatePicture>,
    val description: String,
    val surface: String,
    val roomNumber: Int,
    val bathroomNumber: Int,
    val numberOfBedrooms: Int,
    val location: LatLng,
    val estateType: String,
    val price: String,
)