package com.despaircorp.domain.estate

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.domain.utils.TestCoroutineRule
import com.despaircorp.stubs.EntityProvidinator
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ID
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetEstateWithPictureEntityByIdUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    
    private val estateDomainRepository: EstateDomainRepository = mockk()
    
    private val useCase = GetEstateWithPictureEntityByIdUseCase(
        estateDomainRepository
    )
    
    @Before
    fun setup() {
        coEvery { estateDomainRepository.getEstateWithPictureEntityById(DEFAULT_ID) } returns EntityProvidinator.provideEstateWithPictureEntity()
        
    }
    
    @Test
    fun `nominal case - get estate with picture entity`() = testCoroutineRule.runTest {
        val result = useCase.invoke(DEFAULT_ID)
        
        assertThat(result).isEqualTo(EntityProvidinator.provideEstateWithPictureEntity())
        
        coVerify {
            estateDomainRepository.getEstateWithPictureEntityById(DEFAULT_ID)
        }
        
        confirmVerified(estateDomainRepository)
    }
}