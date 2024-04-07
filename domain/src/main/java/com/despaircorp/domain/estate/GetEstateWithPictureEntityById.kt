package com.despaircorp.domain.estate

import com.despaircorp.domain.estate.model.EstateWithPictureEntity
import javax.inject.Inject

class GetEstateWithPictureEntityById @Inject constructor(
    private val estateDomainRepository: EstateDomainRepository
) {
    suspend fun invoke(estateId: Int): EstateWithPictureEntity =
        estateDomainRepository.getEstateWithPictureEntityById(estateId)
    
}