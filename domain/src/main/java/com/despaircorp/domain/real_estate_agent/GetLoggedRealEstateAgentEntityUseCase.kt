package com.despaircorp.domain.real_estate_agent

import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity
import javax.inject.Inject

class GetLoggedRealEstateAgentEntityUseCase @Inject constructor(
    private val realEstateAgentDomainRepository: RealEstateAgentDomainRepository
) {
    suspend fun invoke(): RealEstateAgentEntity =
        realEstateAgentDomainRepository.getLoggedRealEstateAgentEntity()
}