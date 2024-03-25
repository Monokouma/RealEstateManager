package com.despaircorp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.despaircorp.domain.GetEstateWithPictureEntityAsFlowUseCase
import com.despaircorp.domain.real_estate_agent.DisconnectAgentUseCase
import com.despaircorp.domain.real_estate_agent.GetLoggedRealEstateAgentEntityUseCase
import com.despaircorp.domain.real_estate_agent.InsertCreatedAgentUseCase
import com.despaircorp.shared.R
import com.despaircorp.ui.utils.ProfilePictureRandomizator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.despaircorp.ui.main.Error as StateError

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getLoggedRealEstateAgentEntityUseCase: GetLoggedRealEstateAgentEntityUseCase,
    private val disconnectAgentUseCase: DisconnectAgentUseCase,
    private val profilePictureRandomizator: ProfilePictureRandomizator,
    private val insertCreatedAgentUseCase: InsertCreatedAgentUseCase,
    private val getEstateWithPictureEntityAsFlowUseCase: GetEstateWithPictureEntityAsFlowUseCase
) : ViewModel() {
    
    val uiState: MutableStateFlow<MainState> = MutableStateFlow(MainState.Loading)
    
    private var agentName: String? = null
    
    init {
        viewModelScope.launch {
            val currentLoggedInAgent = getLoggedRealEstateAgentEntityUseCase.invoke()
            getEstateWithPictureEntityAsFlowUseCase.invoke().collect { list ->
                uiState.value = MainState.MainStateView(
                    currentLoggedInAgent,
                    StateError(0, false),
                    OnCreateAgentSuccess(false, 0),
                    list
                )
            }
        }
    }
    
    fun onDisconnect(id: Int) {
        viewModelScope.launch {
            uiState.value = if (disconnectAgentUseCase.invoke(id)) {
                MainState.Disconnected
            } else {
                MainState.MainStateView(
                    getLoggedRealEstateAgentEntityUseCase.invoke(),
                    StateError(R.string.error, true),
                    OnCreateAgentSuccess(false, 0),
                    emptyList()
                )
            }
        }
    }
    
    fun onRealEstateAgentNameTextChange(agentNameInput: String) {
        agentName = agentNameInput
    }
    
    fun onCreateAgentClick() {
        viewModelScope.launch {
            uiState.value = if (agentName.isNullOrEmpty()) {
                //Throw error
                MainState.MainStateView(
                    getLoggedRealEstateAgentEntityUseCase.invoke(),
                    StateError(R.string.empty_text, true),
                    OnCreateAgentSuccess(false, 0),
                    emptyList()
                )
            } else {
                if (insertCreatedAgentUseCase.invoke(
                        agentName ?: return@launch,
                        profilePictureRandomizator.provideRandomProfilePicture()
                    )
                ) {
                    MainState.MainStateView(
                        getLoggedRealEstateAgentEntityUseCase.invoke(),
                        StateError(0, false),
                        OnCreateAgentSuccess(true, R.string.agent_created),
                        emptyList()
                    )
                    
                } else {
                    MainState.MainStateView(
                        getLoggedRealEstateAgentEntityUseCase.invoke(),
                        StateError(R.string.error, true),
                        OnCreateAgentSuccess(false, 0),
                        emptyList()
                    )
                    
                }
            }
        }
    }
}