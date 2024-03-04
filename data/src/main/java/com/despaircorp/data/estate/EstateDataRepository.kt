package com.despaircorp.data.estate

import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.despaircorp.data.estate.dao.EstateDao
import com.despaircorp.data.estate.worker.EstateInitWorker
import com.despaircorp.data.utils.CoroutineDispatcherProvider
import com.despaircorp.domain.estate.EstateDomainRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EstateDataRepository @Inject constructor(
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val estateDao: EstateDao,
    private val workManager: WorkManager
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
    
}