package com.despaircorp.ui.main.estate_addition

import com.despaircorp.ui.main.estate_addition.agent.CreateEstateAgentViewStateItems
import com.despaircorp.ui.main.estate_addition.picture.PictureViewStateItems
import com.despaircorp.ui.main.estate_addition.point_of_interest.PointOfInterestViewStateItems

data class CreateEstateViewState(
    val agentViewStateItems: List<CreateEstateAgentViewStateItems>,
    val pictureViewStateItems: List<PictureViewStateItems>,
    val pointOfInterestViewStateItems: List<PointOfInterestViewStateItems>,
    val surface: String,
    val description: String,
    val roomNumber: String,
    val bathRoomNumber: String,
    val bedRoomNumber: String,
    val address: String,
    val city: String,
    val price: String,
    val isSoldEstate: Boolean,
    val entryDate: String,
    val sellingDate: String,
)