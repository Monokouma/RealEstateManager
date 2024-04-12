package com.despaircorp.ui.map

import com.google.android.gms.maps.model.LatLng

data class MapViewState(
    val userLocation: LatLng,
    val isConnectedToInternet: Boolean,
    val mapViewStateItems: List<MapViewStateItems>
)