package com.despaircorp.domain.picture

import com.despaircorp.domain.utils.TestCoroutineRule
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EnqueuePictureWorkerUseCaseUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val pictureDomainRepository: PictureDomainRepository = mockk()
    
    private val useCase = EnqueuePictureWorkerUseCase(
        pictureDomainRepository
    )
    
    @Before
    fun setup() {
        coJustRun { pictureDomainRepository.enqueueInitPictureWorker() }
    }
    
    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        useCase.invoke()
        
        coVerify {
            pictureDomainRepository.enqueueInitPictureWorker()
        }
        
        confirmVerified(pictureDomainRepository)
    }
}