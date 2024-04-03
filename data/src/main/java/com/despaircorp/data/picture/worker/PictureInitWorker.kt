package com.despaircorp.data.picture.worker

import android.content.Context
import android.content.res.AssetManager
import android.graphics.BitmapFactory
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.despaircorp.domain.picture.PictureDomainRepository
import com.despaircorp.domain.picture.model.EstatePicture
import com.despaircorp.domain.picture.model.EstatePictureType
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class PictureInitWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val pictureDomainRepository: PictureDomainRepository,
    private val assetManager: AssetManager
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        if (!pictureDomainRepository.exist()) {
            pictureDomainRepository.populatePictureTable(
                listOf(
                    
                    //1
                    EstatePicture(
                        1,
                        BitmapFactory.decodeStream(assetManager.open("first_facade.bmp")),
                        EstatePictureType.FACADE,
                    ),
                    EstatePicture(
                        1,
                        BitmapFactory.decodeStream(assetManager.open("first_lounge.bmp")),
                        EstatePictureType.LOUNGE,
                    ),
                    EstatePicture(
                        1,
                        BitmapFactory.decodeStream(assetManager.open("first_kitchen.bmp")),
                        EstatePictureType.KITCHEN,
                    ),
                    EstatePicture(
                        1,
                        BitmapFactory.decodeStream(assetManager.open("first_bedroom.bmp")),
                        EstatePictureType.BEDROOM,
                    ),
                    EstatePicture(
                        1,
                        BitmapFactory.decodeStream(assetManager.open("first_bathroom.bmp")),
                        EstatePictureType.BATHROOM,
                    ),//2
                    
                    
                    EstatePicture(
                        2,
                        BitmapFactory.decodeStream(assetManager.open("second_facade.bmp")),
                        EstatePictureType.FACADE,
                    ),
                    EstatePicture(
                        2,
                        BitmapFactory.decodeStream(assetManager.open("second_lounge.bmp")),
                        EstatePictureType.LOUNGE,
                    ),
                    EstatePicture(
                        2,
                        BitmapFactory.decodeStream(assetManager.open("second_kitchen.bmp")),
                        EstatePictureType.KITCHEN,
                    ),
                    EstatePicture(
                        2,
                        BitmapFactory.decodeStream(assetManager.open("second_bedroom.bmp")),
                        EstatePictureType.BEDROOM,
                    ),
                    EstatePicture(
                        2,
                        BitmapFactory.decodeStream(assetManager.open("second_bathroom.bmp")),
                        EstatePictureType.BATHROOM,
                    ),//3
                    
                    
                    EstatePicture(
                        3,
                        BitmapFactory.decodeStream(assetManager.open("third_facade.bmp")),
                        EstatePictureType.FACADE,
                    ),
                    EstatePicture(
                        3,
                        BitmapFactory.decodeStream(assetManager.open("third_lounge.bmp")),
                        EstatePictureType.LOUNGE,
                    ),
                    EstatePicture(
                        3,
                        BitmapFactory.decodeStream(assetManager.open("third_kitchen.bmp")),
                        EstatePictureType.KITCHEN,
                    ),
                    EstatePicture(
                        3,
                        BitmapFactory.decodeStream(assetManager.open("third_bedroom.bmp")),
                        EstatePictureType.BEDROOM,
                    ),
                    EstatePicture(
                        3,
                        BitmapFactory.decodeStream(assetManager.open("third_bathroom.bmp")),
                        EstatePictureType.BATHROOM,
                    ),//4
                    
                    
                    EstatePicture(
                        4,
                        BitmapFactory.decodeStream(assetManager.open("fourth_facade.bmp")),
                        EstatePictureType.FACADE,
                    ),
                    EstatePicture(
                        4,
                        BitmapFactory.decodeStream(assetManager.open("fourth_lounge.bmp")),
                        EstatePictureType.LOUNGE,
                    ),
                    EstatePicture(
                        4,
                        BitmapFactory.decodeStream(assetManager.open("fourth_kitchen.bmp")),
                        EstatePictureType.KITCHEN,
                    ),
                    
                    EstatePicture(
                        4,
                        BitmapFactory.decodeStream(assetManager.open("fourth_bedroom.bmp")),
                        EstatePictureType.BEDROOM,
                    ),
                    EstatePicture(
                        4,
                        BitmapFactory.decodeStream(assetManager.open("fourth_bathroom.bmp")),
                        EstatePictureType.BATHROOM,
                    ),//5
                    
                    EstatePicture(
                        5,
                        BitmapFactory.decodeStream(assetManager.open("fifth_facade.bmp")),
                        EstatePictureType.FACADE,
                    ),
                    EstatePicture(
                        5,
                        BitmapFactory.decodeStream(assetManager.open("fifth_lounge.bmp")),
                        EstatePictureType.LOUNGE,
                    ),
                    EstatePicture(
                        5,
                        BitmapFactory.decodeStream(assetManager.open("fifth_kitchen.bmp")),
                        EstatePictureType.KITCHEN,
                    ),
                    
                    EstatePicture(
                        5,
                        BitmapFactory.decodeStream(assetManager.open("fifth_bedroom.bmp")),
                        EstatePictureType.BEDROOM,
                    ),
                    EstatePicture(
                        5,
                        BitmapFactory.decodeStream(assetManager.open("fifth_bathroom.bmp")),
                        EstatePictureType.BATHROOM,
                    ),
                )
            )
        }
        return Result.success()
    }
    
}