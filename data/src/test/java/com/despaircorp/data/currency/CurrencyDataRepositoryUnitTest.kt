package com.despaircorp.data.currency

import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.despaircorp.data.currency.dao.CurrencyDao
import com.despaircorp.data.utils.EntitiesMaperinator
import com.despaircorp.data.utils.TestCoroutineRule
import com.despaircorp.stubs.EntityProvidinator.provideCurrencyDto
import com.despaircorp.stubs.EntityProvidinator.provideCurrencyEntity
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CurrencyDataRepositoryUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val currencyDao: CurrencyDao = mockk()
    private val entitiesMaperinator: EntitiesMaperinator = mockk()
    private val workManager: WorkManager = mockk()
    
    private lateinit var repository: CurrencyDataRepository
    
    @Before
    fun setup() {
        coEvery { currencyDao.exist() } returns true
        
        coEvery { workManager.enqueue(any<OneTimeWorkRequest>()) } returns mockk()
        
        coEvery {
            currencyDao.getCurrencyDtoAsFlow()
        } returns flowOf(provideCurrencyDto())
        
        coEvery { entitiesMaperinator.mapCurrencyDtoToCurrencyEntityAsFlow(currencyDao.getCurrencyDtoAsFlow()) } returns flowOf(
            provideCurrencyEntity()
        )
        
        coEvery { currencyDao.getCurrencyDto() } returns provideCurrencyDto()
        
        coEvery { entitiesMaperinator.mapCurrencyDtoToCurrencyEntity(provideCurrencyDto()) } returns provideCurrencyEntity()
        
        coEvery { entitiesMaperinator.mapCurrencyEntityToCurrencyDto(provideCurrencyEntity()) } returns provideCurrencyDto()
        
        coJustRun { currencyDao.insert(provideCurrencyDto()) }
        
        repository = CurrencyDataRepository(
            coroutineDispatcherProvider = testCoroutineRule.getTestCoroutineDispatcherProvider(),
            currencyDao = currencyDao,
            entitiesMaperinator = entitiesMaperinator,
            workManager = workManager
        )
    }
    
    @Test
    fun `nominal case - is table exist should return true`() = testCoroutineRule.runTest {
        val result = repository.isCurrencyTableExist()
        
        assertThat(result).isTrue()
        
        coVerify {
            currencyDao.exist()
        }
        
        confirmVerified(currencyDao)
    }
    
    @Test
    fun `nominal case - enqueue worker`() = testCoroutineRule.runTest {
        repository.enqueueCurrencyWorker()
        
        coVerify {
            workManager.enqueue(any<OneTimeWorkRequest>())
        }
        
        confirmVerified(workManager)
    }
    
    @Test
    fun `nominal case - get actual currency as flow`() = testCoroutineRule.runTest {
        repository.getActualCurrencyAsFlow().test {
            val result = awaitItem()
            awaitComplete()
            
            assertThat(result).isEqualTo(provideCurrencyEntity())
            
            coVerify {
                currencyDao.getCurrencyDtoAsFlow()
                entitiesMaperinator.mapCurrencyDtoToCurrencyEntityAsFlow(currencyDao.getCurrencyDtoAsFlow())
            }
            
            confirmVerified(currencyDao, entitiesMaperinator)
        }
    }
    
    @Test
    fun `nominal case - get actual currency entity`() = testCoroutineRule.runTest {
        val result = repository.getActualCurrencyEntity()
        
        assertThat(result).isEqualTo(provideCurrencyEntity())
        
        coVerify {
            currencyDao.getCurrencyDto()
            entitiesMaperinator.mapCurrencyDtoToCurrencyEntity(provideCurrencyDto())
        }
        
        confirmVerified(
            currencyDao, entitiesMaperinator
        )
    }
    
    @Test
    fun `nominal case - insert currency entity`() = testCoroutineRule.runTest {
        repository.insertCurrencyEntity(provideCurrencyEntity())
        
        coVerify {
            currencyDao.insert(provideCurrencyDto())
        }
        
        confirmVerified(currencyDao)
    }
}