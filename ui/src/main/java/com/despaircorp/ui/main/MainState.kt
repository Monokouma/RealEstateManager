package com.despaircorp.ui.main

import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity

sealed class MainState {
    data class MainStateView(val currentLoggedInAgent: RealEstateAgentEntity) : MainState()
    object Loading : MainState()
    
    object Disconnected : MainState()
    
    data class Error(val errorMessageRes: Int) : MainState()
}