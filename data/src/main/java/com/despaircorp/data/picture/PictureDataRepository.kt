package com.despaircorp.data.picture

import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.despaircorp.data.picture.dao.PictureDao
import com.despaircorp.data.picture.worker.PictureInitWorker
import com.despaircorp.data.utils.CoroutineDispatcherProvider
import com.despaircorp.domain.picture.PictureDomainRepository
import javax.inject.Inject

class PictureDataRepository @Inject constructor(
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val pictureDao: PictureDao,
    private val workManager: WorkManager
) : PictureDomainRepository {
    override fun enqueueInitPictureWorker() {
        val pictureInitWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<PictureInitWorker>()
                .build()
        
        workManager.enqueue(pictureInitWorkRequest)
    }
    
}