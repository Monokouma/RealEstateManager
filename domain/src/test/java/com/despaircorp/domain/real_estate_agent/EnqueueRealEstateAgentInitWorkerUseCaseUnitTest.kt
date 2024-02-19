package com.despaircorp.domain.real_estate_agent

import com.despaircorp.domain.utils.TestCoroutineRule
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EnqueueRealEstateAgentInitWorkerUseCaseUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val realEstateAgentDomainRepository: RealEstateAgentDomainRepository = mockk()
    
    private val useCase = EnqueueRealEstateAgentInitWorkerUseCase(
        realEstateAgentDomainRepository
    )
    
    @Before
    fun setup() {
        coJustRun { realEstateAgentDomainRepository.enqueueRealEstateAgentInitWorker() }
    }
    
    @Test
    fun `nominal case - enqueue worker`() = testCoroutineRule.runTest {
        useCase.invoke()
        
        coVerify {
            realEstateAgentDomainRepository.enqueueRealEstateAgentInitWorker()
        }
        
        confirmVerified(realEstateAgentDomainRepository)
    }
}