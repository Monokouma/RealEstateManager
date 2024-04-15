package com.despaircorp.domain.currency

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.domain.utils.TestCoroutineRule
import com.despaircorp.stubs.EntityProvidinator
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetActualCurrencyUseCaseUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val currencyDomainRepository: CurrencyDomainRepository = mockk()
    
    private val useCase = GetActualCurrencyUseCase(
        currencyDomainRepository
    )
    
    @Before
    fun setup() {
        coEvery { currencyDomainRepository.getActualCurrencyAsFlow() } returns flowOf(
            EntityProvidinator.provideCurrencyEntity()
        )
    }
    
    @Test
    fun `nominal case - get actual currency entity`() = testCoroutineRule.runTest {
        useCase.invoke().test {
            val result = awaitItem()
            awaitComplete()
            
            assertThat(result).isEqualTo(EntityProvidinator.provideCurrencyEntity())
            
            coVerify {
                currencyDomainRepository.getActualCurrencyAsFlow()
            }
            
            confirmVerified(currencyDomainRepository)
        }
    }
}