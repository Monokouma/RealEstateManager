package com.despaircorp.domain.estate

import com.despaircorp.domain.estate.model.EstateWithPictureEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEstateWithPictureEntityAsFlowUseCase @Inject constructor(
    private val estateDomainRepository: EstateDomainRepository
) {
    fun invoke(): Flow<List<EstateWithPictureEntity>> =
        estateDomainRepository.getEstateWithPictureEntitiesAsFlow()
}