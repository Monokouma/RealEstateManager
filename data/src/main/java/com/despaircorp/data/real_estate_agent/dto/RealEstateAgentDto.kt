package com.despaircorp.data.real_estate_agent.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "real_estate_agent")
data class RealEstateAgentDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val imageResource: Int,
    val isLoggedIn: Boolean
)