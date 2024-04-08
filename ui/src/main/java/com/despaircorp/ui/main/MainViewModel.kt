package com.despaircorp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.despaircorp.domain.currency.ChangeActualCurrencyUseCase
import com.despaircorp.domain.real_estate_agent.DisconnectAgentUseCase
import com.despaircorp.domain.real_estate_agent.GetLoggedRealEstateAgentEntityUseCase
import com.despaircorp.domain.real_estate_agent.InsertCreatedAgentUseCase
import com.despaircorp.shared.R
import com.despaircorp.ui.utils.Event
import com.despaircorp.ui.utils.ProfilePictureRandomizator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getLoggedRealEstateAgentEntityUseCase: GetLoggedRealEstateAgentEntityUseCase,
    private val disconnectAgentUseCase: DisconnectAgentUseCase,
    private val profilePictureRandomizator: ProfilePictureRandomizator,
    private val insertCreatedAgentUseCase: InsertCreatedAgentUseCase,
    private val changeActualCurrencyUseCase: ChangeActualCurrencyUseCase
) : ViewModel() {
    
    private var agentName: String? = null
    
    private val viewActionMutableLiveData = MutableLiveData<Event<MainViewAction>>()
    val viewAction: LiveData<Event<MainViewAction>> = viewActionMutableLiveData
    
    val viewState = liveData<MainViewState> {
        val currentLoggedInAgent = getLoggedRealEstateAgentEntityUseCase.invoke()
        
        emit(
            MainViewState(
                currentLoggedInAgent = currentLoggedInAgent
            )
        )
    }
    
    fun onDisconnect() {
        viewModelScope.launch {
            viewActionMutableLiveData.value =
                if (disconnectAgentUseCase.invoke(getLoggedRealEstateAgentEntityUseCase.invoke().id)) {
                    Event(MainViewAction.SuccessDisconnection)
                } else {
                    Event(MainViewAction.Error(R.string.error))
                }
        }
    }
    
    fun onAgentNameTextChanged(agentNameInput: String) {
        agentName = agentNameInput
    }
    
    
    fun onCreateAgentClick() {
        viewModelScope.launch {
            viewActionMutableLiveData.value = if (agentName.isNullOrEmpty()) {
                //Error empty
                Event(MainViewAction.Error(R.string.empty_text))
            } else {
                if (insertCreatedAgentUseCase.invoke(
                        agentName ?: return@launch,
                        profilePictureRandomizator.provideRandomProfilePicture()
                    )
                ) {
                    Event(MainViewAction.AgentCreationSuccess(R.string.agent_created))
                    
                } else {
                    Event(MainViewAction.Error(R.string.error))
                    
                }
            }
        }
    }
    
    fun onChangeCurrencyClicked() {
        viewModelScope.launch {
            changeActualCurrencyUseCase.invoke()
        }
    }
}