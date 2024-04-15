package com.despaircorp.ui.main.estate_form

import com.despaircorp.ui.main.estate_form.agent.EstateFormAgentViewStateItems
import com.despaircorp.ui.main.estate_form.picture.PictureViewStateItems
import com.despaircorp.ui.main.estate_form.point_of_interest.PointOfInterestViewStateItems

data class EstateFormViewState(
    val agentViewStateItems: List<EstateFormAgentViewStateItems>,
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