package com.despaircorp.domain.picture

import javax.inject.Inject

class EnqueuePictureWorkerUseCase @Inject constructor(
    private val pictureDomainRepository: PictureDomainRepository
) {
    fun invoke() {
        pictureDomainRepository.enqueueInitPictureWorker()
    }
}