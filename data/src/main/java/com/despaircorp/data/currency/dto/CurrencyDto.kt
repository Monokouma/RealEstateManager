package com.despaircorp.data.currency.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.despaircorp.domain.currency.model.CurrencyEnum

@Entity("currency_table")
data class CurrencyDto(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val currencyEnum: CurrencyEnum?
)