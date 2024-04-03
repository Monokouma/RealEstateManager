package com.despaircorp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.despaircorp.domain.real_estate_agent.GetRealEstateAgentEntitiesUseCase
import com.despaircorp.domain.real_estate_agent.IsAgentCurrentlyLoggedInUseCase
import com.despaircorp.domain.real_estate_agent.LogChosenAgentUseCase
import com.despaircorp.domain.splash_screen.CountDownSplashScreenUseCase
import com.despaircorp.shared.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val countDownSplashScreenUseCase: CountDownSplashScreenUseCase,
    private val getRealEstateAgentEntitiesUseCase: GetRealEstateAgentEntitiesUseCase,
    private val logChosenAgentUseCase: LogChosenAgentUseCase,
    private val isAgentCurrentlyLoggedInUseCase: IsAgentCurrentlyLoggedInUseCase
) : ViewModel() {
    
    val uiStateFlow: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.CountDown)
    
    init {
        viewModelScope.launch {
            
            getRealEstateAgentEntitiesUseCase.invoke().collect { realEstateAgentEntities ->
                if (countDownSplashScreenUseCase.invoke()) {
                    
                    if (isAgentCurrentlyLoggedInUseCase.invoke()) {
                        uiStateFlow.value = LoginState.AlreadyLoggedInAgent
                    } else {
                        uiStateFlow.value =
                            LoginState.ShowRealEstateAgentEntities(realEstateAgentEntities = realEstateAgentEntities)
                        
                    }
                } else {
                    uiStateFlow.value = LoginState.CountDown
                }
            }
        }
    }
    
    fun onSelectedRealEstateAgent(realEstateAgentId: Int) {
        viewModelScope.launch {
            uiStateFlow.value = if (logChosenAgentUseCase.invoke(realEstateAgentId)) {
                LoginState.SuccessLogin
            } else {
                LoginState.Error(R.string.error)
            }
        }
    }
}