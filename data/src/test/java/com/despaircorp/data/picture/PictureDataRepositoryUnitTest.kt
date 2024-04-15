package com.despaircorp.data.picture

import android.app.Application
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.despaircorp.data.picture.dao.PictureDao
import com.despaircorp.data.utils.EntitiesMaperinator
import com.despaircorp.data.utils.TestCoroutineRule
import com.despaircorp.stubs.EntityProvidinator.provideEstatePictureDto
import com.despaircorp.stubs.EntityProvidinator.provideEstatePictureEntity
import com.despaircorp.stubs.EntityProvidinator.provideEstatePicturesDto
import com.despaircorp.stubs.EntityProvidinator.provideEstatePicturesEntities
import io.mockk.coEvery
import io.mockk.coJustRun
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
    private val application: Application = mockk()
    
    private lateinit var repository: PictureDataRepository
    
    @Before
    fun setup() {
        coEvery { pictureDao.exist() } returns true
        coEvery { workManager.enqueue(any<OneTimeWorkRequest>()) } returns mockk()
        
        repository = PictureDataRepository(
            coroutineDispatcherProvider = testCoroutineRule.getTestCoroutineDispatcherProvider(),
            pictureDao = pictureDao,
            workManager = workManager,
            entitiesMaperinator = entitiesMaperinator,
            application = application
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
    
    @Test
    fun `nominal case - populate picture table`() = testCoroutineRule.runTest {
        coEvery { entitiesMaperinator.mapPictureEntitiesToDto(provideEstatePicturesEntities()) } returns provideEstatePicturesDto()
        coJustRun {
            pictureDao.insertAsList(
                entitiesMaperinator.mapPictureEntitiesToDto(
                    provideEstatePicturesEntities()
                )
            )
        }
        
        repository.populatePictureTable(provideEstatePicturesEntities())
        
        coVerify {
            entitiesMaperinator.mapPictureEntitiesToDto(provideEstatePicturesEntities())
            pictureDao.insertAsList(
                entitiesMaperinator.mapPictureEntitiesToDto(
                    provideEstatePicturesEntities()
                )
            )
        }
        
        confirmVerified(entitiesMaperinator, pictureDao)
    }
    
    @Test
    fun `nominal case - insert picture entity`() = testCoroutineRule.runTest {
        coEvery { entitiesMaperinator.mapPictureEntityToDto(provideEstatePictureEntity()) } returns provideEstatePictureDto()
        coJustRun {
            pictureDao.insert(
                entitiesMaperinator.mapPictureEntityToDto(
                    provideEstatePictureEntity()
                )
            )
        }
        
        repository.insertPictures(provideEstatePictureEntity())
        
        coVerify {
            entitiesMaperinator.mapPictureEntityToDto(provideEstatePictureEntity())
            pictureDao.insert(entitiesMaperinator.mapPictureEntityToDto(provideEstatePictureEntity()))
        }
        
        confirmVerified(entitiesMaperinator, pictureDao)
    }
}