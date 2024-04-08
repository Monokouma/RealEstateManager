package com.despaircorp.ui.main.master_fragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.despaircorp.domain.connectivity.IsUserConnectedToInternetUseCase
import com.despaircorp.domain.currency.GetActualCurrencyUseCase
import com.despaircorp.domain.currency.model.CurrencyEnum
import com.despaircorp.domain.estate.GetEstateWithPictureEntityAsFlowUseCase
import com.despaircorp.domain.geocoder.GetAddressFromLatLngUseCase
import com.despaircorp.ui.R
import com.despaircorp.ui.main.master_fragment.estate.EstateViewStateItems
import com.despaircorp.ui.utils.getEuroFromDollar
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class MasterViewModel @Inject constructor(
    private val getEstateWithPictureEntityAsFlowUseCase: GetEstateWithPictureEntityAsFlowUseCase,
    private val getAddressFromLatLngUseCase: GetAddressFromLatLngUseCase,
    private val application: Application,
    private val getActualCurrencyUseCase: GetActualCurrencyUseCase,
    private val isUserConnectedToInternetUseCase: IsUserConnectedToInternetUseCase
) : ViewModel() {
    
    private val selectedItem = MutableStateFlow(1)
    
    val viewState = liveData<MasterViewState> {
        combine(
            getEstateWithPictureEntityAsFlowUseCase.invoke(),
            selectedItem,
            getActualCurrencyUseCase.invoke(),
            isUserConnectedToInternetUseCase.invoke()
        ) { estateEntities, selectedItem, actualCurrency, isConnectedToInternet ->
            
            emit(
                MasterViewState(
                    estateEntities.map {
                        EstateViewStateItems(
                            id = it.estateEntity.id,
                            picture = it.pictures.first().bitmapImage,
                            city = if (isConnectedToInternet) {
                                getAddressFromLatLngUseCase.invoke(
                                    it.estateEntity.location ?: LatLng(0.0, 0.0)
                                )["city"]
                                    ?: it.estateEntity.city
                            } else {
                                it.estateEntity.city
                            },
                            type = it.estateEntity.estateType,
                            price = if (actualCurrency.currencyEnum == CurrencyEnum.EURO) {
                                StringBuilder().append(
                                    getEuroFromDollar(it.estateEntity.price)
                                )
                                    .append(application.getString(actualCurrency.currencyEnum.symbolResource))
                                    .toString()
                            } else {
                                StringBuilder()
                                    .append(application.getString(actualCurrency.currencyEnum.symbolResource))
                                    .append(it.estateEntity.price)
                                    .toString()
                            },
                            isSelected = if (application.resources.getBoolean(R.bool.isLandscape)) {
                                it.estateEntity.id == selectedItem
                            } else {
                                false
                            }
                        )
                    }
                )
            )
        }.collect()
    }
    
    fun onEstateClicked(estateId: Int) {
        selectedItem.value = estateId
    }
}