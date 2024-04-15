package com.despaircorp.ui.main.details_fragment

import com.despaircorp.ui.main.estate_form.picture.PictureViewStateItems
import com.google.android.gms.maps.model.LatLng

data class DetailsViewState(
    val pictureViewStateItems: List<PictureViewStateItems>,
    val id: Int,
    val description: String,
    val surface: String,
    val latLng: LatLng,
    val address: String,
    val roomNumber: Int,
    val bedroomNumber: Int,
    val bathroomNumber: Int,
    val willShowMap: Boolean,
    val willShowUnavailableMapMessage: Boolean,
)