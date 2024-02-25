package com.despaircorp.ui.main

import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity

sealed class MainState {
    data class MainStateView(
        val currentLoggedInAgent: RealEstateAgentEntity,
        val error: Error,
        val onCreateAgentSuccess: OnCreateAgentSuccess
    ) : MainState()
    object Loading : MainState()
    
    object Disconnected : MainState() //To data class onClick
    
}

data class Error(val errorMessageRes: Int, val showError: Boolean)

data class OnCreateAgentSuccess(val showSuccess: Boolean, val successMessageRes: Int)
