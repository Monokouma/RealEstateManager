package com.despaircorp.ui.main.estate_form.point_of_interest

import com.despaircorp.domain.estate.model.PointOfInterestEnum

data class PointOfInterestViewStateItems(
    val pointOfInterestEnum: PointOfInterestEnum,
    val id: Int,
    val isSelected: Boolean
)