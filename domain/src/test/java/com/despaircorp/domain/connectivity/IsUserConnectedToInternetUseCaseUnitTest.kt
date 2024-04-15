package com.despaircorp.domain.connectivity

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isTrue
import com.despaircorp.domain.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class IsUserConnectedToInternetUseCaseUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val connectivityDomainRepository: ConnectivityDomainRepository = mockk()
    
    private val useCase = IsUserConnectedToInternetUseCase(
        connectivityDomainRepository
    )
    
    @Before
    fun setup() {
        coEvery { connectivityDomainRepository.isConnectedToInternet() } returns flowOf(NetworkType.WIFI)
        
    }
    
    @Test
    fun `nominal case - should return true with wifi type`() = testCoroutineRule.runTest {
        useCase.invoke().test {
            val result = awaitItem()
            awaitComplete()
            
            assertThat(result).isTrue()
            
            coVerify {
                connectivityDomainRepository.isConnectedToInternet()
            }
            
            confirmVerified(connectivityDomainRepository)
        }
    }
    
    @Test
    fun `nominal case - should return true with cellular type`() = testCoroutineRule.runTest {
        useCase.invoke().test {
            coEvery { connectivityDomainRepository.isConnectedToInternet() } returns flowOf(
                NetworkType.CELLULAR
            )
            
            val result = awaitItem()
            awaitComplete()
            
            assertThat(result).isTrue()
            
            coVerify {
                connectivityDomainRepository.isConnectedToInternet()
            }
            
            confirmVerified(connectivityDomainRepository)
        }
    }
    
    @Test
    fun `nominal case - should return false with none type`() = testCoroutineRule.runTest {
        useCase.invoke().test {
            coEvery { connectivityDomainRepository.isConnectedToInternet() } returns flowOf(
                NetworkType.NONE
            )
            
            val result = awaitItem()
            awaitComplete()
            
            assertThat(result).isTrue()
            
            coVerify {
                connectivityDomainRepository.isConnectedToInternet()
            }
            
            confirmVerified(connectivityDomainRepository)
        }
    }
}