package com.despaircorp.domain.real_estate_agent

import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity

interface RealEstateAgentDomainRepository {
    suspend fun getRealEstateAgentEntities(): List<RealEstateAgentEntity>
    
    suspend fun insertRealEstateAgentEntities(realEstateAgentEntities: List<RealEstateAgentEntity>)
    
    suspend fun isRealEstateAgentTableExist(): Boolean
}