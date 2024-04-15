package com.despaircorp.domain.picture

import android.app.Application
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.despaircorp.domain.utils.TestCoroutineRule
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.InputStream

class TransformUriToBitmapUseCaseUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val application: Application = mockk()
    private val contentResolver = mockk<ContentResolver>()
    private val inputStream = mockk<InputStream>()
    private val expectedBitmap = mockk<Bitmap>()
    private val bitMapFactory = mockkStatic(BitmapFactory::class)
    
    
    private val useCase = TransformUriToBitmapUseCase(
        application
    )
    
    
    companion object {
        private val DEFAULT_PICTURE_URI = mockk<Uri>()
    }
    
    @Before
    fun setup() {
        mockkStatic(BitmapFactory::class)
        
        every { application.contentResolver } returns contentResolver
        every { contentResolver.openInputStream(DEFAULT_PICTURE_URI) } returns inputStream
        justRun { inputStream.close() }
        every { BitmapFactory.decodeStream(any()) } returns expectedBitmap
        
    }
    
    @Test
    fun `nominal test - return bitmap`() = testCoroutineRule.runTest {
        val result = useCase.invoke(DEFAULT_PICTURE_URI)
        assertThat(result).isEqualTo(expectedBitmap)
        
        coVerify {
            application.contentResolver
            contentResolver.openInputStream(DEFAULT_PICTURE_URI)
            inputStream.close()
            BitmapFactory.decodeStream(any())
        }
        
        confirmVerified(
            application,
            contentResolver,
            inputStream,
        )
    }
    
    @Test
    fun `error test - return null`() = testCoroutineRule.runTest {
        every { BitmapFactory.decodeStream(any()) } returns null
        
        val result = useCase.invoke(DEFAULT_PICTURE_URI)
        assertThat(result).isNull()
        
        coVerify {
            application.contentResolver
            contentResolver.openInputStream(DEFAULT_PICTURE_URI)
            inputStream.close()
            BitmapFactory.decodeStream(any())
        }
        
        confirmVerified(
            application,
            contentResolver,
            inputStream,
        )
    }
}