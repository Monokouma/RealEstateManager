package com.despaircorp.domain.picture

import com.despaircorp.domain.utils.TestCoroutineRule
import com.despaircorp.stubs.EntityProvidinator
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ID
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InsertPictureForEstateCreationUseCaseUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val pictureDomainRepository: PictureDomainRepository = mockk()
    
    private val useCase = InsertPictureForEstateCreationUseCase(
        pictureDomainRepository
    )
    
    @Before
    fun setup() {
        coJustRun { pictureDomainRepository.insertPictures(EntityProvidinator.provideEstatePictureEntity()) }
    }
    
    @Test
    fun `nominal case - insert picture`() = testCoroutineRule.runTest {
        useCase.invoke(EntityProvidinator.provideEstatePicturesEntities(), DEFAULT_ID)
        
        coVerify {
            pictureDomainRepository.insertPictures(EntityProvidinator.provideEstatePictureEntity())
        }
        
        confirmVerified(pictureDomainRepository)
    }
    
}