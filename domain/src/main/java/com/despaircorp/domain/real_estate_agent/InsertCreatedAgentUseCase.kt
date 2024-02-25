package com.despaircorp.domain.real_estate_agent

import com.despaircorp.domain.real_estate_agent.model.CreatedAgentEntity
import javax.inject.Inject

class InsertCreatedAgentUseCase @Inject constructor(
    private val realEstateAgentDomainRepository: RealEstateAgentDomainRepository
) {
    suspend fun invoke(agentName: String, agentPictureRes: Int): Boolean =
        realEstateAgentDomainRepository.insertCreatedAgent(
            CreatedAgentEntity(
                agentName,
                agentPictureRes
            )
        ) >= 1L
}