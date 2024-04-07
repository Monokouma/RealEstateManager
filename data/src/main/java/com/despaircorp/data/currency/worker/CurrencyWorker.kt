package com.despaircorp.data.currency.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.despaircorp.domain.currency.CurrencyDomainRepository
import com.despaircorp.domain.currency.model.CurrencyEntity
import com.despaircorp.domain.currency.model.CurrencyEnum
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class CurrencyWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val currencyDomainRepository: CurrencyDomainRepository,
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        
        if (!currencyDomainRepository.isCurrencyTableExist()) {
            currencyDomainRepository.insertCurrencyEntity(
                CurrencyEntity(
                    CurrencyEnum.US_DOLLAR
                )
            )
        }
        return Result.success()
    }
}