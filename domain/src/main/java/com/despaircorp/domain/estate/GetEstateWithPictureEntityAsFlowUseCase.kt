package com.despaircorp.domain.estate

import androidx.sqlite.db.SupportSQLiteQuery
import com.despaircorp.domain.estate.model.EstateWithPictureEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEstateWithPictureEntityAsFlowUseCase @Inject constructor(
    private val estateDomainRepository: EstateDomainRepository
) {
    fun invoke(query: SupportSQLiteQuery): Flow<List<EstateWithPictureEntity>> =
        estateDomainRepository.getEstateWithPictureEntitiesAsFlow(query)
}