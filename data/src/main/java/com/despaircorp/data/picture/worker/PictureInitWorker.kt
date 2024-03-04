package com.despaircorp.data.picture.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.despaircorp.domain.picture.PictureDomainRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class PictureInitWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val pictureDomainRepository: PictureDomainRepository
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        
        return Result.success()
    }
}