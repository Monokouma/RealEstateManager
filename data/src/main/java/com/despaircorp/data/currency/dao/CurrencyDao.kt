package com.despaircorp.data.currency.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.despaircorp.data.currency.dto.CurrencyDto
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    
    @Query("SELECT EXISTS(SELECT * FROM currency_table)")
    suspend fun exist(): Boolean
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currencyDto: CurrencyDto)
    
    @Query("SELECT * FROM currency_table")
    fun getCurrencyDtoAsFlow(): Flow<CurrencyDto>
    
    @Query("SELECT * FROM currency_table")
    fun getCurrencyDto(): CurrencyDto
}