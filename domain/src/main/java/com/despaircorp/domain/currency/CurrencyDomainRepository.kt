package com.despaircorp.domain.currency

import com.despaircorp.domain.currency.model.CurrencyEntity
import kotlinx.coroutines.flow.Flow

interface CurrencyDomainRepository {
    fun getActualCurrencyAsFlow(): Flow<CurrencyEntity>
    
    suspend fun getActualCurrencyEntity(): CurrencyEntity
    
    fun enqueueCurrencyWorker()
    suspend fun isCurrencyTableExist(): Boolean
    suspend fun insertCurrencyEntity(currencyEntity: CurrencyEntity)
}