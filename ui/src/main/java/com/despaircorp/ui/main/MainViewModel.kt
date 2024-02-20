package com.despaircorp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.despaircorp.domain.real_estate_agent.DisconnectAgentUseCase
import com.despaircorp.domain.real_estate_agent.GetLoggedRealEstateAgentEntityUseCase
import com.despaircorp.ui.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getLoggedRealEstateAgentEntityUseCase: GetLoggedRealEstateAgentEntityUseCase,
    private val disconnectAgentUseCase: DisconnectAgentUseCase
) : ViewModel() {
    
    val uiState: MutableStateFlow<MainState> = MutableStateFlow(MainState.Loading)
    
    init {
        viewModelScope.launch {
            val currentLoggedInAgent = getLoggedRealEstateAgentEntityUseCase.invoke()
            uiState.value = MainState.MainStateView(currentLoggedInAgent)
        }
    }
    
    fun onDisconnect(id: Int) {
        viewModelScope.launch {
            uiState.value = if (disconnectAgentUseCase.invoke(id)) {
                MainState.Disconnected
            } else {
                MainState.Error(R.string.error)
            }
        }
    }
}