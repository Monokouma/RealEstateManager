package com.despaircorp.domain.picture

import android.graphics.Bitmap
import android.net.Uri
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import com.despaircorp.domain.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SaveBitmapPictureToInternalStorageUseCaseUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val pictureDomainRepository: PictureDomainRepository = mockk()
    private val transformUriToBitmapUseCase: TransformUriToBitmapUseCase = mockk()
    private val bitmap: Bitmap = mockk()
    
    private val useCase = SaveBitmapPictureToInternalStorageUseCase(
        pictureDomainRepository = pictureDomainRepository,
        transformUriToBitmapUseCase = transformUriToBitmapUseCase
    )
    
    companion object {
        private const val DEFAULT_ID = 1
        private const val DEFAULT_FILE_NAME = "DEFAULT_FILE_NAME"
        private val DEFAULT_PICTURE_URI = mockk<Uri>()
    }
    
    @Before
    fun setup() {
        coEvery { transformUriToBitmapUseCase.invoke(DEFAULT_PICTURE_URI) } returns bitmap
        
        coEvery {
            pictureDomainRepository.saveImageToInternalStorage(
                bitmap,
                any()
            )
        } returns DEFAULT_FILE_NAME
    }
    
    @Test
    fun `nominal case - get file path`() = testCoroutineRule.runTest {
        
        
        val result = useCase.invoke(
            DEFAULT_PICTURE_URI,
            DEFAULT_FILE_NAME,
            DEFAULT_ID
        )
        
        assertThat(result).isEqualTo(DEFAULT_FILE_NAME)
        
        coVerify {
            pictureDomainRepository.saveImageToInternalStorage(
                bitmap,
                any()
            )
            transformUriToBitmapUseCase.invoke(DEFAULT_PICTURE_URI)
        }
        
        confirmVerified(pictureDomainRepository, transformUriToBitmapUseCase)
    }
    
    @Test
    fun `error case - null bitmap`() = testCoroutineRule.runTest {
        coEvery { transformUriToBitmapUseCase.invoke(DEFAULT_PICTURE_URI) } returns null
        
        val result = useCase.invoke(
            DEFAULT_PICTURE_URI,
            DEFAULT_FILE_NAME,
            DEFAULT_ID
        )
        
        assertThat(result).isEmpty()
        
        coVerify {
            transformUriToBitmapUseCase.invoke(DEFAULT_PICTURE_URI)
        }
        
        confirmVerified(pictureDomainRepository, transformUriToBitmapUseCase)
    }
}