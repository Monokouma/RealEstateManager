package com.despaircorp.ui.main.master_fragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import com.despaircorp.domain.currency.GetActualCurrencyUseCase
import com.despaircorp.domain.currency.model.CurrencyEnum
import com.despaircorp.domain.estate.GetEstateWithPictureEntityAsFlowUseCase
import com.despaircorp.domain.geocoder.GetAddressFromLatLngUseCase
import com.despaircorp.ui.R
import com.despaircorp.ui.main.master_fragment.estate.EstateViewStateItems
import com.despaircorp.ui.utils.ConnectionUtils
import com.despaircorp.ui.utils.getEuroFromDollar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class MasterViewModel @Inject constructor(
    private val getEstateWithPictureEntityAsFlowUseCase: GetEstateWithPictureEntityAsFlowUseCase,
    private val connectionUtils: ConnectionUtils,
    private val getAddressFromLatLngUseCase: GetAddressFromLatLngUseCase,
    private val application: Application,
    private val getActualCurrencyUseCase: GetActualCurrencyUseCase
) : ViewModel() {
    
    private val selectedItem = MutableStateFlow(1)
    
    val viewState = liveData<MasterViewState> {
        combine(
            getEstateWithPictureEntityAsFlowUseCase.invoke(),
            connectionUtils.asFlow(),
            selectedItem,
            getActualCurrencyUseCase.invoke()
        ) { estateEntities, isConnectedToInternet, selectedItem, actualCurrency ->
            
            emit(
                MasterViewState(
                    estateEntities.map {
                        EstateViewStateItems(
                            id = it.estateEntity.id,
                            picture = it.pictures.first().bitmapImage,
                            city = if (isConnectedToInternet) {
                                getAddressFromLatLngUseCase.invoke(it.estateEntity.location)["city"]
                                    ?: ""
                            } else {
                                ""
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