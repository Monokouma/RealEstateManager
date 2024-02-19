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

class IsAgentCurrentlyLoggedInUseCaseUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val realEstateAgentDomainRepository: RealEstateAgentDomainRepository = mockk()
    
    private val useCase = IsAgentCurrentlyLoggedInUseCase(
        realEstateAgentDomainRepository
    )
    
    companion object {
        private const val DEFAULT_NOT_LOGGED_IN = false
        private const val DEFAULT_LOGGED_IN = true
    }
    
    @Before
    fun setup() {
        coEvery { realEstateAgentDomainRepository.isUserCurrentlyLogged() } returns DEFAULT_LOGGED_IN
    }
    
    @Test
    fun `nominal case - user currently logged return true`() = testCoroutineRule.runTest {
        val result = useCase.invoke()
        
        assertThat(result).isTrue()
        
        coVerify {
            realEstateAgentDomainRepository.isUserCurrentlyLogged()
        }
        
        confirmVerified(realEstateAgentDomainRepository)
    }
    
    @Test
    fun `edge case - user currently logged return false`() = testCoroutineRule.runTest {
        coEvery { realEstateAgentDomainRepository.isUserCurrentlyLogged() } returns DEFAULT_NOT_LOGGED_IN
        
        val result = useCase.invoke()
        
        assertThat(result).isFalse()
        
        coVerify {
            realEstateAgentDomainRepository.isUserCurrentlyLogged()
        }
        
        confirmVerified(realEstateAgentDomainRepository)
    }
}