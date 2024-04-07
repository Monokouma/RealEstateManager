package com.despaircorp.domain.currency

import com.despaircorp.domain.currency.model.CurrencyEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetActualCurrencyUseCase @Inject constructor(
    private val currencyDomainRepository: CurrencyDomainRepository
) {
    fun invoke(): Flow<CurrencyEntity> = currencyDomainRepository.getActualCurrencyAsFlow()
}