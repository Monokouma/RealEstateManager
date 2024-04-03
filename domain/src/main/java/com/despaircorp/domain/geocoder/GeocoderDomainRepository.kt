package com.despaircorp.domain.geocoder

import android.location.Address
import com.google.android.gms.maps.model.LatLng

interface GeocoderDomainRepository {
    suspend fun resolveAddressFromLatLng(location: LatLng): List<Address>
}