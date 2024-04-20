package com.despaircorp.data.picture.worker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.despaircorp.domain.picture.PictureDomainRepository
import com.despaircorp.domain.picture.model.EstatePictureEntity
import com.despaircorp.domain.picture.model.EstatePictureType
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class PictureInitWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val pictureDomainRepository: PictureDomainRepository,
) : CoroutineWorker(context, workerParams) {
    
    private fun getBitmapFromAssets(assetName: String): Bitmap =
        applicationContext.assets.open(assetName).use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }
    
    private suspend fun getEstatePictureEntity(filename: String, id: Int) =
        getBitmapFromAssets(filename).let { bitmap ->
            val savedImagePath =
                pictureDomainRepository.saveImageToInternalStorage(bitmap, filename)
            val type = when {
                filename.contains("facade") -> EstatePictureType.FACADE
                filename.contains("lounge") -> EstatePictureType.LOUNGE
                filename.contains("kitchen") -> EstatePictureType.KITCHEN
                filename.contains("bedroom") -> EstatePictureType.BEDROOM
                filename.contains("bathroom") -> EstatePictureType.BATHROOM
                else -> throw IllegalArgumentException("Unknown picture type for file: $filename")
            }
            
            EstatePictureEntity(
                id = id,
                imagePath = savedImagePath,
                description = type
            )
        }
    
    override suspend fun doWork(): Result {
        
        if (!pictureDomainRepository.exist()) {
            pictureDomainRepository.populatePictureTable(
                listOf(
                    //1
                    getEstatePictureEntity(filename = "first_facade.bmp", id = 1),
                    getEstatePictureEntity(filename = "first_lounge.bmp", id = 1),
                    getEstatePictureEntity(filename = "first_kitchen.bmp", id = 1),
                    getEstatePictureEntity(filename = "first_bedroom.bmp", id = 1),
                    getEstatePictureEntity(filename = "first_bathroom.bmp", id = 1),
                    
                    //2
                    getEstatePictureEntity(filename = "second_facade.bmp", id = 2),
                    getEstatePictureEntity(filename = "second_lounge.bmp", id = 2),
                    getEstatePictureEntity(filename = "second_kitchen.bmp", id = 2),
                    getEstatePictureEntity(filename = "second_bedroom.bmp", id = 2),
                    getEstatePictureEntity(filename = "second_bathroom.bmp", id = 2),
                    
                    //3
                    getEstatePictureEntity(filename = "third_facade.bmp", id = 3),
                    getEstatePictureEntity(filename = "third_lounge.bmp", id = 3),
                    getEstatePictureEntity(filename = "third_kitchen.bmp", id = 3),
                    getEstatePictureEntity(filename = "third_bedroom.bmp", id = 3),
                    getEstatePictureEntity(filename = "third_bathroom.bmp", id = 3),
                    
                    //4
                    getEstatePictureEntity(filename = "fourth_facade.bmp", id = 4),
                    getEstatePictureEntity(filename = "fourth_lounge.bmp", id = 4),
                    getEstatePictureEntity(filename = "fourth_kitchen.bmp", id = 4),
                    getEstatePictureEntity(filename = "fourth_bedroom.bmp", id = 4),
                    getEstatePictureEntity(filename = "fourth_bathroom.bmp", id = 4),
                    
                    //5
                    getEstatePictureEntity(filename = "fifth_facade.bmp", id = 5),
                    getEstatePictureEntity(filename = "fifth_lounge.bmp", id = 5),
                    getEstatePictureEntity(filename = "fifth_kitchen.bmp", id = 5),
                    getEstatePictureEntity(filename = "fifth_bedroom.bmp", id = 5),
                    getEstatePictureEntity(filename = "fifth_bathroom.bmp", id = 5),
                )
            )
        }
        return Result.success()
    }
}