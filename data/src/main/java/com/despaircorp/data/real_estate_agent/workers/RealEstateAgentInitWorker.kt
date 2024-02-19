package com.despaircorp.data.real_estate_agent.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.despaircorp.data.R
import com.despaircorp.domain.real_estate_agent.RealEstateAgentDomainRepository
import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity
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
                listOf(
                    RealEstateAgentEntity(
                        name = applicationContext.getString(R.string.darius),
                        id = 1,
                        imageResource = R.drawable.gamer,
                        isLoggedIn = false
                    ),
                    RealEstateAgentEntity(
                        name = applicationContext.getString(R.string.cassio),
                        id = 2,
                        imageResource = R.drawable.bartender,
                        isLoggedIn = false
                    ),
                    RealEstateAgentEntity(
                        name = applicationContext.getString(R.string.lucian),
                        id = 3,
                        imageResource = R.drawable.ice_skating,
                        isLoggedIn = false
                    ),
                    RealEstateAgentEntity(
                        name = applicationContext.getString(R.string.bard),
                        id = 4,
                        imageResource = R.drawable.old_man,
                        isLoggedIn = false
                    ),
                    RealEstateAgentEntity(
                        name = applicationContext.getString(R.string.nilah),
                        id = 5,
                        imageResource = R.drawable.young_man,
                        isLoggedIn = false
                    )
                )
            )
        }
        return Result.success()
    }
}