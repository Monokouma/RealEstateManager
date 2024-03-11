package com.despaircorp.domain.picture

import com.despaircorp.domain.picture.model.EstatePicture

interface PictureDomainRepository {
    fun enqueueInitPictureWorker()
    suspend fun exist(): Boolean
    suspend fun populatePictureTable(listOf: List<EstatePicture>)
}