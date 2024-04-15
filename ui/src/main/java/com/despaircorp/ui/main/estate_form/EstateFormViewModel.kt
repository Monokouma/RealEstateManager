package com.despaircorp.ui.main.estate_form

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.despaircorp.domain.estate.CreateEstateUseCase
import com.despaircorp.domain.estate.model.PointOfInterestEntity
import com.despaircorp.domain.estate.model.PointOfInterestEnum
import com.despaircorp.domain.picture.SaveBitmapPictureToInternalStorageUseCase
import com.despaircorp.domain.picture.model.EstatePictureEntity
import com.despaircorp.domain.picture.model.EstatePictureType
import com.despaircorp.domain.real_estate_agent.GetRealEstateAgentEntitiesUseCase
import com.despaircorp.ui.R
import com.despaircorp.ui.main.estate_form.agent.EstateFormAgentViewStateItems
import com.despaircorp.ui.main.estate_form.picture.PictureViewStateItems
import com.despaircorp.ui.main.estate_form.point_of_interest.PointOfInterestViewStateItems
import com.despaircorp.ui.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EstateFormViewModel @Inject constructor(
    private val getRealEstateAgentEntitiesUseCase: GetRealEstateAgentEntitiesUseCase,
    private val application: Application,
    private val createEstateUseCase: CreateEstateUseCase,
    private val saveBitmapPictureToInternalStorageUseCase: SaveBitmapPictureToInternalStorageUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val viewActionMutableLiveData = MutableLiveData<Event<EstateFormAction>>()
    val viewAction: LiveData<Event<EstateFormAction>> = viewActionMutableLiveData
    
    private val pointOfInterestListMutableStateFlow =
        MutableStateFlow<List<PointOfInterestEntity>>(emptyList())
    private val chosenPictureMutableStateFlow =
        MutableStateFlow<List<EstatePictureEntity>>(emptyList())
    private val selectedAgentMutableStateFlow = MutableStateFlow(1)
    private val isEstateSoldMutableStateFlow = MutableStateFlow(false)
    private val selectedEntryDateMutableStateFlow = MutableStateFlow("")
    private val selectedSoldDateMutableStateFlow = MutableStateFlow("")
    private var type: String = ""
    private var surface: String = ""
    private var description: String = ""
    private var roomNumber: String = ""
    private var bathroomNumber: String = ""
    private var bedroomNumber: String = ""
    private var address: String = ""
    private var city: String = ""
    private var price: String = ""
    private var entryDate: String = ""
    private var sellingDate: String = ""
    
    private val combineFlowOfIsSoldEntryDateAndSoldDate = combine(
        isEstateSoldMutableStateFlow,
        selectedEntryDateMutableStateFlow,
        selectedSoldDateMutableStateFlow
    ) { isSold, entryDate, soldDate ->
        Triple(isSold, entryDate, soldDate)
    }
    
    init {
        selectedEntryDateMutableStateFlow.value =
            application.getString(R.string.click_to_pick_a_date)
        selectedSoldDateMutableStateFlow.value =
            application.getString(R.string.click_to_pick_a_date)
        
        val list = mutableListOf<PointOfInterestEntity>()
        PointOfInterestEnum.entries.forEach {
            list.add(
                PointOfInterestEntity(
                    isSelected = false,
                    pointOfInterestEnum = it,
                    id = it.id
                )
            )
        }
        pointOfInterestListMutableStateFlow.update {
            it + list
        }
    }
    
    val viewState = liveData<EstateFormViewState> {
        combine(
            getRealEstateAgentEntitiesUseCase.invoke(),
            chosenPictureMutableStateFlow,
            selectedAgentMutableStateFlow,
            pointOfInterestListMutableStateFlow,
            combineFlowOfIsSoldEntryDateAndSoldDate,
        ) { realEstateAgentEntities, chosenPictureEntities, selectedAgent, pointOfInterestList, combinedFlow ->
            Log.i("Monokouma", savedStateHandle.get<Boolean>(ARG_IS_EDIT_MODE).toString())
            Log.i("Monokouma", savedStateHandle.get<Int>(ARG_TO_EDIT_ESTATE_ID).toString())
            emit(
                EstateFormViewState(
                    agentViewStateItems = realEstateAgentEntities.map {
                        EstateFormAgentViewStateItems(
                            id = it.id,
                            image = it.imageResource,
                            name = it.name,
                            it.id == selectedAgent
                        )
                    },
                    pictureViewStateItems = chosenPictureEntities.map {
                        PictureViewStateItems(
                            bitmap = it.imagePath,
                            type = it.description.name,
                            id = it.id
                        )
                    },
                    pointOfInterestViewStateItems = pointOfInterestList.map {
                        PointOfInterestViewStateItems(
                            it.pointOfInterestEnum,
                            it.id,
                            it.isSelected
                        )
                    },
                    surface = surface,
                    description = description,
                    roomNumber = roomNumber,
                    bathRoomNumber = bathroomNumber,
                    bedRoomNumber = bedroomNumber,
                    address = address,
                    city = city,
                    price = price,
                    isSoldEstate = combinedFlow.first,
                    entryDate = combinedFlow.second,
                    sellingDate = combinedFlow.third
                )
            )
        }.collect()
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
    
    fun onAddPicture(selectedType: EstatePictureType, uri: Uri, id: Int) {
        viewModelScope.launch {
            
            val imagePath =
                saveBitmapPictureToInternalStorageUseCase.invoke(uri, selectedType.name, id)
            
            val toAdd = EstatePictureEntity(
                id = id,
                imagePath = imagePath,
                description = selectedType
            )
            chosenPictureMutableStateFlow.update {
                it + toAdd
            }
        }
    }
    
    fun onAgentClicked(agentId: Int) {
        viewModelScope.launch {
            selectedAgentMutableStateFlow.value = agentId
        }
        
    }
    
    fun onSpinnerSelectionChanged(selectedItemInput: Any) {
        type = selectedItemInput.toString()
    }
    
    fun onSurfaceTextChanged(surfaceInput: String) {
        surface = surfaceInput
    }
    
    fun onDescriptionTextChanged(onDescriptionInput: String) {
        description = onDescriptionInput
    }
    
    fun onRoomTextChanged(roomInput: String) {
        roomNumber = roomInput
    }
    
    fun onBedroomTextChanged(bedroomInput: String) {
        bedroomNumber = bedroomInput
    }
    
    fun onBathroomTextChanged(bathroomInput: String) {
        bathroomNumber = bathroomInput
    }
    
    fun onLocationTextChanged(locationInput: String) {
        address = locationInput
    }
    
    fun onCityTextChanged(cityInput: String) {
        city = cityInput
    }
    
    fun onPriceTextChanged(priceInput: String) {
        price = priceInput
    }
    
    fun onForSaleButtonClicked() {
        isEstateSoldMutableStateFlow.value = false
    }
    
    fun onSoldButtonClicked() {
        isEstateSoldMutableStateFlow.value = true
    }
    
    fun onEntryDateChanged(localDate: String) {
        selectedEntryDateMutableStateFlow.value = localDate
        entryDate = localDate
    }
    
    fun onSoldDateChanged(localDate: String) {
        selectedSoldDateMutableStateFlow.value = localDate
        sellingDate = localDate
    }
    
    fun onCreateEstateClicked() {
        viewModelScope.launch {
            val creationResult = createEstateUseCase.invoke(
                estateSurface = surface,
                estateDescription = description,
                estateRoomNumber = roomNumber,
                estateBedroomNumber = bedroomNumber,
                estateBathRoomNumber = bathroomNumber,
                estateAddress = address,
                estateCity = city,
                estatePrice = price,
                estateType = type,
                estatePointOfInterests = pointOfInterestListMutableStateFlow.value,
                isEstateSold = isEstateSoldMutableStateFlow.value,
                estateEntryDate = selectedEntryDateMutableStateFlow.value,
                estateSoldDate = selectedSoldDateMutableStateFlow.value,
                estatePictures = chosenPictureMutableStateFlow.value,
                estateInChargeAgentId = selectedAgentMutableStateFlow.value
            )
            
            val result = creationResult.getOrElse {
                it.message
            }
            
            viewActionMutableLiveData.value = if (result is Boolean) {
                Event(EstateFormAction.Success)
            } else {
                Event(EstateFormAction.OnError(message = result.toString()))
            }
        }
    }
    
    companion object {
        private const val ARG_IS_EDIT_MODE = "ARG_IS_EDIT_MODE"
        private const val ARG_TO_EDIT_ESTATE_ID = "ARG_TO_EDIT_ESTATE_ID"
        
    }
}