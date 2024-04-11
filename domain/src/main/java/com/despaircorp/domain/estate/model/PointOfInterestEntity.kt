package com.despaircorp.domain.estate.model

data class PointOfInterestEntity(
    val isSelected: Boolean,
    val pointOfInterestEnum: PointOfInterestEnum,
    val id: Int
)