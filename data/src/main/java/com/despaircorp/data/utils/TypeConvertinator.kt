package com.despaircorp.data.utils

import androidx.room.TypeConverter
import com.despaircorp.domain.currency.model.CurrencyEnum
import com.despaircorp.domain.estate.model.EstateTypeEnum
import com.despaircorp.domain.estate.model.PointOfInterestEntity
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.format.DateTimeFormatter


object TypeConvertinator {
    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    
    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? {
        return value?.format(formatter)
    }
    
    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let {
            return LocalDate.parse(it, formatter)
        }
    }
    
    @TypeConverter
    fun toCurrencyEnum(value: String): CurrencyEnum? {
        return CurrencyEnum.entries.find { it.name == value }
    }
    
    @TypeConverter
    fun fromCurrencyEnum(currencyEnum: CurrencyEnum?): String {
        return currencyEnum?.name ?: ""
    }
    
    @TypeConverter
    fun fromLatLng(latLng: LatLng?): String? {
        return latLng?.let { "${it.latitude},${it.longitude}" }
    }
    
    @TypeConverter
    fun toLatLng(data: String?): LatLng? {
        return data?.let {
            val pieces = it.split(",")
            if (pieces.size == 2) {
                LatLng(pieces[0].toDouble(), pieces[1].toDouble())
            } else {
                null
            }
        }
    }
    
    @TypeConverter
    fun fromPointOfInterestList(pointOfInterestList: List<PointOfInterestEntity>?): String? {
        if (pointOfInterestList == null) {
            return null
        }
        val type = object : TypeToken<List<PointOfInterestEntity>>() {}.type
        return Gson().toJson(pointOfInterestList, type)
    }
    
    @TypeConverter
    fun toPointOfInterestList(pointOfInterestListString: String?): List<PointOfInterestEntity>? {
        if (pointOfInterestListString == null) {
            return null
        }
        val type = object : TypeToken<List<PointOfInterestEntity>>() {}.type
        return Gson().fromJson(pointOfInterestListString, type)
    }
    
    @TypeConverter
    fun fromEstateTypeEnum(estateTypeEnum: EstateTypeEnum?): String? {
        return estateTypeEnum?.name
    }
    
    @TypeConverter
    fun toEstateTypeEnum(estateTypeEnumName: String?): EstateTypeEnum? {
        return if (estateTypeEnumName == null) null else EstateTypeEnum.valueOf(estateTypeEnumName)
    }
    
}