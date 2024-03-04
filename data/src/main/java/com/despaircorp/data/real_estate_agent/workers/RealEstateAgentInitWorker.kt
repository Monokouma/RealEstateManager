package com.despaircorp.data.real_estate_agent.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.despaircorp.data.utils.EntitiesMaperinator.mapDefaultAgentEnumToRealEstateAgentEntity
import com.despaircorp.domain.real_estate_agent.RealEstateAgentDomainRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class RealEstateAgentInitWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val realEstateAgentDomainRepository: RealEstateAgentDomainRepository,
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        
        if (!realEstateAgentDomainRepository.isRealEstateAgentTableExist()) {
            realEstateAgentDomainRepository.insertRealEstateAgentEntities(
                mapDefaultAgentEnumToRealEstateAgentEntity(DefaultAgentEnum.entries)
            )
        }
        return Result.success()
    }
}