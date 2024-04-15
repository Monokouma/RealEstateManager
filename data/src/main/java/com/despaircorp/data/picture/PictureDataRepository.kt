package com.despaircorp.data.picture

import android.app.Application
import android.graphics.Bitmap
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.despaircorp.data.picture.dao.PictureDao
import com.despaircorp.data.picture.worker.PictureInitWorker
import com.despaircorp.data.utils.CoroutineDispatcherProvider
import com.despaircorp.data.utils.EntitiesMaperinator
import com.despaircorp.domain.picture.PictureDomainRepository
import com.despaircorp.domain.picture.model.EstatePictureEntity
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class PictureDataRepository @Inject constructor(
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val pictureDao: PictureDao,
    private val workManager: WorkManager,
    private val entitiesMaperinator: EntitiesMaperinator,
    private val application: Application,
) : PictureDomainRepository {
    override fun enqueueInitPictureWorker() {
        val pictureInitWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<PictureInitWorker>()
                .build()
        
        workManager.enqueue(pictureInitWorkRequest)
    }
    
    override suspend fun exist(): Boolean = withContext(coroutineDispatcherProvider.io) {
        pictureDao.exist()
    }
    
    override suspend fun populatePictureTable(listOf: List<EstatePictureEntity>): Unit =
        withContext(coroutineDispatcherProvider.io) {
            
            pictureDao.insertAsList(entitiesMaperinator.mapPictureEntitiesToDto(listOf))
        }
    
    override suspend fun insertPictures(pictureEntity: EstatePictureEntity) =
        withContext(coroutineDispatcherProvider.io) {
            pictureDao.insert(entitiesMaperinator.mapPictureEntityToDto(pictureEntity))
        }
    
    override suspend fun saveImageToInternalStorage(bitmap: Bitmap, fileName: String): String =
        withContext(coroutineDispatcherProvider.io) {
            val file = File(application.filesDir, fileName)
            try {
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                    file.absolutePath
                }
            } catch (e: IOException) {
                e.printStackTrace()
                ensureActive()
                ""
            }
        }
    
}