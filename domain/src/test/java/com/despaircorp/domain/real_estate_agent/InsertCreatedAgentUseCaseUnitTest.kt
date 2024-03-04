package com.despaircorp.domain.real_estate_agent

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.despaircorp.domain.utils.TestCoroutineRule
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_IMAGE_RESOURCE
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_NAME
import com.despaircorp.stubs.EntityProvidinator.provideCreatedAgentEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InsertCreatedAgentUseCaseUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val realEstateAgentDomainRepository: RealEstateAgentDomainRepository = mockk()
    
    private val useCase = InsertCreatedAgentUseCase(
        realEstateAgentDomainRepository
    )
    
    @Before
    fun setup() {
        coEvery { realEstateAgentDomainRepository.insertCreatedAgent(provideCreatedAgentEntity()) } returns 1L
    }
    
    @Test
    fun `nominal case - success`() = testCoroutineRule.runTest {
        val result = useCase.invoke(DEFAULT_NAME, DEFAULT_IMAGE_RESOURCE)
        
        assertThat(result).isTrue()
        
        coVerify {
            realEstateAgentDomainRepository.insertCreatedAgent(provideCreatedAgentEntity())
        }
        
        confirmVerified(realEstateAgentDomainRepository)
    }
    
    @Test
    fun `nominal case - failure`() = testCoroutineRule.runTest {
        coEvery { realEstateAgentDomainRepository.insertCreatedAgent(provideCreatedAgentEntity()) } returns 0L
        
        val result = useCase.invoke(DEFAULT_NAME, DEFAULT_IMAGE_RESOURCE)
        
        assertThat(result).isFalse()
        
        coVerify {
            realEstateAgentDomainRepository.insertCreatedAgent(provideCreatedAgentEntity())
        }
        
        confirmVerified(realEstateAgentDomainRepository)
    }
}