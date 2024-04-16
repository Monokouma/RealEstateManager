package com.despaircorp.data.estate.dto

import android.content.ContentValues
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.despaircorp.domain.estate.model.EstateStatus
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity(
    tableName = "estate_table"
)
data class EstateDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String,
    val surface: String,
    val roomNumber: Int,
    val bathroomNumber: Int,
    val numberOfBedrooms: Int,
    val location: LatLng?,
    val estateType: String,
    val price: String,
    val pointOfInterest: List<String>,
    val sellingDate: LocalDate?,
    val entryDate: LocalDate,
    val status: EstateStatus,
    val city: String,
    val address: String,
    val agentInChargeId: Int
) {
    companion object {
        fun fromContentValues(values: ContentValues?): EstateDto {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            
            return EstateDto(
                id = values?.getAsInteger("id") ?: 0,
                description = values?.getAsString("description") ?: "",
                surface = values?.getAsString("surface") ?: "",
                roomNumber = values?.getAsInteger("roomNumber") ?: 0,
                bathroomNumber = values?.getAsInteger("bathroomNumber") ?: 0,
                numberOfBedrooms = values?.getAsInteger("numberOfBedrooms") ?: 0,
                location = values?.getAsString("location")
                    ?.let { Gson().fromJson(it, LatLng::class.java) },
                estateType = values?.getAsString("estateType") ?: "",
                price = values?.getAsString("price") ?: "",
                pointOfInterest = values?.getAsString("pointOfInterest")
                    ?.let { Gson().fromJson(it, object : TypeToken<List<String>>() {}.type) }
                    ?: listOf(),
                sellingDate = values?.getAsString("sellingDate")
                    ?.let { LocalDate.parse(it, formatter) },
                entryDate = values?.getAsString("entryDate")?.let { LocalDate.parse(it, formatter) }
                    ?: LocalDate.now(),
                status = EstateStatus.valueOf(
                    values?.getAsString("status") ?: EstateStatus.FOR_SALE.name
                ),
                city = values?.getAsString("city") ?: "",
                address = values?.getAsString("address") ?: "",
                agentInChargeId = values?.getAsInteger("agentInChargeId") ?: 0
            )
        }
    }
}