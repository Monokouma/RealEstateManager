package com.despaircorp.data.utils

import com.despaircorp.data.real_estate_agent.dto.RealEstateAgentDto
import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object EntitiesMaperinator {
    
    fun mapRealEstateAgentDtoToRealEstateAgentEntities(allRealEstateAgentDto: List<RealEstateAgentDto>): List<RealEstateAgentEntity> {
        return allRealEstateAgentDto.map {
            RealEstateAgentEntity(
                name = it.name,
                id = it.id,
                imageResource = it.imageResource,
                isLoggedIn = it.isLoggedIn
            )
        }
    }
    
    fun mapRealEstateAgentEntitiesToRealEstateAgentDto(realEstateAgentEntities: List<RealEstateAgentEntity>): List<RealEstateAgentDto> {
        return realEstateAgentEntities.map {
            RealEstateAgentDto(
                id = it.id,
                name = it.name,
                imageResource = it.imageResource,
                isLoggedIn = it.isLoggedIn
            )
        }
    }
    
    fun mapRealEstateAgentDtoToEntity(realEstateAgentDto: RealEstateAgentDto): RealEstateAgentEntity {
        return RealEstateAgentEntity(
            name = realEstateAgentDto.name,
            id = realEstateAgentDto.id,
            imageResource = realEstateAgentDto.imageResource,
            isLoggedIn = realEstateAgentDto.isLoggedIn
        )
    }
    
    fun mapRealEstateAgentDtoToEntitiesAsFlow(realEstateAgentDtoFlow: Flow<List<RealEstateAgentDto>>): Flow<List<RealEstateAgentEntity>> =
        realEstateAgentDtoFlow.map { realEstateAgentList ->
            realEstateAgentList.map {
                RealEstateAgentEntity(
                    name = it.name,
                    id = it.id,
                    imageResource = it.imageResource,
                    isLoggedIn = it.isLoggedIn
                )
            }
        }
}