package com.despaircorp.domain.currency

import javax.inject.Inject

class EnqueueCurrencyWorkerUseCase @Inject constructor(
    private val currencyDomainRepository: CurrencyDomainRepository
) {
    fun invoke() {
        currencyDomainRepository.enqueueCurrencyWorker()
    }
}