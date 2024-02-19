package com.despaircorp.domain.real_estate_agent

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.domain.utils.TestCoroutineRule
import com.despaircorp.stubs.EntityProvidinator.provideRealEstateAgentEntities
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetRealEstateAgentEntitiesUseCaseUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val realEstateAgentDomainRepository: RealEstateAgentDomainRepository = mockk()
    
    private val useCase = GetRealEstateAgentEntitiesUseCase(
        realEstateAgentDomainRepository
    )
    
    @Before
    fun setup() {
        coEvery { realEstateAgentDomainRepository.getRealEstateAgentEntitiesAsFlow() } returns flowOf(
            provideRealEstateAgentEntities()
        )
    }
    
    @Test
    fun `nominal case - get real estate agent entities as flow`() = testCoroutineRule.runTest {
        useCase.invoke().test {
            val result = awaitItem()
            awaitComplete()
            
            assertThat(result).isEqualTo(provideRealEstateAgentEntities())
            
            coVerify {
                realEstateAgentDomainRepository.getRealEstateAgentEntitiesAsFlow()
            }
            confirmVerified(realEstateAgentDomainRepository)
        }
    }
}