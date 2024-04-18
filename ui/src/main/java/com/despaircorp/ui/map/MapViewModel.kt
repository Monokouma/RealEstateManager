package com.despaircorp.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.despaircorp.domain.connectivity.IsUserConnectedToInternetUseCase
import com.despaircorp.domain.estate.GetEstateWithPictureEntityAsFlowUseCase
import com.despaircorp.domain.location.GetUserLocationAsFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getUserLocationAsFlowUseCase: GetUserLocationAsFlowUseCase,
    private val isUserConnectedToInternetUseCase: IsUserConnectedToInternetUseCase,
    private val getEstateWithPictureEntityAsFlowUseCase: GetEstateWithPictureEntityAsFlowUseCase,
) : ViewModel() {
    
    val viewState = liveData<MapViewState> {
        
        combine(
            getUserLocationAsFlowUseCase.invoke(),
            isUserConnectedToInternetUseCase.invoke(),
            getEstateWithPictureEntityAsFlowUseCase.invoke(SimpleSQLiteQuery(BASE_SQL_QUERY_ARG))
        ) { locationEntity, isConnected, estateWithPictureEntity ->
            
            emit(
                MapViewState(
                    userLocation = locationEntity.userLatLng,
                    isConnectedToInternet = isConnected,
                    mapViewStateItems = estateWithPictureEntity
                        .filter { it.estateEntity.location != null }
                        .map {
                            MapViewStateItems(
                                it.estateEntity.id,
                                it.estateEntity.address,
                                it.pictures.first().imagePath,
                                it.estateEntity.location?.latitude ?: return@combine,
                                it.estateEntity.location?.longitude ?: return@combine
                            )
                        }
                )
            )
        }.collect()
    }
    
    companion object {
        private const val BASE_SQL_QUERY_ARG = "SELECT * FROM estate_table"
    }
}