package com.despaircorp.domain.picture

import android.graphics.Bitmap
import com.despaircorp.domain.picture.model.EstatePictureEntity

interface PictureDomainRepository {
    fun enqueueInitPictureWorker()
    suspend fun exist(): Boolean
    suspend fun populatePictureTable(listOf: List<EstatePictureEntity>)
    suspend fun insertPictures(pictureEntity: EstatePictureEntity)
    suspend fun saveImageToInternalStorage(bitmap: Bitmap, fileName: String): String
    suspend fun getPictureEntities(): List<EstatePictureEntity>
    suspend fun deleteById(id: Int, path: String)
    
}