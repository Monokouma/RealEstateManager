package com.despaircorp.data.estate.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.despaircorp.domain.estate.model.EstateStatus
import java.time.LocalDate

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
    val latitude: Double,
    val longitude: Double,
    val estateType: String,
    val price: String,
    val pointOfInterest: List<String>,
    val sellingDate: LocalDate?,
    val entryDate: LocalDate,
    val status: EstateStatus
)