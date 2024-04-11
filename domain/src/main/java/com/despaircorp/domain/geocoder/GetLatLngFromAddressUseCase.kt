package com.despaircorp.domain.geocoder

import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class GetLatLngFromAddressUseCase @Inject constructor(
    private val geocoderDomainRepository: GeocoderDomainRepository
) {
    suspend fun invoke(address: String): LatLng? {
        var latLng: LatLng? = null
        val foundedAddress = geocoderDomainRepository.resolveLatLngFromAddress(address)
        
        if (foundedAddress.isEmpty()) {
            latLng = null
        } else {
            foundedAddress.forEach {
                latLng = LatLng(it.latitude, it.longitude)
            }
        }
        return latLng
    }
    
}