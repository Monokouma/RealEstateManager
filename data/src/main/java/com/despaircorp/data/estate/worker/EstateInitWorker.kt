package com.despaircorp.data.estate.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.despaircorp.domain.estate.EstateDomainRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class EstateInitWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val estateDomainRepository: EstateDomainRepository,
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        if (!estateDomainRepository.isTableExisting()) {
        
        }
        return Result.success()
    }
}