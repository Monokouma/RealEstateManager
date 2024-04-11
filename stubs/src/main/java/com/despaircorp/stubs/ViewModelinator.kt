package com.despaircorp.stubs

import android.app.Application
import com.despaircorp.domain.estate.GetEstateWithPictureEntityAsFlowUseCase
import com.despaircorp.domain.geocoder.GetAddressFromLatLngUseCase
import com.despaircorp.domain.real_estate_agent.DisconnectAgentUseCase
import com.despaircorp.domain.real_estate_agent.GetLoggedRealEstateAgentEntityUseCase
import com.despaircorp.domain.real_estate_agent.GetRealEstateAgentEntitiesUseCase
import com.despaircorp.domain.real_estate_agent.InsertCreatedAgentUseCase
import com.despaircorp.domain.real_estate_agent.IsAgentCurrentlyLoggedInUseCase
import com.despaircorp.domain.real_estate_agent.LogChosenAgentUseCase
import com.despaircorp.domain.splash_screen.CountDownSplashScreenUseCase
import com.despaircorp.ui.login.LoginViewModel
import com.despaircorp.ui.main.main_activity.MainViewModel
import com.despaircorp.ui.utils.ConnectionUtils
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
        getEstateWithPictureEntityAsFlowUseCase: GetEstateWithPictureEntityAsFlowUseCase,
        application: Application,
        getAddressFromLatLngUseCase: GetAddressFromLatLngUseCase,
        connectionUtils: ConnectionUtils
    ) = MainViewModel(
        getLoggedRealEstateAgentEntityUseCase = getLoggedRealEstateAgentEntityUseCase,
        disconnectAgentUseCase = disconnectAgentUseCase,
        profilePictureRandomizator = profilePictureRandomizator,
        insertCreatedAgentUseCase = insertCreatedAgentUseCase,
        getEstateWithPictureEntityAsFlowUseCase = getEstateWithPictureEntityAsFlowUseCase,
        application = application,
        getAddressFromLatLngUseCase = getAddressFromLatLngUseCase,
        connectionUtils
    )
}