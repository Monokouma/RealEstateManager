package com.despaircorp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.despaircorp.domain.real_estate_agent.GetRealEstateAgentEntitiesUseCase
import com.despaircorp.domain.real_estate_agent.IsAgentCurrentlyLoggedInUseCase
import com.despaircorp.domain.real_estate_agent.LogChosenAgentUseCase
import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity
import com.despaircorp.domain.splash_screen.CountDownSplashScreenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val countDownSplashScreenUseCase: CountDownSplashScreenUseCase,
    private val getRealEstateAgentEntitiesUseCase: GetRealEstateAgentEntitiesUseCase,
    private val logChosenAgentUseCase: LogChosenAgentUseCase,
    private val isAgentCurrentlyLoggedInUseCase: IsAgentCurrentlyLoggedInUseCase
) : ViewModel() {
    
    private val isSplashScreenShownMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isSplashScreenShownLiveData: LiveData<Boolean> = isSplashScreenShownMutableLiveData
    
    private val realEstateAgentEntitiesMutableLiveData: MutableLiveData<List<RealEstateAgentEntity>> =
        MutableLiveData<List<RealEstateAgentEntity>>()
    val realEstateAgentEntitiesLiveDate: LiveData<List<RealEstateAgentEntity>> =
        realEstateAgentEntitiesMutableLiveData
    
    private val isAgentCurrentlyLoggedInMutableLiveData: MutableLiveData<Boolean> =
        MutableLiveData()
    val isAgentCurrentlyLoggedInLiveData: LiveData<Boolean> =
        isAgentCurrentlyLoggedInMutableLiveData
    
    
    fun startSplashScreenTime() {
        viewModelScope.launch {
            isSplashScreenShownMutableLiveData.value = countDownSplashScreenUseCase.invoke()
        }
    }
    
    fun fetchRealEstateAgentList() {
        viewModelScope.launch {
            realEstateAgentEntitiesMutableLiveData.value =
                getRealEstateAgentEntitiesUseCase.invoke()
        }
    }
    
    fun onSelectedAgent(selectedId: Int) {
        viewModelScope.launch {
            logChosenAgentUseCase.invoke(selectedId)
        }
    }
    
    fun isAgentAlreadyLoggedIn() {
        viewModelScope.launch {
            isAgentCurrentlyLoggedInMutableLiveData.value = isAgentCurrentlyLoggedInUseCase.invoke()
        }
    }
}