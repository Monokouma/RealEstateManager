package com.despaircorp.data.real_estate_agent

import com.despaircorp.data.real_estate_agent.dao.RealEstateAgentDao
import com.despaircorp.data.utils.EntitiesMaperinator
import com.despaircorp.domain.real_estate_agent.RealEstateAgentDomainRepository
import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity
import com.despaircorp.domain.utils.CoroutineDispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RealEstateAgentDataRepository @Inject constructor(
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val realEstateAgentDao: RealEstateAgentDao,
    private val entitiesMaperinator: EntitiesMaperinator
) : RealEstateAgentDomainRepository {
    override suspend fun getRealEstateAgentEntities(): List<RealEstateAgentEntity> =
        withContext(coroutineDispatcherProvider.io) {
            entitiesMaperinator.mapRealEstateAgentDtoToRealEstateAgentEntities(realEstateAgentDao.getAllRealEstateAgentDto())
        }
    
    override suspend fun insertRealEstateAgentEntities(realEstateAgentEntities: List<RealEstateAgentEntity>) =
        withContext(coroutineDispatcherProvider.io) {
            realEstateAgentDao.insert(
                entitiesMaperinator.mapRealEstateAgentEntitiesToRealEstateAgentDto(
                    realEstateAgentEntities
                )
            )
        }
    
    override suspend fun isRealEstateAgentTableExist(): Boolean =
        withContext(coroutineDispatcherProvider.io) {
            realEstateAgentDao.exist()
        }
    
    override suspend fun getLoggedRealEstateAgentEntity(): RealEstateAgentEntity =
        withContext(coroutineDispatcherProvider.io) {
            entitiesMaperinator.mapRealEstateAgentDtoToEntity(realEstateAgentDao.getLoggedRealEstateAgentEntity())
        }
    
    override suspend fun logChosenAgent(agentId: Int) =
        withContext(coroutineDispatcherProvider.io) {
            realEstateAgentDao.logChosenAgent(agentId)
        }
    
    override suspend fun disconnect(agentId: Int): Int =
        withContext(coroutineDispatcherProvider.io) {
            realEstateAgentDao.disconnect(agentId)
        }
    
    override suspend fun isUserCurrentlyLogged(): Boolean =
        withContext(coroutineDispatcherProvider.io) {
            realEstateAgentDao.isAgentLoggedOn()
        }
}