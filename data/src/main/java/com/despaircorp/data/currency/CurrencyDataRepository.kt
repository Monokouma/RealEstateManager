package com.despaircorp.data.currency

import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.despaircorp.data.currency.dao.CurrencyDao
import com.despaircorp.data.currency.worker.CurrencyWorker
import com.despaircorp.data.utils.CoroutineDispatcherProvider
import com.despaircorp.data.utils.EntitiesMaperinator
import com.despaircorp.domain.currency.CurrencyDomainRepository
import com.despaircorp.domain.currency.model.CurrencyEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrencyDataRepository @Inject constructor(
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val currencyDao: CurrencyDao,
    private val entitiesMaperinator: EntitiesMaperinator,
    private val workManager: WorkManager
) : CurrencyDomainRepository {
    override fun getActualCurrencyAsFlow(): Flow<CurrencyEntity> =
        entitiesMaperinator.mapCurrencyDtoToCurrencyEntityAsFlow(currencyDao.getCurrencyDtoAsFlow())
            .flowOn(coroutineDispatcherProvider.io)
    
    override suspend fun getActualCurrencyEntity(): CurrencyEntity =
        withContext(coroutineDispatcherProvider.io) {
            entitiesMaperinator.mapCurrencyDtoToCurrencyEntity(currencyDao.getCurrencyDto())
        }
    
    override fun enqueueCurrencyWorker() {
        val currencyInitWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<CurrencyWorker>()
                .build()
        
        workManager.enqueue(currencyInitWorkRequest)
    }
    
    override suspend fun isCurrencyTableExist(): Boolean =
        withContext(coroutineDispatcherProvider.io) {
            currencyDao.exist()
        }
    
    override suspend fun insertCurrencyEntity(currencyEntity: CurrencyEntity) =
        withContext(coroutineDispatcherProvider.io) {
            currencyDao.insert(entitiesMaperinator.mapCurrencyEntityToCurrencyDto(currencyEntity))
        }
    
    
}