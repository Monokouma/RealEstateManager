package com.despaircorp.stubs

import com.despaircorp.domain.real_estate_agent.GetRealEstateAgentEntitiesUseCase
import com.despaircorp.domain.real_estate_agent.IsAgentCurrentlyLoggedInUseCase
import com.despaircorp.domain.real_estate_agent.LogChosenAgentUseCase
import com.despaircorp.domain.splash_screen.CountDownSplashScreenUseCase
import com.despaircorp.ui.login.LoginViewModel


object ViewModelinator {
    
    fun provideLoginViewModel(
        countDownSplashScreenUseCase: CountDownSplashScreenUseCase,
        getRealEstateAgentEntitiesUseCase: GetRealEstateAgentEntitiesUseCase,
        logChosenAgentUseCase: LogChosenAgentUseCase,
        isAgentCurrentlyLoggedInUseCase: IsAgentCurrentlyLoggedInUseCase,
    ) = LoginViewModel(
        countDownSplashScreenUseCase,
        getRealEstateAgentEntitiesUseCase,
        logChosenAgentUseCase,
        isAgentCurrentlyLoggedInUseCase
    )
}