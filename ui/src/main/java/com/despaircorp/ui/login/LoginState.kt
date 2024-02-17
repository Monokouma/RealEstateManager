package com.despaircorp.ui.login

import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity

sealed class LoginState {
    object CountDown : LoginState()
    object AlreadyLoggedInAgent : LoginState()
    
    data class ShowRealEstateAgentEntities(val realEstateAgentEntities: List<RealEstateAgentEntity>) :
        LoginState()
    
    data class Error(val error: Int) : LoginState()
    
    object SuccessLogin : LoginState()
    
}