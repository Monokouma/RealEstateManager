package com.despaircorp.ui.main.estate_form

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.despaircorp.domain.estate.EstateFormValidationUseCase
import com.despaircorp.domain.estate.GetEstateWithPictureEntityByIdUseCase
import com.despaircorp.domain.estate.model.EstateStatus
import com.despaircorp.domain.estate.model.EstateTypeEntity
import com.despaircorp.domain.estate.model.EstateTypeEnum
import com.despaircorp.domain.estate.model.EstateWithPictureEntity
import com.despaircorp.domain.estate.model.PointOfInterestEntity
import com.despaircorp.domain.estate.model.PointOfInterestEnum
import com.despaircorp.domain.picture.SaveBitmapPictureToInternalStorageUseCase
import com.despaircorp.domain.picture.model.EstatePictureEntity
import com.despaircorp.domain.picture.model.EstatePictureType
import com.despaircorp.domain.real_estate_agent.GetRealEstateAgentEntitiesUseCase
import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity
import com.despaircorp.shared.R
import com.despaircorp.ui.main.estate_form.agent.EstateFormAgentViewStateItems
import com.despaircorp.ui.main.estate_form.estate_type.EstateTypeViewStateItems
import com.despaircorp.ui.main.estate_form.picture.PictureViewStateItems
import com.despaircorp.ui.main.estate_form.point_of_interest.PointOfInterestViewStateItems
import com.despaircorp.ui.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class EstateFormViewModel @Inject constructor(
    private val application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val getRealEstateAgentEntitiesUseCase: GetRealEstateAgentEntitiesUseCase,
    private val estateFormValidationUseCase: EstateFormValidationUseCase,
    private val saveBitmapPictureToInternalStorageUseCase: SaveBitmapPictureToInternalStorageUseCase,
    private val getEstateWithPictureEntityById: GetEstateWithPictureEntityByIdUseCase,
) : ViewModel() {
    private val viewActionMutableLiveData = MutableLiveData<Event<EstateFormAction>>()
    val viewAction: LiveData<Event<EstateFormAction>> = viewActionMutableLiveData
    
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
    
    private val pointOfInterestListMutableStateFlow =
        MutableStateFlow<List<PointOfInterestEntity>>(emptyList())
    private val chosenPictureMutableStateFlow =
        MutableStateFlow<List<EstatePictureEntity>>(emptyList())
    private val estateTypeListMutableStateFlow =
        MutableStateFlow<List<EstateTypeEntity>>(emptyList())
    
    private val selectedEstateTypeIdMutableStateFlow = MutableStateFlow(0)
    
    private val selectedAgentMutableStateFlow = MutableStateFlow(1)
    private val isEstateSoldMutableStateFlow = MutableStateFlow(false)
    private val selectedEntryDateMutableStateFlow = MutableStateFlow("")
    private val selectedSoldDateMutableStateFlow = MutableStateFlow("")
    
    private val combineFlowOfIsSoldEntryDateAndSoldDate = combine(
        isEstateSoldMutableStateFlow,
        selectedEntryDateMutableStateFlow,
        selectedSoldDateMutableStateFlow
    ) { isSold,
        entryDate,
        soldDate ->
        Triple(
            first = isSold,
            second = entryDate,
            third = soldDate
        )
    }
    
    private val combineFlowOfPointOfInterestListChosenPictureEstateType = combine(
        pointOfInterestListMutableStateFlow,
        chosenPictureMutableStateFlow,
        estateTypeListMutableStateFlow
    ) { pointOfInterestList,
        chosenPictureList,
        estateTypeList ->
        Triple(
            first = pointOfInterestList,
            second = chosenPictureList,
            third = estateTypeList
        )
    }
    
    private var agentSelectionDeadLock = true
    private var isSoldDeadLock = true
    
    init {
        viewModelScope.launch {
            val estateToEditId = savedStateHandle.get<Int>(ARG_TO_EDIT_ESTATE_ID) ?: 1
            val isEditMode = savedStateHandle.get<Boolean>(ARG_IS_EDIT_MODE) ?: false
            val estateWithPictureEntity = getEstateWithPictureEntityById.invoke(estateToEditId)
            
            if (isEditMode) {
                initOnEditMode(estateToEditId, estateWithPictureEntity)
            } else {
                initOnCreationMode()
            }
        }
    }
    
    val viewState = liveData {
        combine(
            getRealEstateAgentEntitiesUseCase.invoke(),
            selectedAgentMutableStateFlow,
            combineFlowOfIsSoldEntryDateAndSoldDate,
            combineFlowOfPointOfInterestListChosenPictureEstateType,
            selectedEstateTypeIdMutableStateFlow
        ) { realEstateAgentEntities,
            selectedAgent,
            combineFlowOfIsSoldEntryDateAndSoldDate,
            combineFlowOfPointOfInterestListChosenPictureEstateType,
            estateTypeId ->
            
            val estateToEditId = savedStateHandle.get<Int>(ARG_TO_EDIT_ESTATE_ID) ?: 1
            val isEditMode = savedStateHandle.get<Boolean>(ARG_IS_EDIT_MODE) ?: false
            
            emit(
                if (isEditMode) {
                    provideViewStateForEditionMode(
                        realEstateAgentEntities = realEstateAgentEntities,
                        chosenPictureEntities = combineFlowOfPointOfInterestListChosenPictureEstateType.second,
                        realId = selectedAgent,
                        pointOfInterestList = combineFlowOfPointOfInterestListChosenPictureEstateType.first,
                        combinedFlow = combineFlowOfIsSoldEntryDateAndSoldDate,
                        toEditEstatePictureEntity = getEstateWithPictureEntityById.invoke(
                            estateToEditId
                        ),
                        estateTypeEntities = combineFlowOfPointOfInterestListChosenPictureEstateType.third,
                        estateTypeId = estateTypeId
                    )
                } else {
                    provideViewStateForCreationMode(
                        realEstateAgentEntities = realEstateAgentEntities,
                        chosenPictureEntities = combineFlowOfPointOfInterestListChosenPictureEstateType.second,
                        selectedAgent = selectedAgent,
                        pointOfInterestList = combineFlowOfPointOfInterestListChosenPictureEstateType.first,
                        combinedFlow = combineFlowOfIsSoldEntryDateAndSoldDate,
                        estateTypeEntities = combineFlowOfPointOfInterestListChosenPictureEstateType.third,
                        estateTypeId = estateTypeId
                    )
                }
            )
        }.collect()
    }
    
    private fun provideViewStateForEditionMode(
        realEstateAgentEntities: List<RealEstateAgentEntity>,
        chosenPictureEntities: List<EstatePictureEntity>,
        realId: Int,
        pointOfInterestList: List<PointOfInterestEntity>,
        combinedFlow: Triple<Boolean, String, String>,
        toEditEstatePictureEntity: EstateWithPictureEntity,
        estateTypeEntities: List<EstateTypeEntity>,
        estateTypeId: Int
    ): EstateFormViewState =
        EstateFormViewState(
            agentViewStateItems = realEstateAgentEntities.map {
                EstateFormAgentViewStateItems(
                    id = it.id,
                    image = it.imageResource,
                    name = it.name,
                    it.id == realId
                )
            },
            pictureViewStateItems = chosenPictureEntities.map {
                PictureViewStateItems(
                    bitmap = it.imagePath,
                    type = it.description.name,
                    id = it.id,
                    true
                )
            },
            pointOfInterestViewStateItems = pointOfInterestList.map {
                PointOfInterestViewStateItems(
                    it.pointOfInterestEnum,
                    it.id,
                    it.isSelected
                )
            },
            surface = surface.ifEmpty {
                "${toEditEstatePictureEntity.estateEntity.surface}"
            },
            description = description.ifEmpty { toEditEstatePictureEntity.estateEntity.description },
            roomNumber = roomNumber.ifEmpty { "${toEditEstatePictureEntity.estateEntity.roomNumber}" },
            bathRoomNumber = bathroomNumber.ifEmpty { "${toEditEstatePictureEntity.estateEntity.bathroomNumber}" },
            bedRoomNumber = bedroomNumber.ifEmpty { "${toEditEstatePictureEntity.estateEntity.numberOfBedrooms}" },
            address = address.ifEmpty { toEditEstatePictureEntity.estateEntity.address },
            city = city.ifEmpty { toEditEstatePictureEntity.estateEntity.city },
            price = price.ifEmpty { toEditEstatePictureEntity.estateEntity.price },
            isSoldEstate = combinedFlow.first,
            entryDate = if (combinedFlow.second == application.getString(R.string.click_to_pick_a_date)) {
                toEditEstatePictureEntity.estateEntity.entryDate.format(
                    DateTimeFormatter.ofPattern("dd/MM/yyyy")
                ) ?: application.getString(R.string.click_to_pick_a_date)
            } else {
                combinedFlow.second
            },
            sellingDate = if (combinedFlow.third == application.getString(R.string.click_to_pick_a_date)) {
                toEditEstatePictureEntity.estateEntity.sellingDate?.format(
                    DateTimeFormatter.ofPattern("dd/MM/yyyy")
                ) ?: application.getString(R.string.click_to_pick_a_date)
            } else {
                combinedFlow.third
            },
            titleRes = R.string.edit_property,
            buttonTextRes = R.string.modify_button,
            estateTypeViewStateItems = estateTypeEntities.map {
                EstateTypeViewStateItems(
                    it.id,
                    it.estateTypeEnum,
                    it.id == estateTypeId,
                    false
                )
            }
        )
    
    private fun provideViewStateForCreationMode(
        realEstateAgentEntities: List<RealEstateAgentEntity>,
        chosenPictureEntities: List<EstatePictureEntity>,
        selectedAgent: Int,
        pointOfInterestList: List<PointOfInterestEntity>,
        combinedFlow: Triple<Boolean, String, String>,
        estateTypeEntities: List<EstateTypeEntity>,
        estateTypeId: Int
    ): EstateFormViewState =
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
                    id = it.id,
                    false
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
            sellingDate = combinedFlow.third,
            titleRes = R.string.create_new_property,
            buttonTextRes = com.despaircorp.shared.R.string.create,
            estateTypeViewStateItems = estateTypeEntities.map {
                EstateTypeViewStateItems(
                    it.id,
                    it.estateTypeEnum,
                    it.id == estateTypeId,
                    false
                )
            }
        )
    
    private suspend fun initOnEditMode(
        estateToEditId: Int,
        estateWithPictureEntity: EstateWithPictureEntity
    ) {
        initEstateStatusMutableFlowForEdition(estateWithPictureEntity = estateWithPictureEntity)
        initEstateTypeIdFlowForEdition(estateWithPictureEntity = estateWithPictureEntity)
        initEstateTypeFlowForEdition()
        initDateFlowsForEdition(estateWithPictureEntity = estateWithPictureEntity)
        initPointOfInterestFlowForEdition(estateWithPictureEntity = estateWithPictureEntity)
        initPictureFlowForEdition(estateWithPictureEntity = estateWithPictureEntity)
        initAgentFlowForEdition(estateToEditId = estateToEditId)
    }
    
    private fun initEstateStatusMutableFlowForEdition(estateWithPictureEntity: EstateWithPictureEntity) {
        if (isSoldDeadLock) {
            isSoldDeadLock = false
            isEstateSoldMutableStateFlow.value =
                estateWithPictureEntity.estateEntity.status != EstateStatus.FOR_SALE
        }
    }
    
    private fun initEstateTypeIdFlowForEdition(estateWithPictureEntity: EstateWithPictureEntity) {
        selectedEstateTypeIdMutableStateFlow.value =
            estateWithPictureEntity.estateEntity.estateType.id
    }
    
    private fun initEstateTypeFlowForEdition() {
        val estateTypeList = mutableListOf<EstateTypeEntity>()
        EstateTypeEnum.entries.forEach {
            estateTypeList.add(
                EstateTypeEntity(
                    isSelected = false,
                    estateTypeEnum = it,
                    id = it.id,
                )
            )
        }
        
        estateTypeListMutableStateFlow.update {
            it + estateTypeList
        }
    }
    
    private fun initDateFlowsForEdition(estateWithPictureEntity: EstateWithPictureEntity) {
        selectedEntryDateMutableStateFlow.value =
            estateWithPictureEntity.estateEntity.entryDate.format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
            )
        selectedSoldDateMutableStateFlow.value =
            estateWithPictureEntity.estateEntity.sellingDate?.format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
            ) ?: application.getString(R.string.click_to_pick_a_date)
    }
    
    private fun initPointOfInterestFlowForEdition(estateWithPictureEntity: EstateWithPictureEntity) {
        
        val pointOfInterestListFromEnumEntries = mutableListOf<PointOfInterestEntity>()
        PointOfInterestEnum.entries.forEach { pointOfInterestEnum ->
            pointOfInterestListFromEnumEntries.add(
                PointOfInterestEntity(
                    isSelected = false,
                    pointOfInterestEnum = pointOfInterestEnum,
                    id = pointOfInterestEnum.id
                )
            )
        }
        
        val associatedMapOfPointOfInterestSelected =
            estateWithPictureEntity.estateEntity.pointOfInterest.associateBy { it.id }
        
        val newListWithSelectedPointOfInterest = mutableListOf<PointOfInterestEntity>()
        newListWithSelectedPointOfInterest.clear()
        
        pointOfInterestListFromEnumEntries.forEach {
            if (associatedMapOfPointOfInterestSelected[it.id] != null) {
                val listFromUser = associatedMapOfPointOfInterestSelected[it.id]
                newListWithSelectedPointOfInterest.add(
                    PointOfInterestEntity(
                        isSelected = true,
                        pointOfInterestEnum = listFromUser?.pointOfInterestEnum
                            ?: PointOfInterestEnum.PARK,
                        id = listFromUser?.id ?: 0
                    )
                )
            } else {
                newListWithSelectedPointOfInterest.add(
                    PointOfInterestEntity(
                        isSelected = false,
                        pointOfInterestEnum = it.pointOfInterestEnum,
                        id = it.id
                    )
                )
            }
        }
        
        pointOfInterestListMutableStateFlow.update {
            it + newListWithSelectedPointOfInterest
        }
    }
    
    private fun initPictureFlowForEdition(estateWithPictureEntity: EstateWithPictureEntity) {
        chosenPictureMutableStateFlow.update {
            it + estateWithPictureEntity.pictures
        }
    }
    
    private suspend fun initAgentFlowForEdition(estateToEditId: Int) {
        if (agentSelectionDeadLock) {
            agentSelectionDeadLock = false
            selectedAgentMutableStateFlow.value =
                getEstateWithPictureEntityById.invoke(estateToEditId).estateEntity.agentInChargeId
        }
    }
    
    private fun initOnCreationMode() {
        initDateMutableStateFlowsForCreation()
        initPointOfInterestFlowForCreation()
        initEstateTypeFlowForCreation()
    }
    
    private fun initDateMutableStateFlowsForCreation() {
        selectedEntryDateMutableStateFlow.value =
            application.getString(R.string.click_to_pick_a_date)
        selectedSoldDateMutableStateFlow.value =
            application.getString(R.string.click_to_pick_a_date)
    }
    
    private fun initEstateTypeFlowForCreation() {
        val estateTypeList = mutableListOf<EstateTypeEntity>()
        EstateTypeEnum.entries.forEach {
            estateTypeList.add(
                EstateTypeEntity(
                    isSelected = false,
                    estateTypeEnum = it,
                    id = it.id,
                )
            )
        }
        estateTypeListMutableStateFlow.update {
            it + estateTypeList
        }
    }
    
    private fun initPointOfInterestFlowForCreation() {
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
    
    fun onRemovePointOfInterest(
        id: Int,
    ) {
        pointOfInterestListMutableStateFlow.value = pointOfInterestListMutableStateFlow.value.map {
            if (it.id == id) {
                PointOfInterestEntity(
                    isSelected = false,
                    pointOfInterestEnum = it.pointOfInterestEnum,
                    id = it.id
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
                    isSelected = true,
                    pointOfInterestEnum = it.pointOfInterestEnum,
                    id = it.id
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
    
    fun onAddEstateType(id: Int) {
        selectedEstateTypeIdMutableStateFlow.value = id
    }
    
    fun onRemoveImage(id: Int) {
        chosenPictureMutableStateFlow.value =
            chosenPictureMutableStateFlow.value.filter { it.id != id }
    }
    
    fun onCreateEstateClicked() {
        viewModelScope.launch {
            val estateToEditId = savedStateHandle.get<Int>(ARG_TO_EDIT_ESTATE_ID) ?: 1
            val isEditMode = savedStateHandle.get<Boolean>(ARG_IS_EDIT_MODE) ?: false
            
            val creationResult = estateFormValidationUseCase.invoke(
                estateSurface = surface,
                estateDescription = description,
                estateRoomNumber = roomNumber,
                estateBedroomNumber = bedroomNumber,
                estateBathRoomNumber = bathroomNumber,
                estateAddress = address,
                estateCity = city,
                estatePrice = price,
                estateType = EstateTypeEnum.entries[selectedEstateTypeIdMutableStateFlow.value],
                estatePointOfInterests = pointOfInterestListMutableStateFlow.value,
                isEstateSold = isEstateSoldMutableStateFlow.value,
                estateEntryDate = selectedEntryDateMutableStateFlow.value,
                estateSoldDate = selectedSoldDateMutableStateFlow.value,
                estatePictures = chosenPictureMutableStateFlow.value,
                estateInChargeAgentId = selectedAgentMutableStateFlow.value,
                estateToEditId = estateToEditId,
                isEditMode = isEditMode
            )
            
            val result = creationResult.getOrElse {
                it.message
            }
            
            viewActionMutableLiveData.value = if (result is Boolean) {
                if (isEditMode) {
                    Event(EstateFormAction.Success(R.string.modification_success))
                } else {
                    Event(EstateFormAction.Success(R.string.creation_success))
                }
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