package com.despaircorp.data.estate.workers

import android.app.Application
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.data.estate.worker.EstateInitWorker
import com.despaircorp.data.utils.TestCoroutineRule
import com.despaircorp.domain.estate.EstateDomainRepository
import com.despaircorp.shared.R
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EstateInitWorkerUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val estateDomainRepository: EstateDomainRepository = mockk()
    
    private val application: Application = mockk()
    
    private val workerParameters: WorkerParameters = mockk()
    
    private lateinit var estateInitWorker: EstateInitWorker
    
    @Before
    fun setup() {
        coEvery { estateDomainRepository.isTableExisting() } returns false
        coJustRun { estateDomainRepository.prePopulateEstateTable(any()) }
        
        every { application.applicationContext } returns mockk()
        every { workerParameters.taskExecutor.serialTaskExecutor } returns mockk()
        
        every { application.applicationContext.getString(R.string.first_house_desc) } returns "desc"
        every { application.applicationContext.getString(R.string.house) } returns "house"
        every { application.applicationContext.getString(R.string.apartment) } returns "apartment"
        every { application.applicationContext.getString(R.string.schools) } returns "schools"
        every { application.applicationContext.getString(R.string.shops) } returns "shops"
        every { application.applicationContext.getString(R.string.parks) } returns "parks"
        every { application.applicationContext.getString(R.string.subway) } returns "subway"
        every { application.applicationContext.getString(R.string.second_house_desc) } returns "desc"
        every { application.applicationContext.getString(R.string.third_house_desc) } returns "desc"
        every { application.applicationContext.getString(R.string.fourth_house_desc) } returns "desc"
        every { application.applicationContext.getString(R.string.fifth_house_desc) } returns "desc"
        
        
        estateInitWorker = EstateInitWorker(
            application.applicationContext,
            workerParameters,
            estateDomainRepository
        )
    }
    
    @Test
    fun `nominal case - success`() = testCoroutineRule.runTest {
        val result = estateInitWorker.doWork()
        
        assertThat(result).isEqualTo(ListenableWorker.Result.success())
        
        coVerify {
            estateDomainRepository.isTableExisting()
            estateDomainRepository.prePopulateEstateTable(any())
        }
        
        confirmVerified(estateDomainRepository)
    }
    
    @Test
    fun `nominal case - table exist`() = testCoroutineRule.runTest {
        coEvery { estateDomainRepository.isTableExisting() } returns true
        
        val result = estateInitWorker.doWork()
        
        assertThat(result).isEqualTo(ListenableWorker.Result.success())
        
        coVerify {
            estateDomainRepository.isTableExisting()
        }
        
        confirmVerified(estateDomainRepository)
    }
}