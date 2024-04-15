package com.despaircorp.domain.currency

import com.despaircorp.domain.utils.TestCoroutineRule
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EnqueueCurrencyWorkerUseCaseUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val currencyDomainRepository: CurrencyDomainRepository = mockk()
    
    private val useCase = EnqueueCurrencyWorkerUseCase(
        currencyDomainRepository
    )
    
    @Before
    fun setup() {
        coJustRun { currencyDomainRepository.enqueueCurrencyWorker() }
    }
    
    @Test
    fun `nominal case - enqueue worker`() = testCoroutineRule.runTest {
        useCase.invoke()
        
        coVerify {
            currencyDomainRepository.enqueueCurrencyWorker()
        }
        
        confirmVerified(currencyDomainRepository)
    }
}