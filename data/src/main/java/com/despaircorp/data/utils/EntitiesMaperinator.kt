package com.despaircorp.data.utils

import com.despaircorp.data.real_estate_agent.dto.RealEstateAgentDto
import com.despaircorp.data.real_estate_agent.workers.DefaultAgentEnum
import com.despaircorp.domain.real_estate_agent.model.CreatedAgentEntity
import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.enums.EnumEntries

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
    
    fun mapDefaultAgentEnumToRealEstateAgentEntity(entries: EnumEntries<DefaultAgentEnum>): List<RealEstateAgentEntity> =
        entries.map { defaultAgentEnum ->
            RealEstateAgentEntity(
                name = defaultAgentEnum.displayNameRes,
                id = defaultAgentEnum.id,
                imageResource = defaultAgentEnum.imageRes,
                isLoggedIn = defaultAgentEnum.defaultIsLoggedIn
            )
        }
    
    fun mapCreatedAgentEntityToRealEstateAgentDto(createdAgentEntity: CreatedAgentEntity): RealEstateAgentDto =
        RealEstateAgentDto(
            name = createdAgentEntity.name,
            imageResource = createdAgentEntity.imageResource,
            isLoggedIn = false
        )
}