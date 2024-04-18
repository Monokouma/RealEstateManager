package com.despaircorp.ui.main.main_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.despaircorp.domain.currency.ChangeActualCurrencyUseCase
import com.despaircorp.domain.estate.model.EstateTypeEntity
import com.despaircorp.domain.estate.model.EstateTypeEnum
import com.despaircorp.domain.estate.model.PointOfInterestEntity
import com.despaircorp.domain.estate.model.PointOfInterestEnum
import com.despaircorp.domain.real_estate_agent.DisconnectAgentUseCase
import com.despaircorp.domain.real_estate_agent.GetLoggedRealEstateAgentEntityUseCase
import com.despaircorp.domain.real_estate_agent.InsertCreatedAgentUseCase
import com.despaircorp.shared.R
import com.despaircorp.ui.main.estate_form.estate_type.EstateTypeViewStateItems
import com.despaircorp.ui.main.estate_form.point_of_interest.PointOfInterestViewStateItems
import com.despaircorp.ui.utils.Event
import com.despaircorp.ui.utils.ProfilePictureRandomizator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
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
    private val isMenuItemVisibleMutableStateFlow = MutableStateFlow(false)
    
    private val viewActionMutableLiveData = MutableLiveData<Event<MainViewAction>>()
    val viewAction: LiveData<Event<MainViewAction>> = viewActionMutableLiveData
    
    val pointOfInterestListMutableStateFlow =
        MutableStateFlow<List<PointOfInterestEntity>>(emptyList())
    
    val estateTypeListMutableStateFlow =
        MutableStateFlow<List<EstateTypeEntity>>(emptyList())
    
    init {
        val pointOfInterestList = mutableListOf<PointOfInterestEntity>()
        PointOfInterestEnum.entries.forEach {
            pointOfInterestList.add(
                PointOfInterestEntity(
                    isSelected = false,
                    pointOfInterestEnum = it,
                    id = it.id
                )
            )
        }
        pointOfInterestListMutableStateFlow.update {
            it + pointOfInterestList
        }
        
        val estateTypeList = mutableListOf<EstateTypeEntity>()
        EstateTypeEnum.entries.forEach {
            estateTypeList.add(
                EstateTypeEntity(
                    isSelected = false,
                    estateTypeEnum = it,
                    id = it.id
                )
            )
        }
        estateTypeListMutableStateFlow.update {
            it + estateTypeList
        }
    }
    
    val viewState = liveData<MainViewState> {
        val currentLoggedInAgent = getLoggedRealEstateAgentEntityUseCase.invoke()
        
        combine(
            isMenuItemVisibleMutableStateFlow,
            pointOfInterestListMutableStateFlow,
            estateTypeListMutableStateFlow
        ) { isMenuVisible, pointOfInterestList, estateTypeList ->
            emit(
                MainViewState(
                    currentLoggedInAgent = currentLoggedInAgent,
                    isFilterMenuVisible = isMenuVisible,
                    pointOfInterestList.map {
                        PointOfInterestViewStateItems(
                            it.pointOfInterestEnum,
                            it.id,
                            it.isSelected
                        )
                    },
                    estateTypeList.map {
                        EstateTypeViewStateItems(
                            it.id,
                            it.estateTypeEnum,
                            it.isSelected,
                            true
                        )
                    }
                )
            )
        }.collect()
        
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
    
    fun onRemovePointOfInterest(
        id: Int,
    ) {
        pointOfInterestListMutableStateFlow.value = pointOfInterestListMutableStateFlow.value.map {
            if (it.id == id) {
                PointOfInterestEntity(
                    false,
                    it.pointOfInterestEnum,
                    it.id
                )
            } else {
                it
            }
        }
    }
    
    fun onAddPointOfInterest(id: Int) {
        pointOfInterestListMutableStateFlow.value = pointOfInterestListMutableStateFlow.value.map {
            if (it.id == id) {
                PointOfInterestEntity(
                    true,
                    it.pointOfInterestEnum,
                    it.id
                )
            } else {
                it
            }
        }
    }
    
    fun onRemoveEstateType(
        id: Int,
    ) {
        estateTypeListMutableStateFlow.value = estateTypeListMutableStateFlow.value.map {
            if (it.id == id) {
                EstateTypeEntity(
                    false,
                    it.estateTypeEnum,
                    it.id
                )
            } else {
                it
            }
        }
    }
    
    fun onAddEstateType(id: Int) {
        estateTypeListMutableStateFlow.value = estateTypeListMutableStateFlow.value.map {
            if (it.id == id) {
                EstateTypeEntity(
                    true,
                    it.estateTypeEnum,
                    it.id
                )
            } else {
                it
            }
        }
    }
    
    fun onChangeCurrencyClicked() {
        viewModelScope.launch {
            changeActualCurrencyUseCase.invoke()
        }
    }
    
    fun onSearchMenuClicked() {
        isMenuItemVisibleMutableStateFlow.value = !isMenuItemVisibleMutableStateFlow.value
    }
    
    fun resetList() {
        val pointOfInterestList = mutableListOf<PointOfInterestEntity>()
        PointOfInterestEnum.entries.forEach {
            pointOfInterestList.add(
                PointOfInterestEntity(
                    isSelected = false,
                    pointOfInterestEnum = it,
                    id = it.id
                )
            )
        }
        pointOfInterestListMutableStateFlow.value = pointOfInterestList
        
        
        val estateTypeList = mutableListOf<EstateTypeEntity>()
        EstateTypeEnum.entries.forEach {
            estateTypeList.add(
                EstateTypeEntity(
                    isSelected = false,
                    estateTypeEnum = it,
                    id = it.id
                )
            )
        }
        
        estateTypeListMutableStateFlow.value = estateTypeList
        
    }
    
}