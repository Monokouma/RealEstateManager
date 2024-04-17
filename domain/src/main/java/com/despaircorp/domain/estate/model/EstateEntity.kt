package com.despaircorp.domain.estate.model

import com.google.android.gms.maps.model.LatLng
import java.time.LocalDate


data class EstateEntity(
    val id: Int,
    val description: String,
    val surface: Int,
    val roomNumber: Int,
    val bathroomNumber: Int,
    val numberOfBedrooms: Int,
    val location: LatLng?,
    val estateType: String,
    val price: String,
    val pointOfInterest: List<PointOfInterestEntity>,
    val sellingDate: LocalDate?,
    val entryDate: LocalDate,
    val status: EstateStatus,
    val address: String,
    val city: String,
    val agentInChargeId: Int
)