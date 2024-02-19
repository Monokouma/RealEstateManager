package com.despaircorp.data.real_estate_agent.workers

import android.app.Application
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.data.R
import com.despaircorp.data.utils.TestCoroutineRule
import com.despaircorp.domain.real_estate_agent.RealEstateAgentDomainRepository
import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity
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
        
        every { application.applicationContext.getString(R.string.darius) } returns "darius"
        every { application.applicationContext.getString(R.string.cassio) } returns "cassio"
        every { application.applicationContext.getString(R.string.lucian) } returns "lucian"
        every { application.applicationContext.getString(R.string.bard) } returns "bard"
        every { application.applicationContext.getString(R.string.nilah) } returns "nilah"
        
        coJustRun {
            realEstateAgentDomainRepository.insertRealEstateAgentEntities(
                listOf(
                    RealEstateAgentEntity(
                        name = application.applicationContext.getString(R.string.darius),
                        id = 1,
                        imageResource = R.drawable.gamer,
                        isLoggedIn = false
                    ),
                    RealEstateAgentEntity(
                        name = application.applicationContext.getString(R.string.cassio),
                        id = 2,
                        imageResource = R.drawable.bartender,
                        isLoggedIn = false
                    ),
                    RealEstateAgentEntity(
                        name = application.applicationContext.getString(R.string.lucian),
                        id = 3,
                        imageResource = R.drawable.ice_skating,
                        isLoggedIn = false
                    ),
                    RealEstateAgentEntity(
                        name = application.applicationContext.getString(R.string.bard),
                        id = 4,
                        imageResource = R.drawable.old_man,
                        isLoggedIn = false
                    ),
                    RealEstateAgentEntity(
                        name = application.applicationContext.getString(R.string.nilah),
                        id = 5,
                        imageResource = R.drawable.young_man,
                        isLoggedIn = false
                    )
                )
            )
        }
        
        realEstateAgentInitWorker = RealEstateAgentInitWorker(
            application.applicationContext,
            workerParameters,
            realEstateAgentDomainRepository
        )
    }
    
    @Test
    fun `nominal case - table not exist`() = testCoroutineRule.runTest {
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