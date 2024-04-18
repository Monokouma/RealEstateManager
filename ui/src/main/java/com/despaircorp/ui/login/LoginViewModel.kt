package com.despaircorp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.despaircorp.domain.real_estate_agent.GetRealEstateAgentEntitiesUseCase
import com.despaircorp.domain.real_estate_agent.IsAgentCurrentlyLoggedInUseCase
import com.despaircorp.domain.real_estate_agent.LogChosenAgentUseCase
import com.despaircorp.shared.R
import com.despaircorp.ui.login.agent.AgentDropDownViewStateItems
import com.despaircorp.ui.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getRealEstateAgentEntitiesUseCase: GetRealEstateAgentEntitiesUseCase,
    private val logChosenAgentUseCase: LogChosenAgentUseCase,
    private val isAgentCurrentlyLoggedInUseCase: IsAgentCurrentlyLoggedInUseCase
) : ViewModel() {
    
    private val viewActionMutableLiveData = MutableLiveData<Event<LoginViewAction>>()
    val viewActionLiveData: LiveData<Event<LoginViewAction>> = viewActionMutableLiveData
    
    private val viewStateMutableLiveData = MutableLiveData<LoginViewState>()
    val viewStateLiveData: LiveData<LoginViewState> = viewStateMutableLiveData
    
    
    init {
        viewModelScope.launch {
            
            getRealEstateAgentEntitiesUseCase.invoke().collect { realEstateAgentEntities ->
                if (isAgentCurrentlyLoggedInUseCase.invoke()) {
                    viewActionMutableLiveData.value = Event(LoginViewAction.AlreadyLoggedInAgent)
                } else {
                    viewStateMutableLiveData.value = LoginViewState(
                        agentDropDownViewStateItems = realEstateAgentEntities.map {
                            AgentDropDownViewStateItems(
                                it.id,
                                it.imageResource,
                                it.name
                            )
                        }
                    )
                }
            }
        }
    }
    
    fun onAgentClicked(clickedAgentId: Int) {
        viewModelScope.launch {
            viewActionMutableLiveData.value =
                if (logChosenAgentUseCase.invoke(clickedAgentId)) {
                    Event(LoginViewAction.SuccessLogin)
                } else {
                    Event(LoginViewAction.Error(R.string.error))
                }
            
        }
    }
}