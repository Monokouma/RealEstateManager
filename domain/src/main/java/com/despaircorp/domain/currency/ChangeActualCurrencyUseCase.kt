package com.despaircorp.domain.currency

import com.despaircorp.domain.currency.model.CurrencyEntity
import com.despaircorp.domain.currency.model.CurrencyEnum
import javax.inject.Inject

class ChangeActualCurrencyUseCase @Inject constructor(
    private val currencyDomainRepository: CurrencyDomainRepository
) {
    suspend fun invoke() {
        val actualCurrency = currencyDomainRepository.getActualCurrencyEntity()
        if (actualCurrency.currencyEnum == CurrencyEnum.US_DOLLAR) {
            currencyDomainRepository.insertCurrencyEntity(CurrencyEntity(CurrencyEnum.EURO))
        } else {
            currencyDomainRepository.insertCurrencyEntity(CurrencyEntity(CurrencyEnum.US_DOLLAR))
        }
    }
}