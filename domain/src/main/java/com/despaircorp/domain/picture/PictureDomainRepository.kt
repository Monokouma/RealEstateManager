package com.despaircorp.domain.picture

interface PictureDomainRepository {
    fun enqueueInitPictureWorker()
}