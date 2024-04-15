package com.despaircorp.data.currency.workers

import android.app.Application
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.data.currency.worker.CurrencyWorker
import com.despaircorp.data.utils.TestCoroutineRule
import com.despaircorp.domain.currency.CurrencyDomainRepository
import com.despaircorp.stubs.EntityProvidinator.provideCurrencyEntity
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CurrencyWorkerUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val application: Application = mockk()
    
    private val workerParameters: WorkerParameters = mockk()
    
    private val currencyDomainRepository: CurrencyDomainRepository = mockk()
    
    private lateinit var worker: CurrencyWorker
    
    @Before
    fun setup() {
        every { application.applicationContext } returns mockk()
        every { workerParameters.taskExecutor.serialTaskExecutor } returns mockk()
        coEvery { currencyDomainRepository.isCurrencyTableExist() } returns false
        coJustRun { currencyDomainRepository.insertCurrencyEntity(provideCurrencyEntity()) }
        
        
        worker = CurrencyWorker(
            application.applicationContext,
            workerParameters,
            currencyDomainRepository
        )
    }
    
    @Test
    fun `nominal case - table not exist should return false`() = testCoroutineRule.runTest {
        val result = worker.doWork()
        
        assertThat(result).isEqualTo(ListenableWorker.Result.success())
        
        coVerify {
            currencyDomainRepository.isCurrencyTableExist()
            currencyDomainRepository.insertCurrencyEntity(provideCurrencyEntity())
        }
        
        confirmVerified(currencyDomainRepository)
    }
    
    @Test
    fun `edge case - table already exist`() = testCoroutineRule.runTest {
        coEvery { currencyDomainRepository.isCurrencyTableExist() } returns true
        
        val result = worker.doWork()
        
        assertThat(result).isEqualTo(ListenableWorker.Result.success())
        
        coVerify {
            currencyDomainRepository.isCurrencyTableExist()
        }
        
        confirmVerified(currencyDomainRepository)
    }
}