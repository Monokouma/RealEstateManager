package com.despaircorp.domain.picture

import com.despaircorp.domain.picture.model.EstatePictureEntity
import javax.inject.Inject

class InsertPictureForEstateCreationUseCase @Inject constructor(
    private val pictureDomainRepository: PictureDomainRepository
) {
    suspend fun invoke(pictures: List<EstatePictureEntity>, estateId: Int, isEditMode: Boolean) {
        if (isEditMode) {
            
            val associatedPictureWithId = pictures.map {
                it.copy(id = estateId)
            }
            val pictureEntityForThisEstate =
                pictureDomainRepository.getPictureEntities().filter { it.id == estateId }
            
            pictureEntityForThisEstate.filter { it !in associatedPictureWithId }.forEach {
                pictureDomainRepository.deleteById(it.id, it.imagePath)
            }
            
            associatedPictureWithId.filter { it !in pictureEntityForThisEstate }.forEach {
                pictureDomainRepository.insertPictures(
                    it
                )
            }
            
        } else {
            val associatedPictureWithId = pictures.map {
                it.copy(id = estateId)
            }
            
            associatedPictureWithId.forEach {
                pictureDomainRepository.insertPictures(it)
            }
        }
    }
}