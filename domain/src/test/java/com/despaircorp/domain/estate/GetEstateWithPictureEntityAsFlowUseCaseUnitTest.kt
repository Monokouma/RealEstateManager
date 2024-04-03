package com.despaircorp.domain.estate

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.domain.utils.TestCoroutineRule
import com.despaircorp.stubs.EntityProvidinator.provideEstateWithPictureEntities
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetEstateWithPictureEntityAsFlowUseCaseUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    
    private val estateDomainRepository: EstateDomainRepository = mockk()
    
    private val useCase = GetEstateWithPictureEntityAsFlowUseCase(
        estateDomainRepository
    )
    
    @Before
    fun setup() {
        coEvery {
            estateDomainRepository.getEstateWithPictureEntitiesAsFlow()
        } returns flowOf(provideEstateWithPictureEntities())
    }
    
    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        useCase.invoke().test {
            val result = awaitItem()
            awaitComplete()
            
            assertThat(result).isEqualTo(provideEstateWithPictureEntities())
            
            coVerify {
                estateDomainRepository.getEstateWithPictureEntitiesAsFlow()
            }
            
            confirmVerified(estateDomainRepository)
        }
    }
}