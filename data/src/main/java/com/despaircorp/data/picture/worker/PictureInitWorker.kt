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
                id,
                savedImagePath,
                type
            )
        }
    
    override suspend fun doWork(): Result {
        
        if (!pictureDomainRepository.exist()) {
            pictureDomainRepository.populatePictureTable(
                listOf(
                    //1
                    getEstatePictureEntity("first_facade.bmp", 1),
                    getEstatePictureEntity("first_lounge.bmp", 1),
                    getEstatePictureEntity("first_kitchen.bmp", 1),
                    getEstatePictureEntity("first_bedroom.bmp", 1),
                    getEstatePictureEntity("first_bathroom.bmp", 1),
                    
                    //2
                    getEstatePictureEntity("second_facade.bmp", 2),
                    getEstatePictureEntity("second_lounge.bmp", 2),
                    getEstatePictureEntity("second_kitchen.bmp", 2),
                    getEstatePictureEntity("second_bedroom.bmp", 2),
                    getEstatePictureEntity("second_bathroom.bmp", 2),
                    
                    //3
                    getEstatePictureEntity("third_facade.bmp", 3),
                    getEstatePictureEntity("third_lounge.bmp", 3),
                    getEstatePictureEntity("third_kitchen.bmp", 3),
                    getEstatePictureEntity("third_bedroom.bmp", 3),
                    getEstatePictureEntity("third_bathroom.bmp", 3),
                    
                    //4
                    getEstatePictureEntity("fourth_facade.bmp", 4),
                    getEstatePictureEntity("fourth_lounge.bmp", 4),
                    getEstatePictureEntity("fourth_kitchen.bmp", 4),
                    getEstatePictureEntity("fourth_bedroom.bmp", 4),
                    getEstatePictureEntity("fourth_bathroom.bmp", 4),
                    
                    //5
                    getEstatePictureEntity("fifth_facade.bmp", 5),
                    getEstatePictureEntity("fifth_lounge.bmp", 5),
                    getEstatePictureEntity("fifth_kitchen.bmp", 5),
                    getEstatePictureEntity("fifth_bedroom.bmp", 5),
                    getEstatePictureEntity("fifth_bathroom.bmp", 5),
                    
                    )
            )
            
            
        }
        
        
        return Result.success()
    }
    
    
}