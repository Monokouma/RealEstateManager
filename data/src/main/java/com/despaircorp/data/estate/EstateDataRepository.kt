package com.despaircorp.data.estate

import androidx.sqlite.db.SupportSQLiteQuery
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.despaircorp.data.estate.dao.EstateDao
import com.despaircorp.data.estate.worker.EstateInitWorker
import com.despaircorp.data.utils.CoroutineDispatcherProvider
import com.despaircorp.data.utils.EntitiesMaperinator
import com.despaircorp.domain.estate.EstateDomainRepository
import com.despaircorp.domain.estate.model.EstateEntity
import com.despaircorp.domain.estate.model.EstateWithPictureEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EstateDataRepository @Inject constructor(
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val estateDao: EstateDao,
    private val workManager: WorkManager,
    private val entitiesMaperinator: EntitiesMaperinator,
) : EstateDomainRepository {
    override suspend fun isTableExisting(): Boolean = withContext(coroutineDispatcherProvider.io) {
        estateDao.exist()
    }
    
    override fun enqueueEstateWorker() {
        val estateInitWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<EstateInitWorker>()
                .build()
        
        workManager.enqueue(estateInitWorkRequest)
    }
    
    override fun getEstateWithPictureEntitiesAsFlow(query: SupportSQLiteQuery): Flow<List<EstateWithPictureEntity>> =
        entitiesMaperinator
            .mapEstateWithPictureDtoToEntities(
                estateDao.getEstateWithPictureAsFlow(query)
            ).flowOn(coroutineDispatcherProvider.io)
    
    override suspend fun prePopulateEstateTable(estateEntities: List<EstateEntity>) =
        withContext(coroutineDispatcherProvider.io) {
            estateDao.insertAsList(entitiesMaperinator.mapEstateEntitiesToDto(estateEntities))
        }
    
    override suspend fun getEstateWithPictureEntityById(estateId: Int): EstateWithPictureEntity =
        withContext(coroutineDispatcherProvider.io) {
            entitiesMaperinator.mapEstateWithPictureDtoToEstateWithPictureEntity(
                estateDao.getEstateWithPictureDtoById(
                    estateId
                )
            )
        }
    
    override suspend fun insertNewEstate(estateEntity: EstateEntity) =
        withContext(coroutineDispatcherProvider.io) {
            estateDao.insert(entitiesMaperinator.mapEstateEntityToDto(estateEntity))
        }
    
    override suspend fun getEstateEntities(): List<EstateEntity> =
        withContext(coroutineDispatcherProvider.io) {
            entitiesMaperinator.mapEstateDtoToEstateEntity(estateDao.getEstateDto())
        }
    
    override suspend fun updateEstate(estateEntity: EstateEntity): Int =
        withContext(coroutineDispatcherProvider.io) {
            estateDao.updateEstate(entitiesMaperinator.mapEstateEntityToDto(estateEntity))
        }
}