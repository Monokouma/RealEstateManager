package com.despaircorp.ui.main.main_activity

import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity
import com.despaircorp.ui.main.estate_form.estate_type.EstateTypeViewStateItems
import com.despaircorp.ui.main.estate_form.point_of_interest.PointOfInterestViewStateItems

data class MainViewState(
    val currentLoggedInAgent: RealEstateAgentEntity,
    val isFilterMenuVisible: Boolean,
    val pointOfInterestViewStateItems: List<PointOfInterestViewStateItems>,
    val estateTypeViewStateItems: List<EstateTypeViewStateItems>
)