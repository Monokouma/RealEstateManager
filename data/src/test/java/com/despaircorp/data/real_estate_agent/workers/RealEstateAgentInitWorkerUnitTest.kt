package com.despaircorp.data.real_estate_agent.workers

import android.app.Application
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.data.utils.EntitiesMaperinator.mapDefaultAgentEnumToRealEstateAgentEntity
import com.despaircorp.data.utils.TestCoroutineRule
import com.despaircorp.domain.real_estate_agent.RealEstateAgentDomainRepository
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RealEstateAgentInitWorkerUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val realEstateAgentDomainRepository: RealEstateAgentDomainRepository = mockk()
    
    private val application: Application = mockk()
    
    private val workerParameters: WorkerParameters = mockk()
    
    private lateinit var realEstateAgentInitWorker: RealEstateAgentInitWorker
    
    @Before
    fun setup() {
        every { application.applicationContext } returns mockk()
        every { workerParameters.taskExecutor.serialTaskExecutor } returns mockk()
        
        coEvery { realEstateAgentDomainRepository.isRealEstateAgentTableExist() } returns true
        
        coJustRun {
            realEstateAgentDomainRepository.insertRealEstateAgentEntities(
                mapDefaultAgentEnumToRealEstateAgentEntity(DefaultAgentEnum.entries)
            )
        }
        
        realEstateAgentInitWorker = RealEstateAgentInitWorker(
            application.applicationContext,
            workerParameters,
            realEstateAgentDomainRepository
        )
    }
    
    @Test
    fun `nominal case - table not exist should insert entities`() = testCoroutineRule.runTest {
        coEvery { realEstateAgentDomainRepository.isRealEstateAgentTableExist() } returns false
        
        val result = realEstateAgentInitWorker.doWork()
        
        assertThat(result).isEqualTo(ListenableWorker.Result.success())
        
        
    }
    
    @Test
    fun `nominal case - table exist`() = testCoroutineRule.runTest {
        val result = realEstateAgentInitWorker.doWork()
        
        assertThat(result).isEqualTo(ListenableWorker.Result.success())
        
        coVerify {
            realEstateAgentDomainRepository.isRealEstateAgentTableExist()
        }
        
        confirmVerified(realEstateAgentDomainRepository)
    }
}