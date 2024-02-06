package com.despaircorp.domain.real_estate_agent

import javax.inject.Inject

class LogChosenAgentUseCase @Inject constructor(
    private val realEstateAgentDomainRepository: RealEstateAgentDomainRepository
) {
    suspend fun invoke(agentId: Int) = realEstateAgentDomainRepository.logChosenAgent(agentId)
}