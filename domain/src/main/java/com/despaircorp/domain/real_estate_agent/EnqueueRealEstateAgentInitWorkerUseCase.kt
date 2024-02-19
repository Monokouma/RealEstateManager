package com.despaircorp.domain.real_estate_agent

import javax.inject.Inject

class EnqueueRealEstateAgentInitWorkerUseCase @Inject constructor(
    private val realEstateAgentDomainRepository: RealEstateAgentDomainRepository
) {
    fun invoke() {
        realEstateAgentDomainRepository.enqueueRealEstateAgentInitWorker()
    }
}