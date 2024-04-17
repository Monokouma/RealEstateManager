package com.despaircorp.domain.estate

import com.despaircorp.domain.estate.model.EstateEntity
import com.despaircorp.domain.estate.model.EstateWithPictureEntity
import kotlinx.coroutines.flow.Flow

interface EstateDomainRepository {
    suspend fun isTableExisting(): Boolean
    fun enqueueEstateWorker()
    
    fun getEstateWithPictureEntitiesAsFlow(): Flow<List<EstateWithPictureEntity>>
    suspend fun prePopulateEstateTable(estateEntities: List<EstateEntity>)
    suspend fun getEstateWithPictureEntityById(estateId: Int): EstateWithPictureEntity
    suspend fun insertNewEstate(estateEntity: EstateEntity)
    suspend fun getEstateEntities(): List<EstateEntity>
    suspend fun updateEstate(estateEntity: EstateEntity): Int
}