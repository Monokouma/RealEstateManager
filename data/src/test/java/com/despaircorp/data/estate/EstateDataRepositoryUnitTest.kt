package com.despaircorp.data.estate

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.despaircorp.data.estate.dao.EstateDao
import com.despaircorp.data.utils.EntitiesMaperinator
import com.despaircorp.data.utils.TestCoroutineRule
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ID
import com.despaircorp.stubs.EntityProvidinator.provideEstateDto
import com.despaircorp.stubs.EntityProvidinator.provideEstateDtoAsList
import com.despaircorp.stubs.EntityProvidinator.provideEstateEntities
import com.despaircorp.stubs.EntityProvidinator.provideEstateEntity
import com.despaircorp.stubs.EntityProvidinator.provideEstateWithPictureDto
import com.despaircorp.stubs.EntityProvidinator.provideEstateWithPictureDtoToEntity
import com.despaircorp.stubs.EntityProvidinator.provideEstateWithPictureEntities
import com.despaircorp.stubs.EntityProvidinator.provideListOfEstateWithPictureDto
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EstateDataRepositoryUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val estateDao: EstateDao = mockk()
    private val workManager: WorkManager = mockk()
    private val entitiesMaperinator: EntitiesMaperinator = mockk()
    
    private lateinit var repository: EstateDataRepository
    
    @Before
    fun setup() {
        coEvery { estateDao.exist() } returns true
        coEvery { workManager.enqueue(any<OneTimeWorkRequest>()) } returns mockk()
        
        coEvery { estateDao.getEstateWithPictureAsFlow(any()) } returns flowOf(
            provideListOfEstateWithPictureDto()
        )
        
        coEvery {
            entitiesMaperinator.mapEstateWithPictureDtoToEntities(
                estateDao.getEstateWithPictureAsFlow(
                    any()
                )
            )
        } returns flowOf(provideEstateWithPictureEntities())
        
        coJustRun {
            estateDao.insertAsList(
                entitiesMaperinator.mapEstateEntitiesToDto(
                    provideEstateEntities()
                )
            )
        }
        
        repository = EstateDataRepository(
            coroutineDispatcherProvider = testCoroutineRule.getTestCoroutineDispatcherProvider(),
            estateDao = estateDao,
            workManager = workManager,
            entitiesMaperinator = entitiesMaperinator
        )
    }
    
    @Test
    fun `nominal case - get estate picture entities as flow`() = testCoroutineRule.runTest {
        repository.getEstateWithPictureEntitiesAsFlow(SimpleSQLiteQuery("SELECT * FROM estate_table"))
            .test {
                val result = awaitItem()
                awaitComplete()
                assertThat(result).isEqualTo(provideEstateWithPictureEntities())
                
                coVerify {
                    entitiesMaperinator.mapEstateWithPictureDtoToEntities(
                        estateDao.getEstateWithPictureAsFlow(
                            any()
                        )
                    )
                }
                
                confirmVerified(
                    entitiesMaperinator,
                    estateDao
                )
            }
    }
    
    @Test
    fun `nominal case - prepopulate estate table`() = testCoroutineRule.runTest {
        repository.prePopulateEstateTable(provideEstateEntities())
        
        coVerify {
            estateDao.insertAsList(entitiesMaperinator.mapEstateEntitiesToDto(provideEstateEntities()))
        }
        
        confirmVerified(estateDao)
    }
    
    @Test
    fun `nominal case - enqueue worker`() = testCoroutineRule.runTest {
        repository.enqueueEstateWorker()
        
        coVerify {
            workManager.enqueue(any<OneTimeWorkRequest>())
        }
        
        confirmVerified(workManager)
    }
    
    @Test
    fun `nominal case - estate dao table should exist`() = testCoroutineRule.runTest {
        val result = repository.isTableExisting()
        
        assertThat(result).isTrue()
        
        coVerify {
            estateDao.exist()
        }
        
        confirmVerified(estateDao)
    }
    
    @Test
    fun `nominal case - estate dao table shouldn't exist`() = testCoroutineRule.runTest {
        coEvery { estateDao.exist() } returns false
        
        val result = repository.isTableExisting()
        
        assertThat(result).isFalse()
        
        coVerify {
            estateDao.exist()
        }
        
        confirmVerified(estateDao)
    }
    
    @Test
    fun `nominal case - get estate with picture entity by id`() = testCoroutineRule.runTest {
        coEvery { estateDao.getEstateWithPictureDtoById(DEFAULT_ID) } returns provideEstateWithPictureDto()
        coEvery {
            entitiesMaperinator.mapEstateWithPictureDtoToEstateWithPictureEntity(
                estateDao.getEstateWithPictureDtoById(
                    DEFAULT_ID
                )
            )
        } returns provideEstateWithPictureDtoToEntity(provideEstateWithPictureDto())
        
        val result = repository.getEstateWithPictureEntityById(DEFAULT_ID)
        
        assertThat(result).isEqualTo(provideEstateWithPictureDtoToEntity(provideEstateWithPictureDto()))
        
        coVerify {
            estateDao.getEstateWithPictureDtoById(DEFAULT_ID)
            entitiesMaperinator.mapEstateWithPictureDtoToEstateWithPictureEntity(
                estateDao.getEstateWithPictureDtoById(
                    DEFAULT_ID
                )
            )
        }
        
        confirmVerified(
            estateDao,
            entitiesMaperinator
        )
    }
    
    @Test
    fun `nominal case - insert new estate`() = testCoroutineRule.runTest {
        coJustRun { estateDao.insert(provideEstateDto()) }
        coEvery {
            entitiesMaperinator.mapEstateEntityToDto(provideEstateEntity())
        } returns provideEstateDto()
        
        repository.insertNewEstate(provideEstateEntity())
        
        coVerify {
            estateDao.insert(provideEstateDto())
            entitiesMaperinator.mapEstateEntityToDto(provideEstateEntity())
        }
        
        confirmVerified(
            estateDao,
            entitiesMaperinator
        )
    }
    
    @Test
    fun `nominal case - get estate entities`() = testCoroutineRule.runTest {
        coEvery { estateDao.getEstateDto() } returns provideEstateDtoAsList()
        coEvery { entitiesMaperinator.mapEstateDtoToEstateEntity(provideEstateDtoAsList()) } returns provideEstateEntities()
        
        val result = repository.getEstateEntities()
        
        assertThat(result).isEqualTo(provideEstateEntities())
        
        coVerify {
            estateDao.getEstateDto()
            entitiesMaperinator.mapEstateDtoToEstateEntity(provideEstateDtoAsList())
        }
        
        confirmVerified(
            estateDao,
            entitiesMaperinator
        )
    }
}