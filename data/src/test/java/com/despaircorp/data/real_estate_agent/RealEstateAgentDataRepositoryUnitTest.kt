package com.despaircorp.data.real_estate_agent

import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.despaircorp.data.real_estate_agent.dao.RealEstateAgentDao
import com.despaircorp.data.utils.EntitiesMaperinator
import com.despaircorp.data.utils.TestCoroutineRule
import com.despaircorp.stubs.EntityProvidinator.provideLoggedRealEstateAgentDto
import com.despaircorp.stubs.EntityProvidinator.provideLoggedRealEstateAgentEntity
import com.despaircorp.stubs.EntityProvidinator.provideRealEstateAgentEntities
import com.despaircorp.stubs.EntityProvidinator.provideRealEstateDtoEntities
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RealEstateAgentDataRepositoryUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val realEstateAgentDao: RealEstateAgentDao = mockk()
    private val entitiesMaperinator: EntitiesMaperinator = mockk()
    private val workManager: WorkManager = mockk()
    
    private lateinit var repository: RealEstateAgentDataRepository
    
    companion object {
        private const val DEFAULT_AGENT_ID = 1
    }
    
    @Before
    fun setup() {
        
        coEvery {
            realEstateAgentDao.getAllRealEstateAgentDto()
        } returns provideRealEstateDtoEntities()
        
        coEvery {
            entitiesMaperinator.mapRealEstateAgentDtoToRealEstateAgentEntities(
                provideRealEstateDtoEntities()
            )
        } returns provideRealEstateAgentEntities()
        
        coJustRun {
            realEstateAgentDao.insert(provideRealEstateDtoEntities())
        }
        
        coEvery {
            entitiesMaperinator.mapRealEstateAgentEntitiesToRealEstateAgentDto(
                provideRealEstateAgentEntities()
            )
        } returns provideRealEstateDtoEntities()
        
        coEvery {
            realEstateAgentDao.exist()
        } returns true
        
        coEvery {
            realEstateAgentDao.getLoggedRealEstateAgentEntity()
        } returns provideLoggedRealEstateAgentDto()
        
        coEvery {
            entitiesMaperinator.mapRealEstateAgentDtoToEntity(provideLoggedRealEstateAgentDto())
        } returns provideLoggedRealEstateAgentEntity()
        
        coEvery {
            realEstateAgentDao.logChosenAgent(DEFAULT_AGENT_ID)
        } returns 1
        
        coEvery {
            realEstateAgentDao.disconnect(DEFAULT_AGENT_ID)
        } returns 1
        
        coEvery {
            realEstateAgentDao.isAgentLoggedOn()
        } returns true
        
        coEvery {
            workManager.enqueue(any<OneTimeWorkRequest>())
        } returns mockk()
        
        repository = RealEstateAgentDataRepository(
            coroutineDispatcherProvider = testCoroutineRule.getTestCoroutineDispatcherProvider(),
            realEstateAgentDao = realEstateAgentDao,
            entitiesMaperinator = entitiesMaperinator,
            workManager = workManager
        )
    }
    
    @Test
    fun `nominal case - get real estate agent entities`() = testCoroutineRule.runTest {
        val result = repository.getRealEstateAgentEntities()
        
        assertThat(result).isEqualTo(
            provideRealEstateAgentEntities()
        )
        
        coVerify {
            realEstateAgentDao.getAllRealEstateAgentDto()
        }
        confirmVerified(
            realEstateAgentDao
        )
    }
    
    @Test
    fun `nominal case - insert real estate agent entities`() = testCoroutineRule.runTest {
        repository.insertRealEstateAgentEntities(provideRealEstateAgentEntities())
        
        coVerify {
            realEstateAgentDao.insert(provideRealEstateDtoEntities())
        }
        
        confirmVerified(realEstateAgentDao)
    }
    
    @Test
    fun `nominal case - real estate agent table exist return true`() = testCoroutineRule.runTest {
        val result = repository.isRealEstateAgentTableExist()
        
        assertThat(result).isTrue()
        
        coVerify {
            realEstateAgentDao.exist()
        }
        
        confirmVerified(
            realEstateAgentDao
        )
    }
    
    @Test
    fun `nominal case - real estate agent table exist return false`() = testCoroutineRule.runTest {
        coEvery {
            realEstateAgentDao.exist()
        } returns false
        
        val result = repository.isRealEstateAgentTableExist()
        
        assertThat(result).isFalse()
        
        coVerify {
            realEstateAgentDao.exist()
        }
        
        confirmVerified(
            realEstateAgentDao
        )
    }
    
    @Test
    fun `nominal case - get logged real estate agent entities`() = testCoroutineRule.runTest {
        val result = repository.getLoggedRealEstateAgentEntity()
        
        assertThat(result).isEqualTo(provideLoggedRealEstateAgentEntity())
        
        coVerify {
            realEstateAgentDao.getLoggedRealEstateAgentEntity()
        }
        
        confirmVerified(
            realEstateAgentDao
        )
    }
    
    @Test
    fun `nominal case - log chosen agent`() = testCoroutineRule.runTest {
        val result = repository.logChosenAgent(DEFAULT_AGENT_ID)
        
        assertThat(result).isEqualTo(1)
        
        coVerify {
            realEstateAgentDao.logChosenAgent(DEFAULT_AGENT_ID)
        }
        
        confirmVerified(realEstateAgentDao)
    }
    
    @Test
    fun `nominal case - disconnect`() = testCoroutineRule.runTest {
        val result = repository.disconnect(DEFAULT_AGENT_ID)
        
        assertThat(result).isEqualTo(1)
        
        coVerify {
            realEstateAgentDao.disconnect(DEFAULT_AGENT_ID)
        }
        confirmVerified(
            realEstateAgentDao
        )
    }
    
    @Test
    fun `nominal case - agent currently log return true`() = testCoroutineRule.runTest {
        val result = repository.isUserCurrentlyLogged()
        
        assertThat(result).isTrue()
        
        coVerify {
            realEstateAgentDao.isAgentLoggedOn()
        }
        
        confirmVerified(realEstateAgentDao)
    }
    
    @Test
    fun `nominal case - agent currently log return false`() = testCoroutineRule.runTest {
        coEvery {
            realEstateAgentDao.isAgentLoggedOn()
        } returns false
        
        val result = repository.isUserCurrentlyLogged()
        
        assertThat(result).isFalse()
        
        coVerify {
            realEstateAgentDao.isAgentLoggedOn()
        }
        
        confirmVerified(realEstateAgentDao)
    }
    
    @Test
    fun `nominal case - enqueue init real estate agent entity worker`() =
        testCoroutineRule.runTest {
            repository.enqueueRealEstateAgentInitWorker()
            
            coVerify {
                workManager.enqueue(any<OneTimeWorkRequest>())
            }
            
            confirmVerified(workManager)
        }
    
    
}