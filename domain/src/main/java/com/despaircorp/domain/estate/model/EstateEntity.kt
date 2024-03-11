package com.despaircorp.domain.estate.model

import com.google.android.gms.maps.model.LatLng
import java.time.LocalDate


data class EstateEntity(
    val id: Int,
    val description: String,
    val surface: String,
    val roomNumber: Int,
    val bathroomNumber: Int,
    val numberOfBedrooms: Int,
    val location: LatLng,
    val estateType: String,
    val price: String,
    val pointOfInterest: List<String>,
    val sellingDate: LocalDate?,
    val entryDate: LocalDate,
    val status: EstateStatus,
)