package com.despaircorp.data.picture

import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.despaircorp.data.picture.dao.PictureDao
import com.despaircorp.data.utils.EntitiesMaperinator
import com.despaircorp.data.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class PictureDataRepositoryUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val pictureDao: PictureDao = mockk()
    private val workManager: WorkManager = mockk()
    private val entitiesMaperinator: EntitiesMaperinator = mockk()
    
    private lateinit var repository: PictureDataRepository
    
    @Before
    fun setup() {
        coEvery { pictureDao.exist() } returns true
        
        coEvery { workManager.enqueue(any<OneTimeWorkRequest>()) } returns mockk()
        
        
        repository = PictureDataRepository(
            testCoroutineRule.getTestCoroutineDispatcherProvider(),
            pictureDao,
            workManager,
            entitiesMaperinator
        )
    }
    
    
    @Test
    fun `nominal case - enqueue worker`() = testCoroutineRule.runTest {
        repository.enqueueInitPictureWorker()
        
        coVerify {
            workManager.enqueue(any<OneTimeWorkRequest>())
        }
        
        confirmVerified(workManager)
    }
    
    
    @Test
    fun `nominal case - not created table`() = testCoroutineRule.runTest {
        coEvery { pictureDao.exist() } returns false
        
        assertThat(repository.exist()).isFalse()
        
        coVerify {
            pictureDao.exist()
        }
        
        confirmVerified(pictureDao)
    }
    
    @Test
    fun `nominal case - created table`() = testCoroutineRule.runTest {
        assertThat(repository.exist()).isTrue()
        
        coVerify {
            pictureDao.exist()
        }
        
        confirmVerified(pictureDao)
    }
}