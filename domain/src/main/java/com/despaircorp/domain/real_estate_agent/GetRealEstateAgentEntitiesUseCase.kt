package com.despaircorp.domain.real_estate_agent

import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRealEstateAgentEntitiesUseCase @Inject constructor(
    private val realEstateAgentDomainRepository: RealEstateAgentDomainRepository
) {
    suspend fun invoke(): Flow<List<RealEstateAgentEntity>> =
        realEstateAgentDomainRepository.getRealEstateAgentEntitiesAsFlow()
}