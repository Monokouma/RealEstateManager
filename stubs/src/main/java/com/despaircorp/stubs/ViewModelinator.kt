package com.despaircorp.stubs

import com.despaircorp.domain.GetEstateWithPictureEntityAsFlowUseCase
import com.despaircorp.domain.real_estate_agent.DisconnectAgentUseCase
import com.despaircorp.domain.real_estate_agent.GetLoggedRealEstateAgentEntityUseCase
import com.despaircorp.domain.real_estate_agent.GetRealEstateAgentEntitiesUseCase
import com.despaircorp.domain.real_estate_agent.InsertCreatedAgentUseCase
import com.despaircorp.domain.real_estate_agent.IsAgentCurrentlyLoggedInUseCase
import com.despaircorp.domain.real_estate_agent.LogChosenAgentUseCase
import com.despaircorp.domain.splash_screen.CountDownSplashScreenUseCase
import com.despaircorp.ui.login.LoginViewModel
import com.despaircorp.ui.main.MainViewModel
import com.despaircorp.ui.utils.ProfilePictureRandomizator


object ViewModelinator {
    
    fun provideLoginViewModel(
        countDownSplashScreenUseCase: CountDownSplashScreenUseCase,
        getRealEstateAgentEntitiesUseCase: GetRealEstateAgentEntitiesUseCase,
        logChosenAgentUseCase: LogChosenAgentUseCase,
        isAgentCurrentlyLoggedInUseCase: IsAgentCurrentlyLoggedInUseCase,
    ) = LoginViewModel(
        countDownSplashScreenUseCase = countDownSplashScreenUseCase,
        getRealEstateAgentEntitiesUseCase = getRealEstateAgentEntitiesUseCase,
        logChosenAgentUseCase = logChosenAgentUseCase,
        isAgentCurrentlyLoggedInUseCase = isAgentCurrentlyLoggedInUseCase
    )
    
    fun provideMainViewModel(
        getLoggedRealEstateAgentEntityUseCase: GetLoggedRealEstateAgentEntityUseCase,
        disconnectAgentUseCase: DisconnectAgentUseCase,
        profilePictureRandomizator: ProfilePictureRandomizator,
        insertCreatedAgentUseCase: InsertCreatedAgentUseCase,
        getEstateWithPictureEntityAsFlowUseCase: GetEstateWithPictureEntityAsFlowUseCase
    ) = MainViewModel(
        getLoggedRealEstateAgentEntityUseCase = getLoggedRealEstateAgentEntityUseCase,
        disconnectAgentUseCase = disconnectAgentUseCase,
        profilePictureRandomizator = profilePictureRandomizator,
        insertCreatedAgentUseCase = insertCreatedAgentUseCase,
        getEstateWithPictureEntityAsFlowUseCase
    )
}