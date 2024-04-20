package com.despaircorp.ui.main.master_fragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.despaircorp.domain.connectivity.IsUserConnectedToInternetUseCase
import com.despaircorp.domain.currency.GetActualCurrencyUseCase
import com.despaircorp.domain.currency.model.CurrencyEnum
import com.despaircorp.domain.estate.GetEstateWithPictureEntityAsFlowUseCase
import com.despaircorp.domain.estate.model.EstateTypeEntity
import com.despaircorp.domain.estate.model.PointOfInterestEntity
import com.despaircorp.domain.filter.EstateSqlQueryBuilderUseCase
import com.despaircorp.domain.filter.FilterTypeEnum
import com.despaircorp.domain.filter.OnFilterChangedUseCase
import com.despaircorp.domain.geocoder.GetAddressFromLatLngUseCase
import com.despaircorp.ui.R
import com.despaircorp.ui.main.master_fragment.estate.EstateViewStateItems
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
    private val isUserConnectedToInternetUseCase: IsUserConnectedToInternetUseCase,
    private val onFilterChangedUseCase: OnFilterChangedUseCase,
    private val estateSqlQueryBuilderUseCase: EstateSqlQueryBuilderUseCase
) : ViewModel() {
    
    private val selectedItemMutableStateFlow = MutableStateFlow(1)
    
    private val pointOfInterestEntitiesMutableStateFlow =
        MutableStateFlow(emptyList<PointOfInterestEntity>())
    private val estateTypeListMutableStateFlow =
        MutableStateFlow(emptyList<EstateTypeEntity>())
    
    private val rawQueryMutableStateFlow =
        MutableStateFlow(BASE_SQL_QUERY)
    
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
            
            combine(
                selectedItemMutableStateFlow,
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
        /*
        val format =
            NumberFormat.getNumberInstance(if (forCurrencyEnum == CurrencyEnum.US_DOLLAR) Locale.US else Locale.FRANCE)
        
        return format.format(price.toInt())
        
         */
        
        val locale = if (forCurrencyEnum == CurrencyEnum.US_DOLLAR) Locale.US else Locale.FRANCE
        val format = NumberFormat.getNumberInstance(locale)
        
        // Parse the string to a number using the locale-specific format
        val number = format.parse(price) ?: throw IllegalArgumentException("Invalid number format")
        
        // Format the number back to a string using the same locale-specific format
        return format.format(number)
    }
    
    fun onEstateClicked(estateId: Int) {
        selectedItemMutableStateFlow.value = estateId
    }
    
    fun onFilterChanged(filterArg: String, filterBy: FilterTypeEnum) {
        //React with enum etc
        toBuildQueryMutableStateFlow.value =
            onFilterChangedUseCase.invoke(filterArg, filterBy, toBuildQueryMutableStateFlow.value)
    }
    
    fun onApplyFilter() {
        val queryMap = toBuildQueryMutableStateFlow.value
        
        val sqlQuery = estateSqlQueryBuilderUseCase.invoke(
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
    }
    
    fun onResetFilter() {
        rawQueryMutableStateFlow.value = BASE_SQL_QUERY
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
    
    companion object {
        private const val BASE_SQL_QUERY = "SELECT * FROM estate_table"
    }
}