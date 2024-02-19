package com.despaircorp.domain.real_estate_agent

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.domain.utils.TestCoroutineRule
import com.despaircorp.stubs.EntityProvidinator.provideLoggedRealEstateAgentEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetLoggedRealEstateAgentEntityUseCaseUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val realEstateAgentDomainRepository: RealEstateAgentDomainRepository = mockk()
    
    private val useCase = GetLoggedRealEstateAgentEntityUseCase(
        realEstateAgentDomainRepository
    )
    
    @Before
    fun setup() {
        coEvery { realEstateAgentDomainRepository.getLoggedRealEstateAgentEntity() } returns provideLoggedRealEstateAgentEntity()
    }
    
    @Test
    fun `nominal case - get logged real estate agent entity`() = testCoroutineRule.runTest {
        val result = useCase.invoke()
        
        assertThat(result).isEqualTo(provideLoggedRealEstateAgentEntity())
        
        coVerify {
            realEstateAgentDomainRepository.getLoggedRealEstateAgentEntity()
        }
        
        confirmVerified(realEstateAgentDomainRepository)
    }
}