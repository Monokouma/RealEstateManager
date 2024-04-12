package com.despaircorp.data.location

import android.annotation.SuppressLint
import com.despaircorp.data.utils.CoroutineDispatcherProvider
import com.despaircorp.domain.location.LocationDomainRepository
import com.despaircorp.domain.location.model.LocationEntity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@SuppressLint("MissingPermission")
class LocationDataRepository @Inject constructor(
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : LocationDomainRepository {
    override fun getUserLocationAsFlow(): Flow<LocationEntity> = callbackFlow {
        val locationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let {
                    trySend(
                        LocationEntity(
                            userLatLng = LatLng(it.latitude, it.longitude)
                        )
                    )
                }
            }
        }
        
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 60_000L)
            .setMinUpdateDistanceMeters(4f)
            .build()
        
        
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            coroutineDispatcherProvider.io.asExecutor(),
            locationCallback,
        )
        
        awaitClose { fusedLocationProviderClient.removeLocationUpdates(locationCallback) }
    }.flowOn(coroutineDispatcherProvider.io)
}