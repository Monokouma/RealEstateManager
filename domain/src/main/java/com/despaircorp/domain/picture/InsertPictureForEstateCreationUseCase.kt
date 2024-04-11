package com.despaircorp.domain.picture

import com.despaircorp.domain.picture.model.EstatePictureEntity
import javax.inject.Inject

class InsertPictureForEstateCreationUseCase @Inject constructor(
    private val pictureDomainRepository: PictureDomainRepository
) {
    suspend fun invoke(pictures: List<EstatePictureEntity>, estateId: Int) {
        val associatedPictureWithId = pictures.map {
            it.copy(id = estateId)
        }
        
        associatedPictureWithId.forEach {
            pictureDomainRepository.insertPictures(it)
        }
        
    }
    
}