package com.despaircorp.domain.real_estate_agent

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.despaircorp.domain.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DisconnectAgentUseCaseUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val realEstateAgentDomainRepository: RealEstateAgentDomainRepository = mockk()
    
    private val useCase = DisconnectAgentUseCase(
        realEstateAgentDomainRepository
    )
    
    companion object {
        private const val DEFAULT_AGENT_ID = 1
    }
    
    @Before
    fun setup() {
        coEvery {
            realEstateAgentDomainRepository.disconnect(DEFAULT_AGENT_ID)
        } returns 1
    }
    
    @Test
    fun `nominal case - disconnect user`() = testCoroutineRule.runTest {
        val result = useCase.invoke(DEFAULT_AGENT_ID)
        
        assertThat(result).isTrue()
        
        coVerify {
            realEstateAgentDomainRepository.disconnect(DEFAULT_AGENT_ID)
        }
        
        confirmVerified(realEstateAgentDomainRepository)
    }
    
    @Test
    fun `error case - disconnect user`() = testCoroutineRule.runTest {
        coEvery {
            realEstateAgentDomainRepository.disconnect(DEFAULT_AGENT_ID)
        } returns 0
        
        val result = useCase.invoke(DEFAULT_AGENT_ID)
        
        assertThat(result).isFalse()
        
        coVerify {
            realEstateAgentDomainRepository.disconnect(DEFAULT_AGENT_ID)
        }
        
        confirmVerified(realEstateAgentDomainRepository)
    }
}