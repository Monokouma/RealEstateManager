package com.despaircorp.domain.estate

import javax.inject.Inject

class EnqueueEstateWorkerUseCase @Inject constructor(
    private val estateDomainRepository: EstateDomainRepository
) {
    fun invoke() {
        estateDomainRepository.enqueueEstateWorker()
    }
}