package com.despaircorp.ui.main.master_fragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.despaircorp.domain.connectivity.IsUserConnectedToInternetUseCase
import com.despaircorp.domain.currency.GetActualCurrencyUseCase
import com.despaircorp.domain.currency.model.CurrencyEnum
import com.despaircorp.domain.estate.GetEstateWithPictureEntityAsFlowUseCase
import com.despaircorp.domain.estate.model.EstateStatus
import com.despaircorp.domain.estate.model.EstateTypeEntity
import com.despaircorp.domain.estate.model.PointOfInterestEntity
import com.despaircorp.domain.geocoder.GetAddressFromLatLngUseCase
import com.despaircorp.ui.R
import com.despaircorp.ui.main.master_fragment.estate.EstateViewStateItems
import com.despaircorp.ui.main.master_fragment.filter.FilterTypeEnum
import com.despaircorp.ui.utils.getEuroFromDollar
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MasterViewModel @Inject constructor(
    private val getEstateWithPictureEntityAsFlowUseCase: GetEstateWithPictureEntityAsFlowUseCase,
    private val getAddressFromLatLngUseCase: GetAddressFromLatLngUseCase,
    private val application: Application,
    private val getActualCurrencyUseCase: GetActualCurrencyUseCase,
    private val isUserConnectedToInternetUseCase: IsUserConnectedToInternetUseCase
) : ViewModel() {
    
    private val selectedItem = MutableStateFlow(1)
    
    private val pointOfInterestEntitiesMutableStateFlow =
        MutableStateFlow(emptyList<PointOfInterestEntity>())
    private val estateTypeListMutableStateFlow =
        MutableStateFlow(emptyList<EstateTypeEntity>())
    
    private val rawQueryMutableStateFlow =
        MutableStateFlow("SELECT * FROM estate_table")
    private val toBuildQueryMutableStateFlow = MutableStateFlow(
        (mutableMapOf<String, String>(
            "surfaceMin" to "",
            "surfaceMax" to "",
            "priceMin" to "",
            "priceMax" to "",
            "roomNumber" to "",
            "forSale" to "",
            "sold" to "",
            "entryDate" to "",
        ))
    )
    
    
    val viewState = liveData<MasterViewState> {
        
        rawQueryMutableStateFlow.collectLatest { rawQuery ->
            Log.i("Monokouma", rawQuery)
            combine(
                selectedItem,
                getActualCurrencyUseCase.invoke(),
                isUserConnectedToInternetUseCase.invoke(),
                getEstateWithPictureEntityAsFlowUseCase.invoke(SimpleSQLiteQuery(rawQuery))
            ) { selectedItem, actualCurrency, isConnectedToInternet, estateEntities ->
                
                emit(
                    MasterViewState(
                        estateEntities.map {
                            EstateViewStateItems(
                                id = it.estateEntity.id,
                                picture = it.pictures.first().imagePath,
                                city = if (isConnectedToInternet) {
                                    getAddressFromLatLngUseCase.invoke(
                                        it.estateEntity.location ?: LatLng(0.0, 0.0)
                                    )["city"]
                                        ?: it.estateEntity.city
                                } else {
                                    it.estateEntity.city
                                },
                                type = application.getString(it.estateEntity.estateType.textRes),
                                price = if (actualCurrency.currencyEnum == CurrencyEnum.EURO) {
                                    StringBuilder().append(
                                        formatToCorrectNumber(
                                            CurrencyEnum.EURO,
                                            getEuroFromDollar(it.estateEntity.price)
                                        )
                                    
                                    )
                                        .append(application.getString(actualCurrency.currencyEnum.symbolResource))
                                        .toString()
                                } else {
                                    StringBuilder()
                                        .append(application.getString(actualCurrency.currencyEnum.symbolResource))
                                        .append(
                                            formatToCorrectNumber(
                                                CurrencyEnum.US_DOLLAR,
                                                it.estateEntity.price
                                            )
                                        )
                                        .toString()
                                },
                                isSelected = if (application.resources.getBoolean(R.bool.isLandscape)) {
                                    it.estateEntity.id == selectedItem
                                } else {
                                    false
                                }
                            )
                        }
                    )
                )
                
            }.collect()
        }
        
    }
    
    private fun formatToCorrectNumber(forCurrencyEnum: CurrencyEnum, price: String): String {
        val format =
            NumberFormat.getNumberInstance(if (forCurrencyEnum == CurrencyEnum.US_DOLLAR) Locale.US else Locale.FRANCE)
        return format.format(price.toInt())
    }
    
    fun onEstateClicked(estateId: Int) {
        selectedItem.value = estateId
    }
    
    fun onFilterChanged(filterArg: String, filterBy: FilterTypeEnum) {
        //React with enum etc
        Log.i("Monokouma", filterBy.toString())
        when (filterBy) {
            FilterTypeEnum.SURFACE_MIN -> {
                
                val currentMap = toBuildQueryMutableStateFlow.value.toMutableMap()
                if (filterArg.isEmpty()) {
                    currentMap["surfaceMin"] = ""
                    toBuildQueryMutableStateFlow.value = currentMap
                } else {
                    currentMap["surfaceMin"] = filterArg
                    toBuildQueryMutableStateFlow.value = currentMap
                }
            }
            
            FilterTypeEnum.SURFACE_MAX -> {
                
                val currentMap = toBuildQueryMutableStateFlow.value.toMutableMap()
                if (filterArg.isEmpty()) {
                    currentMap["surfaceMax"] = ""
                    toBuildQueryMutableStateFlow.value = currentMap
                } else {
                    currentMap["surfaceMax"] = filterArg
                    toBuildQueryMutableStateFlow.value = currentMap
                }
            }
            
            FilterTypeEnum.PRICE_MIN -> {
                val currentMap = toBuildQueryMutableStateFlow.value.toMutableMap()
                if (filterArg.isEmpty()) {
                    currentMap["priceMin"] = ""
                    toBuildQueryMutableStateFlow.value = currentMap
                } else {
                    currentMap["priceMin"] = filterArg
                    toBuildQueryMutableStateFlow.value = currentMap
                }
            }
            
            FilterTypeEnum.PRICE_MAX -> {
                val currentMap = toBuildQueryMutableStateFlow.value.toMutableMap()
                if (filterArg.isEmpty()) {
                    currentMap["priceMax"] = ""
                    toBuildQueryMutableStateFlow.value = currentMap
                } else {
                    currentMap["priceMax"] = filterArg
                    toBuildQueryMutableStateFlow.value = currentMap
                }
            }
            
            FilterTypeEnum.ROOM_NUMBER -> {
                val currentMap = toBuildQueryMutableStateFlow.value.toMutableMap()
                if (filterArg == "roomNumber=''") {
                    currentMap["roomNumber"] = ""
                    toBuildQueryMutableStateFlow.value = currentMap
                } else {
                    currentMap["roomNumber"] = filterArg
                    toBuildQueryMutableStateFlow.value = currentMap
                }
            }
            
            FilterTypeEnum.FOR_SALE -> {
                val currentMap = toBuildQueryMutableStateFlow.value.toMutableMap()
                currentMap["sold"] = ""
                currentMap["forSale"] = filterArg
                toBuildQueryMutableStateFlow.value = currentMap
            }
            
            FilterTypeEnum.SOLD -> {
                val currentMap = toBuildQueryMutableStateFlow.value.toMutableMap()
                currentMap["sold"] = filterArg
                currentMap["forSale"] = ""
                toBuildQueryMutableStateFlow.value = currentMap
            }
            
            FilterTypeEnum.ENTRY_DATE -> {
                val currentMap = toBuildQueryMutableStateFlow.value.toMutableMap()
                currentMap["entryDate"] = filterArg
                toBuildQueryMutableStateFlow.value = currentMap
            }
            
            FilterTypeEnum.POINT_OF_INTEREST -> {
                TODO()
            }
            
            FilterTypeEnum.ESTATE_TYPE -> {
                TODO()
            }
        }
        
    }
    
    fun onApplyFilter() {
        val queryMap = toBuildQueryMutableStateFlow.value
        
        val sqlQuery = buildEstateSearchQuery(
            surfaceMin = queryMap["surfaceMin"],
            surfaceMax = queryMap["surfaceMax"],
            priceMin = queryMap["priceMin"],
            priceMax = queryMap["priceMax"],
            roomNumber = queryMap["roomNumber"],
            forSale = queryMap["forSale"],
            sold = queryMap["sold"],
            entryDate = queryMap["entryDate"],
            estateType = estateTypeListMutableStateFlow.value,
            pointsOfInterest = pointOfInterestEntitiesMutableStateFlow.value
        )
        rawQueryMutableStateFlow.value = sqlQuery
        Log.i("Monokouma", sqlQuery)
    }
    
    private fun buildEstateSearchQuery(
        surfaceMin: String?,
        surfaceMax: String?,
        priceMin: String?,
        priceMax: String?,
        roomNumber: String?,
        forSale: String?,
        sold: String?,
        entryDate: String?,
        estateType: List<EstateTypeEntity>?, // Assuming this is a single value, you might need to adjust if multiple types can be selected
        pointsOfInterest: List<PointOfInterestEntity>? // List of selected points of interest like schools, parks, etc.
    ): String {
        
        val queryBuilder = StringBuilder("SELECT * FROM estate_table WHERE 1=1")
        
        // Surface conditions
        surfaceMin?.takeIf { it.isNotBlank() }?.also { queryBuilder.append(" AND surface >= $it") }
        surfaceMax?.takeIf { it.isNotBlank() }?.also { queryBuilder.append(" AND surface <= $it") }
        
        // Price conditions
        when {
            !priceMin.isNullOrBlank() && !priceMax.isNullOrBlank() -> {
                queryBuilder.append(" AND price BETWEEN ${priceMin.toInt()} AND ${priceMax.toInt()}")
            }
            
            !priceMin.isNullOrBlank() -> {
                queryBuilder.append(" AND price >= ${priceMin.toInt()}")
            }
            
            !priceMax.isNullOrBlank() -> {
                queryBuilder.append(" AND price <= ${priceMax.toInt()}")
            }
        }
        
        
        // Room number condition
        roomNumber?.takeIf { it.isNotBlank() }
            ?.also { queryBuilder.append(" AND roomNumber = $it") }
        
        // Sale status conditions
        forSale?.takeIf { it.isNotBlank() }
            ?.also { queryBuilder.append(" AND status = '${EstateStatus.FOR_SALE.name}'") }
        sold?.takeIf { it.isNotBlank() }
            ?.also { queryBuilder.append(" AND status = '${EstateStatus.SOLD.name}'") }
        
        // Entry date condition
        entryDate?.takeIf { it.isNotBlank() }
            ?.also { queryBuilder.append(" AND entryDate = '$it'") }
        
        // Points of interest conditions
        if (pointsOfInterest?.isNotEmpty() == true) {
            val poiConditions =
                pointsOfInterest.joinToString(separator = " OR ") { "pointOfInterest LIKE '%${it.pointOfInterestEnum.name}%'" }
            queryBuilder.append(" AND ($poiConditions)")
        }
        Log.i("Monokouma", estateType.toString())
        if (estateType?.isNotEmpty() == true) {
            val estateTypeConditions =
                estateType.joinToString(separator = " OR ") { "estateType LIKE '%${it.estateTypeEnum.name}%'" }
            queryBuilder.append(" AND ($estateTypeConditions)")
        }
        
        return queryBuilder.toString()
    }
    
    fun onResetFilter() {
        rawQueryMutableStateFlow.value = "SELECT * FROM estate_table"
        toBuildQueryMutableStateFlow.value = mutableMapOf(
            "surfaceMin" to "",
            "surfaceMax" to "",
            "priceMin" to "",
            "priceMax" to "",
            "roomNumber" to "",
            "forSale" to "",
            "sold" to "",
            "entryDate" to "",
        )
    }
    
    fun onPointOfInterestForFilteringChanged(pointOfInterestEntities: List<PointOfInterestEntity>) {
        pointOfInterestEntitiesMutableStateFlow.value =
            pointOfInterestEntities.filter { it.isSelected }
    }
    
    fun onEstateTypeForFilteringChanged(estateTypeEntities: List<EstateTypeEntity>) {
        estateTypeListMutableStateFlow.value =
            estateTypeEntities.filter { it.isSelected }
    }
}