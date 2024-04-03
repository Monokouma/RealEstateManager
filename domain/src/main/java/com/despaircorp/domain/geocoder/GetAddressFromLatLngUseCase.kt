package com.despaircorp.domain.geocoder

import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class GetAddressFromLatLngUseCase @Inject constructor(
    private val geocoderDomainRepository: GeocoderDomainRepository
) {
    suspend fun invoke(location: LatLng): Map<String, String> {
        var locationMap = emptyMap<String, String>()
        
        geocoderDomainRepository.resolveAddressFromLatLng(location).forEach {
            
            locationMap = mapOf(
                "city" to it.locality,
                "address" to it.getAddressLine(0)
            )
        }
        
        return locationMap
    }
}