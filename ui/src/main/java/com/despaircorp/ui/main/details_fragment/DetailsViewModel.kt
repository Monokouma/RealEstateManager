package com.despaircorp.ui.main.details_fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.despaircorp.domain.connectivity.IsUserConnectedToInternetUseCase
import com.despaircorp.domain.estate.GetEstateWithPictureEntityByIdUseCase
import com.despaircorp.domain.geocoder.GetAddressFromLatLngUseCase
import com.despaircorp.ui.main.estate_form.picture.PictureViewStateItems
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getEstateWithPictureEntityById: GetEstateWithPictureEntityByIdUseCase,
    private val getAddressFromLatLngUseCase: GetAddressFromLatLngUseCase,
    private val isUserConnectedToInternetUseCase: IsUserConnectedToInternetUseCase,
    
    ) : ViewModel() {
    
    private val itemIdMutableStateFlow = MutableStateFlow(1)
    
    val viewState = liveData<DetailsViewState> {
        
        itemIdMutableStateFlow.collect { estateId ->
            
            val estateWithPictureEntity = getEstateWithPictureEntityById.invoke(estateId)
            isUserConnectedToInternetUseCase.invoke().collect { isConnected ->
                
                val address =
                    if (isConnected && estateWithPictureEntity.estateEntity.location != null) {
                        getAddressFromLatLngUseCase.invoke(
                            estateWithPictureEntity.estateEntity.location ?: LatLng(0.0, 0.0)
                        )["address"] ?: estateWithPictureEntity.estateEntity.address
                    } else {
                        estateWithPictureEntity.estateEntity.address
                    }
                Log.i("Monokouma", estateWithPictureEntity.estateEntity.location.toString())
                emit(
                    DetailsViewState(
                        pictureViewStateItems = estateWithPictureEntity.pictures.map {
                            
                            PictureViewStateItems(
                                it.imagePath,
                                it.description.name,
                                it.id
                            )
                        },
                        id = estateWithPictureEntity.estateEntity.id,
                        description = estateWithPictureEntity.estateEntity.description,
                        surface = estateWithPictureEntity.estateEntity.surface,
                        latLng = estateWithPictureEntity.estateEntity.location ?: LatLng(0.0, 0.0),
                        address = address,
                        roomNumber = estateWithPictureEntity.estateEntity.roomNumber,
                        bedroomNumber = estateWithPictureEntity.estateEntity.numberOfBedrooms,
                        bathroomNumber = estateWithPictureEntity.estateEntity.bathroomNumber,
                        willShowMap = if (isConnected) {
                            estateWithPictureEntity.estateEntity.location != null
                        } else {
                            false
                        },
                        willShowUnavailableMapMessage = if (!isConnected) {
                            true
                        } else {
                            estateWithPictureEntity.estateEntity.location == null
                        }
                    )
                )
            }
            
            
        }
        
    }
    
    fun onReceiveEstateId(itemId: Int) {
        itemIdMutableStateFlow.value = itemId
    }
    
    
}