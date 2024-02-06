package com.despaircorp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.despaircorp.domain.real_estate_agent.DisconnectAgentUseCase
import com.despaircorp.domain.real_estate_agent.GetLoggedRealEstateAgentEntityUseCase
import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getLoggedRealEstateAgentEntityUseCase: GetLoggedRealEstateAgentEntityUseCase,
    private val disconnectAgentUseCase: DisconnectAgentUseCase
) : ViewModel() {
    
    private val loggedRealEstateAgentEntityMutableLiveData: MutableLiveData<RealEstateAgentEntity> =
        MutableLiveData()
    val loggedRealEstateAgentEntityLiveData: LiveData<RealEstateAgentEntity> =
        loggedRealEstateAgentEntityMutableLiveData
    
    private val isUserDisconnectedMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isUserDisconnectedLiveData: LiveData<Boolean> = isUserDisconnectedMutableLiveData
    
    fun fetchLoggedRealEstateAgentEntity() {
        viewModelScope.launch {
            loggedRealEstateAgentEntityMutableLiveData.value =
                getLoggedRealEstateAgentEntityUseCase.invoke()
        }
    }
    
    fun onDisconnect(id: Int) {
        viewModelScope.launch {
            isUserDisconnectedMutableLiveData.value = disconnectAgentUseCase.invoke(id)
        }
    }
}