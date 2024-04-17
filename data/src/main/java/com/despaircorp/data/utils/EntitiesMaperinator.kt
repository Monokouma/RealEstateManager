package com.despaircorp.data.utils

import com.despaircorp.data.currency.dto.CurrencyDto
import com.despaircorp.data.estate.dto.EstateDto
import com.despaircorp.data.estate.dto.EstateWithPictureDto
import com.despaircorp.data.picture.dto.PictureDto
import com.despaircorp.data.real_estate_agent.dto.RealEstateAgentDto
import com.despaircorp.data.real_estate_agent.workers.DefaultAgentEnum
import com.despaircorp.domain.currency.model.CurrencyEntity
import com.despaircorp.domain.currency.model.CurrencyEnum
import com.despaircorp.domain.estate.model.EstateEntity
import com.despaircorp.domain.estate.model.EstateWithPictureEntity
import com.despaircorp.domain.picture.model.EstatePictureEntity
import com.despaircorp.domain.real_estate_agent.model.CreatedAgentEntity
import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
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
                        location = it.estateDto.location,
                        estateType = it.estateDto.estateType,
                        price = it.estateDto.price,
                        pointOfInterest = it.estateDto.pointOfInterest,
                        sellingDate = it.estateDto.sellingDate,
                        entryDate = it.estateDto.entryDate,
                        status = it.estateDto.status,
                        city = it.estateDto.city,
                        address = it.estateDto.address,
                        agentInChargeId = it.estateDto.agentInChargeId
                    ),
                    pictures = it.pictureList.map { pictureDto ->
                        EstatePictureEntity(
                            pictureDto.id,
                            pictureDto.imagePath,
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
                location = estateEntity.location,
                estateType = estateEntity.estateType,
                price = estateEntity.price,
                pointOfInterest = estateEntity.pointOfInterest,
                sellingDate = estateEntity.sellingDate,
                entryDate = estateEntity.entryDate,
                status = estateEntity.status,
                city = estateEntity.city,
                address = estateEntity.address,
                agentInChargeId = estateEntity.agentInChargeId
            )
        }
    
    fun mapPictureEntitiesToDto(pictureEntities: List<EstatePictureEntity>): List<PictureDto> =
        pictureEntities.map { pictureEntity ->
            PictureDto(
                imagePath = pictureEntity.imagePath,
                description = pictureEntity.description,
                estateId = pictureEntity.id
            )
        }
    
    fun mapEstateWithPictureDtoToEstateWithPictureEntity(estateWithPictureDto: EstateWithPictureDto) =
        EstateWithPictureEntity(
            estateEntity = EstateEntity(
                id = estateWithPictureDto.estateDto.id,
                description = estateWithPictureDto.estateDto.description,
                surface = estateWithPictureDto.estateDto.surface,
                roomNumber = estateWithPictureDto.estateDto.roomNumber,
                bathroomNumber = estateWithPictureDto.estateDto.bathroomNumber,
                numberOfBedrooms = estateWithPictureDto.estateDto.numberOfBedrooms,
                location = estateWithPictureDto.estateDto.location,
                estateType = estateWithPictureDto.estateDto.estateType,
                price = estateWithPictureDto.estateDto.price,
                pointOfInterest = estateWithPictureDto.estateDto.pointOfInterest,
                sellingDate = estateWithPictureDto.estateDto.sellingDate,
                entryDate = estateWithPictureDto.estateDto.entryDate,
                status = estateWithPictureDto.estateDto.status,
                address = estateWithPictureDto.estateDto.address,
                city = estateWithPictureDto.estateDto.city,
                agentInChargeId = estateWithPictureDto.estateDto.agentInChargeId
            ),
            pictures = estateWithPictureDto.pictureList.map {
                EstatePictureEntity(
                    id = it.id,
                    imagePath = it.imagePath,
                    description = it.description
                )
            }
        )
    
    
    fun mapCurrencyDtoToCurrencyEntity(currencyDto: CurrencyDto): CurrencyEntity = CurrencyEntity(
        currencyDto.currencyEnum ?: CurrencyEnum.US_DOLLAR
    )
    
    fun mapCurrencyDtoToCurrencyEntityAsFlow(currencyDtoAsFlow: Flow<CurrencyDto>): Flow<CurrencyEntity> =
        currencyDtoAsFlow.mapNotNull { currencyDto ->
            currencyDto.currencyEnum?.let {
                CurrencyEntity(it)
            }
        }
    
    fun mapCurrencyEntityToCurrencyDto(currencyEntity: CurrencyEntity): CurrencyDto = CurrencyDto(
        1,
        currencyEntity.currencyEnum
    )
    
    fun mapEstateDtoToEstateEntity(estateDtoList: List<EstateDto>): List<EstateEntity> =
        estateDtoList.map { estateDto ->
            EstateEntity(
                id = estateDto.id,
                description = estateDto.description,
                surface = estateDto.surface,
                roomNumber = estateDto.roomNumber,
                bathroomNumber = estateDto.bathroomNumber,
                numberOfBedrooms = estateDto.numberOfBedrooms,
                location = estateDto.location,
                estateType = estateDto.estateType,
                price = estateDto.price,
                pointOfInterest = estateDto.pointOfInterest,
                sellingDate = estateDto.sellingDate,
                entryDate = estateDto.entryDate,
                status = estateDto.status,
                address = estateDto.address,
                city = estateDto.city,
                estateDto.agentInChargeId
            )
        }
    
    fun mapEstateEntityToDto(estateEntity: EstateEntity): EstateDto = EstateDto(
        id = estateEntity.id,
        description = estateEntity.description,
        surface = estateEntity.surface,
        roomNumber = estateEntity.roomNumber,
        bathroomNumber = estateEntity.bathroomNumber,
        numberOfBedrooms = estateEntity.numberOfBedrooms,
        location = estateEntity.location,
        estateType = estateEntity.estateType,
        price = estateEntity.price,
        pointOfInterest = estateEntity.pointOfInterest,
        sellingDate = estateEntity.sellingDate,
        entryDate = estateEntity.entryDate,
        status = estateEntity.status,
        city = estateEntity.city,
        address = estateEntity.address,
        estateEntity.agentInChargeId
    )
    
    fun mapPictureEntityToDto(pictureEntity: EstatePictureEntity) = PictureDto(
        imagePath = pictureEntity.imagePath,
        description = pictureEntity.description,
        estateId = pictureEntity.id
    )
    
    fun mapPictureDtoToEstatePictureEntities(pictureDtoList: List<PictureDto>): List<EstatePictureEntity> =
        pictureDtoList.map {
            EstatePictureEntity(
                id = it.estateId,
                imagePath = it.imagePath,
                description = it.description
            )
        }
}