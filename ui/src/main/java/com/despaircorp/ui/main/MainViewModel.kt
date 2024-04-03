package com.despaircorp.ui.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.despaircorp.domain.estate.GetEstateWithPictureEntityAsFlowUseCase
import com.despaircorp.domain.estate.model.EstateWithPictureEntity
import com.despaircorp.domain.geocoder.GetAddressFromLatLngUseCase
import com.despaircorp.domain.real_estate_agent.DisconnectAgentUseCase
import com.despaircorp.domain.real_estate_agent.GetLoggedRealEstateAgentEntityUseCase
import com.despaircorp.domain.real_estate_agent.InsertCreatedAgentUseCase
import com.despaircorp.shared.R
import com.despaircorp.ui.main.activity.CurrencyEnum
import com.despaircorp.ui.utils.ProfilePictureRandomizator
import com.despaircorp.ui.utils.getEuroFromDollar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.despaircorp.ui.main.Error as StateError

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getLoggedRealEstateAgentEntityUseCase: GetLoggedRealEstateAgentEntityUseCase,
    private val disconnectAgentUseCase: DisconnectAgentUseCase,
    private val profilePictureRandomizator: ProfilePictureRandomizator,
    private val insertCreatedAgentUseCase: InsertCreatedAgentUseCase,
    private val getEstateWithPictureEntityAsFlowUseCase: GetEstateWithPictureEntityAsFlowUseCase,
    private val application: Application,
    private val getAddressFromLatLngUseCase: GetAddressFromLatLngUseCase
) : ViewModel() {
    
    val uiState: MutableStateFlow<MainState> = MutableStateFlow(MainState.Loading)
    private var agentName: String? = null
    
    val actualCurrencyMutableStateFlow: MutableStateFlow<CurrencyEnum> =
        MutableStateFlow(CurrencyEnum.US_DOLLAR)
    
    init {
        viewModelScope.launch {
            val currentLoggedInAgent = getLoggedRealEstateAgentEntityUseCase.invoke()
            
            combine(
                getEstateWithPictureEntityAsFlowUseCase.invoke(),
                actualCurrencyMutableStateFlow
            ) { estateWithPictureEntities, actualCurrency ->
                
                uiState.value = MainState.MainStateView(
                    currentLoggedInAgent,
                    StateError(0, false),
                    OnCreateAgentSuccess(false, 0),
                    
                    estateWithPictureEntities.map {
                        val address = getAddressFromLatLngUseCase.invoke(it.estateEntity.location)
                        
                        if (actualCurrency == CurrencyEnum.EURO) {
                            EstateWithPictureEntity(
                                it.estateEntity.copy(
                                    price = StringBuilder().append(
                                        getEuroFromDollar(it.estateEntity.price)
                                    ).append(application.getString(actualCurrency.symbolResource))
                                        .toString(),
                                    address = address["address"].toString(),
                                    city = address["city"].toString()
                                ),
                                it.pictures
                            )
                            
                        } else {
                            EstateWithPictureEntity(
                                it.estateEntity.copy(
                                    price = StringBuilder()
                                        .append(application.getString(actualCurrency.symbolResource))
                                        .append(it.estateEntity.price)
                                        .toString(),
                                    address = address["address"].toString(),
                                    city = address["city"].toString()
                                ),
                                it.pictures
                            )
                        }
                    }
                )
                
            }.collect()
        }
    }
    
    
    fun onDisconnect(id: Int) {
        viewModelScope.launch {
            uiState.value = if (disconnectAgentUseCase.invoke(id)) {
                MainState.Disconnected
            } else {
                MainState.MainStateView(
                    getLoggedRealEstateAgentEntityUseCase.invoke(),
                    StateError(R.string.error, true),
                    OnCreateAgentSuccess(false, 0),
                    emptyList()
                )
            }
        }
    }
    
    fun onRealEstateAgentNameTextChange(agentNameInput: String) {
        agentName = agentNameInput
    }
    
    fun onCreateAgentClick() {
        viewModelScope.launch {
            uiState.value = if (agentName.isNullOrEmpty()) {
                //Throw error
                MainState.MainStateView(
                    getLoggedRealEstateAgentEntityUseCase.invoke(),
                    StateError(R.string.empty_text, true),
                    OnCreateAgentSuccess(false, 0),
                    emptyList()
                )
            } else {
                if (insertCreatedAgentUseCase.invoke(
                        agentName ?: return@launch,
                        profilePictureRandomizator.provideRandomProfilePicture()
                    )
                ) {
                    MainState.MainStateView(
                        getLoggedRealEstateAgentEntityUseCase.invoke(),
                        StateError(0, false),
                        OnCreateAgentSuccess(true, R.string.agent_created),
                        emptyList()
                    )
                    
                } else {
                    MainState.MainStateView(
                        getLoggedRealEstateAgentEntityUseCase.invoke(),
                        StateError(R.string.error, true),
                        OnCreateAgentSuccess(false, 0),
                        emptyList()
                    )
                    
                }
            }
        }
    }
    
    fun onChangeCurrencyClicked() {
        actualCurrencyMutableStateFlow.value =
            if (actualCurrencyMutableStateFlow.value == CurrencyEnum.US_DOLLAR) {
                CurrencyEnum.EURO
            } else {
                CurrencyEnum.US_DOLLAR
            }
    }
}