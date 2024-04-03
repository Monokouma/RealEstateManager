package com.despaircorp.domain.estate

import com.despaircorp.domain.utils.TestCoroutineRule
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EnqueueEstateWorkerUseCaseUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val estateDomainRepository: EstateDomainRepository = mockk()
    
    private val useCase = EnqueueEstateWorkerUseCase(
        estateDomainRepository
    )
    
    @Before
    fun setup() {
        coJustRun { estateDomainRepository.enqueueEstateWorker() }
    }
    
    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        useCase.invoke()
        
        coVerify {
            estateDomainRepository.enqueueEstateWorker()
        }
        
        confirmVerified(estateDomainRepository)
    }
}