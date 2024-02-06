package com.despaircorp.domain.real_estate_agent

import javax.inject.Inject

class DisconnectAgentUseCase @Inject constructor(
    private val realEstateAgentDomainRepository: RealEstateAgentDomainRepository
) {
    suspend fun invoke(agentId: Int): Boolean = realEstateAgentDomainRepository.disconnect(agentId) >= 1
}