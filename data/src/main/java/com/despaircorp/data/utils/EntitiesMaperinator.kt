package com.despaircorp.data.utils

import com.despaircorp.data.estate.dto.EstateDto
import com.despaircorp.data.estate.dto.EstateWithPictureDto
import com.despaircorp.data.picture.dto.PictureDto
import com.despaircorp.data.real_estate_agent.dto.RealEstateAgentDto
import com.despaircorp.data.real_estate_agent.workers.DefaultAgentEnum
import com.despaircorp.domain.estate.model.EstateEntity
import com.despaircorp.domain.estate.model.EstateWithPictureEntity
import com.despaircorp.domain.picture.model.EstatePicture
import com.despaircorp.domain.real_estate_agent.model.CreatedAgentEntity
import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity
import com.google.android.gms.maps.model.LatLng
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
    
    fun mapEstateWithPictureDtoToEntities(estateWithPictureDto: Flow<List<EstateWithPictureDto>>): Flow<List<EstateWithPictureEntity>> =
        estateWithPictureDto.map { estateWithPicture ->
            estateWithPicture.map {
                EstateWithPictureEntity(
                    estateEntity = EstateEntity(
                        id = it.estateDto.id,
                        description = it.estateDto.description,
                        surface = it.estateDto.surface,
                        roomNumber = it.estateDto.roomNumber,
                        bathroomNumber = it.estateDto.bathroomNumber,
                        numberOfBedrooms = it.estateDto.numberOfBedrooms,
                        location = LatLng(it.estateDto.latitude, it.estateDto.longitude),
                        estateType = it.estateDto.estateType,
                        price = it.estateDto.price,
                        pointOfInterest = it.estateDto.pointOfInterest,
                        sellingDate = it.estateDto.sellingDate,
                        entryDate = it.estateDto.entryDate,
                        status = it.estateDto.status
                    ),
                    pictures = it.pictureList.map { pictureDto ->
                        EstatePicture(
                            pictureDto.id,
                            pictureDto.image,
                            pictureDto.description
                        )
                    }
                )
            }
        }
    
    fun mapEstateEntitiesToDto(estateEntities: List<EstateEntity>): List<EstateDto> =
        estateEntities.map { estateEntity ->
            EstateDto(
                description = estateEntity.description,
                surface = estateEntity.surface,
                roomNumber = estateEntity.roomNumber,
                bathroomNumber = estateEntity.bathroomNumber,
                numberOfBedrooms = estateEntity.numberOfBedrooms,
                latitude = estateEntity.location.latitude,
                longitude = estateEntity.location.longitude,
                estateType = estateEntity.estateType,
                price = estateEntity.price,
                pointOfInterest = estateEntity.pointOfInterest,
                sellingDate = estateEntity.sellingDate,
                entryDate = estateEntity.entryDate,
                status = estateEntity.status,
            )
        }
    
    fun mapPictureEntitiesToDto(pictureEntities: List<EstatePicture>): List<PictureDto> =
        pictureEntities.map { pictureEntity ->
            PictureDto(
                image = pictureEntity.bitmapImage,
                description = pictureEntity.description,
                estateId = pictureEntity.id
            )
        }
}