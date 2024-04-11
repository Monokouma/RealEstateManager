package com.despaircorp.data.geocoder

import android.location.Address
import android.location.Geocoder
import com.despaircorp.data.utils.CoroutineDispatcherProvider
import com.despaircorp.domain.geocoder.GeocoderDomainRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GeocoderDataRepository @Inject constructor(
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val geocoder: Geocoder
) : GeocoderDomainRepository {
    override suspend fun resolveAddressFromLatLng(location: LatLng): List<Address> =
        withContext(coroutineDispatcherProvider.io) {
            geocoder.getFromLocation(location.latitude, location.longitude, 1) ?: emptyList()
        }
    
    override suspend fun resolveLatLngFromAddress(address: String): List<Address> =
        withContext(coroutineDispatcherProvider.io) {
            geocoder.getFromLocationName(address, 1) ?: emptyList()
        }
    
}