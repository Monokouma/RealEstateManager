package com.despaircorp.data.real_estate_agent

import com.despaircorp.data.real_estate_agent.dao.RealEstateAgentDao
import com.despaircorp.data.real_estate_agent.dto.RealEstateAgentDto
import com.despaircorp.domain.real_estate_agent.RealEstateAgentDomainRepository
import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity
import com.despaircorp.domain.utils.CoroutineDispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RealEstateAgentDataRepository @Inject constructor(
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val realEstateAgentDao: RealEstateAgentDao
) : RealEstateAgentDomainRepository {
    override suspend fun getRealEstateAgentEntities(): List<RealEstateAgentEntity> =
        withContext(coroutineDispatcherProvider.io) {
            realEstateAgentDao.getAllRealEstateAgentDto().map {
                RealEstateAgentEntity(
                    name = it.name,
                    id = it.id,
                    imageResource = it.imageResource
                )
            }
        }
    
    override suspend fun insertRealEstateAgentEntities(realEstateAgentEntities: List<RealEstateAgentEntity>) =
        withContext(coroutineDispatcherProvider.io) {
            realEstateAgentDao.insert(realEstateAgentEntities.map {
                RealEstateAgentDto(
                    id = it.id,
                    name = it.name,
                    imageResource = it.imageResource
                )
            })
        }
    
    override suspend fun isRealEstateAgentTableExist(): Boolean =
        withContext(coroutineDispatcherProvider.io) {
            realEstateAgentDao.exist()
        }
    
}