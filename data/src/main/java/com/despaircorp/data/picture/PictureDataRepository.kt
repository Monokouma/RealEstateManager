package com.despaircorp.data.picture

import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.despaircorp.data.picture.dao.PictureDao
import com.despaircorp.data.picture.worker.PictureInitWorker
import com.despaircorp.data.utils.CoroutineDispatcherProvider
import com.despaircorp.data.utils.EntitiesMaperinator
import com.despaircorp.domain.picture.PictureDomainRepository
import com.despaircorp.domain.picture.model.EstatePicture
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PictureDataRepository @Inject constructor(
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val pictureDao: PictureDao,
    private val workManager: WorkManager,
    private val entitiesMaperinator: EntitiesMaperinator,
) : PictureDomainRepository {
    override fun enqueueInitPictureWorker() {
        val pictureInitWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<PictureInitWorker>()
                .build()
        
        workManager.enqueue(pictureInitWorkRequest)
    }
    
    override suspend fun exist(): Boolean = withContext(coroutineDispatcherProvider.io) {
        pictureDao.exist()
    }
    
    override suspend fun populatePictureTable(listOf: List<EstatePicture>): Unit =
        withContext(coroutineDispatcherProvider.io) {
            pictureDao.insert(entitiesMaperinator.mapPictureEntitiesToDto(listOf))
        }
}