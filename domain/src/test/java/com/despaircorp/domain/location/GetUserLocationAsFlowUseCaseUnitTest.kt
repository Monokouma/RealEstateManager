package com.despaircorp.domain.location

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.domain.utils.TestCoroutineRule
import com.despaircorp.stubs.EntityProvidinator.provideLocationEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetUserLocationAsFlowUseCaseUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val locationDomainRepository: LocationDomainRepository = mockk()
    
    private val useCase = GetUserLocationAsFlowUseCase(
        locationDomainRepository
    )
    
    @Before
    fun setup() {
        coEvery { locationDomainRepository.getUserLocationAsFlow() } returns flowOf(
            provideLocationEntity()
        )
    }
    
    @Test
    fun `nominal case - get entity`() = testCoroutineRule.runTest {
        useCase.invoke().test {
            val result = awaitItem()
            awaitComplete()
            
            assertThat(result).isEqualTo(provideLocationEntity())
            
            coVerify {
                locationDomainRepository.getUserLocationAsFlow()
            }
            
            confirmVerified(locationDomainRepository)
        }
    }
}