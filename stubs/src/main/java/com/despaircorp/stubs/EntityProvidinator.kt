package com.despaircorp.stubs

import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import com.despaircorp.data.currency.dto.CurrencyDto
import com.despaircorp.data.estate.dto.EstateDto
import com.despaircorp.data.estate.dto.EstateWithPictureDto
import com.despaircorp.data.picture.dto.PictureDto
import com.despaircorp.data.real_estate_agent.dto.RealEstateAgentDto
import com.despaircorp.domain.currency.model.CurrencyEntity
import com.despaircorp.domain.currency.model.CurrencyEnum
import com.despaircorp.domain.estate.model.EstateEntity
import com.despaircorp.domain.estate.model.EstateStatus
import com.despaircorp.domain.estate.model.EstateTypeEnum
import com.despaircorp.domain.estate.model.EstateWithPictureEntity
import com.despaircorp.domain.estate.model.PointOfInterestEntity
import com.despaircorp.domain.estate.model.PointOfInterestEnum
import com.despaircorp.domain.estate.model.ToSanitizeEstateEntity
import com.despaircorp.domain.location.model.LocationEntity
import com.despaircorp.domain.picture.model.EstatePictureEntity
import com.despaircorp.domain.picture.model.EstatePictureType
import com.despaircorp.domain.real_estate_agent.model.CreatedAgentEntity
import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDate
import java.time.format.DateTimeFormatter


object EntityProvidinator {
    const val DEFAULT_NAME = "DEFAULT_NAME"
    const val DEFAULT_LOGGED_IN_FALSE = false
    const val DEFAULT_ID = 1
    const val DEFAULT_IMAGE_RESOURCE = 1
    const val DEFAULT_LOGGED_IN_TRUE = true
    const val DEFAULT_ESTATE_DESC = "DEFAULT_ESTATE_DESC"
    const val DEFAULT_ESTATE_SURFACE = 100
    const val DEFAULT_ESTATE_ROOM_NUMBER = 2
    const val DEFAULT_ESTATE_BATH_ROOM_NUMBER = 2
    const val DEFAULT_ESTATE_BED_ROOM_NUMBER = 2
    val DEFAULT_ESTATE_LOCATION = LatLng(0.0, 0.0)
    val DEFAULT_ESTATE_TYPE = EstateTypeEnum.MANOR
    const val DEFAULT_ESTATE_PRICE = "10000"
    val DEFAULT_ESTATE_ENTRY_DATE = LocalDate.of(2022, 1, 11)
    val DEFAULT_ESTATE_SOLD_DATE = LocalDate.of(2022, 1, 11)
    val DEFAULT_ESTATE_STATUS = EstateStatus.FOR_SALE
    const val DEFAULT_ESTATE_ADDRESS = "DEFAULT_ESTATE_ADDRESS"
    const val DEFAULT_ESTATE_CITY = "DEFAULT_ESTATE_CITY"
    const val DEFAULT_AGENT_IN_CHARGE_ID = 1
    const val DEFAULT_BITMAP_FILE_PATH = "DEFAULT_BITMAP_FILE_PATH"
    val DEFAULT_CURRENCY_ENUM_VALUE = CurrencyEnum.US_DOLLAR
    const val DEFAULT_LATITUDE = 48.857920
    const val DEFAULT_LONGITUDE = 2.295048
    val DEFAULT_PICTURE_TYPE = EstatePictureType.KITCHEN
    const val DEFAULT_IS_SELECTED_POINT_OF_INTEREST = true
    val DEFAULT_POINT_OF_INTEREST_ENUM = PointOfInterestEnum.SUBWAY
    
    
    @RequiresApi(Build.VERSION_CODES.P)
    fun provideListOfEstateWithPictureDto() = List(3) { estateWithPictureID ->
        EstateWithPictureDto(
            EstateDto(
                id = estateWithPictureID,
                description = DEFAULT_ESTATE_DESC,
                surface = DEFAULT_ESTATE_SURFACE,
                roomNumber = DEFAULT_ESTATE_ROOM_NUMBER,
                bathroomNumber = DEFAULT_ESTATE_BATH_ROOM_NUMBER,
                numberOfBedrooms = DEFAULT_ESTATE_BED_ROOM_NUMBER,
                location = DEFAULT_ESTATE_LOCATION,
                estateType = DEFAULT_ESTATE_TYPE,
                price = DEFAULT_ESTATE_PRICE,
                pointOfInterest = emptyList(),
                sellingDate = null,
                entryDate = DEFAULT_ESTATE_ENTRY_DATE,
                status = DEFAULT_ESTATE_STATUS,
                city = DEFAULT_ESTATE_ADDRESS,
                address = DEFAULT_ESTATE_CITY,
                agentInChargeId = DEFAULT_AGENT_IN_CHARGE_ID
            ),
            emptyList()
        )
    }
    
    fun provideEstateDto() = EstateDto(
        id = DEFAULT_ID,
        description = DEFAULT_ESTATE_DESC,
        surface = DEFAULT_ESTATE_SURFACE,
        roomNumber = DEFAULT_ESTATE_ROOM_NUMBER,
        bathroomNumber = DEFAULT_ESTATE_BATH_ROOM_NUMBER,
        numberOfBedrooms = DEFAULT_ESTATE_BED_ROOM_NUMBER,
        location = DEFAULT_ESTATE_LOCATION,
        estateType = DEFAULT_ESTATE_TYPE,
        price = DEFAULT_ESTATE_PRICE,
        pointOfInterest = emptyList(),
        sellingDate = null,
        entryDate = DEFAULT_ESTATE_ENTRY_DATE,
        status = DEFAULT_ESTATE_STATUS,
        city = DEFAULT_ESTATE_ADDRESS,
        address = DEFAULT_ESTATE_CITY,
        agentInChargeId = DEFAULT_AGENT_IN_CHARGE_ID
    )
    
    fun provideEstateDtoAsList() = List(3) {
        EstateDto(
            id = it,
            description = DEFAULT_ESTATE_DESC,
            surface = DEFAULT_ESTATE_SURFACE,
            roomNumber = DEFAULT_ESTATE_ROOM_NUMBER,
            bathroomNumber = DEFAULT_ESTATE_BATH_ROOM_NUMBER,
            numberOfBedrooms = DEFAULT_ESTATE_BED_ROOM_NUMBER,
            location = DEFAULT_ESTATE_LOCATION,
            estateType = DEFAULT_ESTATE_TYPE,
            price = DEFAULT_ESTATE_PRICE,
            pointOfInterest = emptyList(),
            sellingDate = null,
            entryDate = DEFAULT_ESTATE_ENTRY_DATE,
            status = DEFAULT_ESTATE_STATUS,
            city = DEFAULT_ESTATE_ADDRESS,
            address = DEFAULT_ESTATE_CITY,
            agentInChargeId = DEFAULT_AGENT_IN_CHARGE_ID
        )
    }
    
    fun provideEstateEntity() = EstateEntity(
        id = DEFAULT_ID,
        description = DEFAULT_ESTATE_DESC,
        surface = DEFAULT_ESTATE_SURFACE,
        roomNumber = DEFAULT_ESTATE_ROOM_NUMBER,
        bathroomNumber = DEFAULT_ESTATE_BATH_ROOM_NUMBER,
        numberOfBedrooms = DEFAULT_ESTATE_BED_ROOM_NUMBER,
        location = DEFAULT_ESTATE_LOCATION,
        estateType = DEFAULT_ESTATE_TYPE,
        price = DEFAULT_ESTATE_PRICE,
        pointOfInterest = emptyList(),
        sellingDate = null,
        entryDate = DEFAULT_ESTATE_ENTRY_DATE,
        status = DEFAULT_ESTATE_STATUS,
        city = DEFAULT_ESTATE_ADDRESS,
        address = DEFAULT_ESTATE_CITY,
        agentInChargeId = DEFAULT_AGENT_IN_CHARGE_ID
    )
    
    fun provideEstateEntities() = List(3) {
        EstateEntity(
            id = it,
            description = DEFAULT_ESTATE_DESC,
            surface = DEFAULT_ESTATE_SURFACE,
            roomNumber = DEFAULT_ESTATE_ROOM_NUMBER,
            bathroomNumber = DEFAULT_ESTATE_BATH_ROOM_NUMBER,
            numberOfBedrooms = DEFAULT_ESTATE_BED_ROOM_NUMBER,
            location = DEFAULT_ESTATE_LOCATION,
            estateType = DEFAULT_ESTATE_TYPE,
            price = DEFAULT_ESTATE_PRICE,
            pointOfInterest = emptyList(),
            sellingDate = null,
            entryDate = DEFAULT_ESTATE_ENTRY_DATE,
            status = DEFAULT_ESTATE_STATUS,
            address = DEFAULT_ESTATE_ADDRESS,
            city = DEFAULT_ESTATE_CITY,
            agentInChargeId = DEFAULT_AGENT_IN_CHARGE_ID
        )
    }
    
    fun provideEstateWithPictureEntities() = List(3) { estateWithPictureId ->
        EstateWithPictureEntity(
            EstateEntity(
                id = estateWithPictureId,
                description = DEFAULT_ESTATE_DESC,
                surface = DEFAULT_ESTATE_SURFACE,
                roomNumber = DEFAULT_ESTATE_ROOM_NUMBER,
                bathroomNumber = DEFAULT_ESTATE_BATH_ROOM_NUMBER,
                numberOfBedrooms = DEFAULT_ESTATE_BED_ROOM_NUMBER,
                location = DEFAULT_ESTATE_LOCATION,
                estateType = DEFAULT_ESTATE_TYPE,
                price = DEFAULT_ESTATE_PRICE,
                pointOfInterest = emptyList(),
                sellingDate = null,
                entryDate = DEFAULT_ESTATE_ENTRY_DATE,
                status = DEFAULT_ESTATE_STATUS,
                address = DEFAULT_ESTATE_ADDRESS,
                city = DEFAULT_ESTATE_CITY,
                agentInChargeId = DEFAULT_AGENT_IN_CHARGE_ID
            ),
            List(3) {
                EstatePictureEntity(
                    id = estateWithPictureId,
                    imagePath = DEFAULT_BITMAP_FILE_PATH,
                    description = DEFAULT_PICTURE_TYPE
                )
            }
        )
    }
    
    fun provideRealEstateAgentEntities() = List(3) {
        RealEstateAgentEntity(
            name = DEFAULT_NAME + "$it",
            id = it,
            imageResource = it,
            isLoggedIn = DEFAULT_LOGGED_IN_FALSE
        )
    }
    
    fun provideRealEstateDtoEntities() = List(3) {
        RealEstateAgentDto(
            name = DEFAULT_NAME + "$it",
            imageResource = it,
            isLoggedIn = DEFAULT_LOGGED_IN_FALSE
        )
    }
    
    
    fun provideLoggedRealEstateAgentDto() = RealEstateAgentDto(
        name = DEFAULT_NAME,
        imageResource = DEFAULT_IMAGE_RESOURCE,
        isLoggedIn = DEFAULT_LOGGED_IN_TRUE
    )
    
    fun provideLoggedRealEstateAgentEntity() = RealEstateAgentEntity(
        id = DEFAULT_ID,
        name = DEFAULT_NAME,
        imageResource = DEFAULT_IMAGE_RESOURCE,
        isLoggedIn = DEFAULT_LOGGED_IN_TRUE
    )
    
    fun provideCreatedAgentEntity() = CreatedAgentEntity(
        name = DEFAULT_NAME,
        imageResource = DEFAULT_IMAGE_RESOURCE
    )
    
    fun provideEstatePicturesEntities(): List<EstatePictureEntity> = List(3) {
        EstatePictureEntity(
            it,
            DEFAULT_BITMAP_FILE_PATH,
            EstatePictureType.KITCHEN
        )
    }
    
    
    fun provideEstatePicturesDto(): List<PictureDto> = List(3) {
        PictureDto(
            it,
            DEFAULT_BITMAP_FILE_PATH,
            EstatePictureType.KITCHEN,
            it
        )
    }
    
    fun provideEstatePictureEntity(): EstatePictureEntity =
        EstatePictureEntity(
            DEFAULT_ID,
            DEFAULT_BITMAP_FILE_PATH,
            EstatePictureType.KITCHEN
        )
    
    
    fun provideEstatePictureDto(): PictureDto =
        PictureDto(
            DEFAULT_ID,
            DEFAULT_BITMAP_FILE_PATH,
            EstatePictureType.KITCHEN,
            DEFAULT_ID
        )
    
    
    fun provideCurrencyDto() = CurrencyDto(
        DEFAULT_ID,
        DEFAULT_CURRENCY_ENUM_VALUE
    )
    
    fun provideCurrencyEntity() = CurrencyEntity(
        DEFAULT_CURRENCY_ENUM_VALUE
    )
    
    
    fun provideEstateWithPictureDtoToEntity(estateWithPictureDto: EstateWithPictureDto) =
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
                city = estateWithPictureDto.estateDto.city,
                address = estateWithPictureDto.estateDto.address,
                agentInChargeId = estateWithPictureDto.estateDto.agentInChargeId
            ),
            pictures = estateWithPictureDto.pictureList.map { pictureDto ->
                EstatePictureEntity(
                    pictureDto.id,
                    pictureDto.imagePath,
                    pictureDto.description
                )
            }
        )
    
    fun provideEstateWithPictureEntity() = EstateWithPictureEntity(
        EstateEntity(
            id = DEFAULT_ID,
            description = DEFAULT_ESTATE_DESC,
            surface = DEFAULT_ESTATE_SURFACE,
            roomNumber = DEFAULT_ESTATE_ROOM_NUMBER,
            bathroomNumber = DEFAULT_ESTATE_BATH_ROOM_NUMBER,
            numberOfBedrooms = DEFAULT_ESTATE_BED_ROOM_NUMBER,
            location = DEFAULT_ESTATE_LOCATION,
            estateType = DEFAULT_ESTATE_TYPE,
            price = DEFAULT_ESTATE_PRICE,
            pointOfInterest = emptyList(),
            sellingDate = null,
            entryDate = DEFAULT_ESTATE_ENTRY_DATE,
            status = DEFAULT_ESTATE_STATUS,
            address = DEFAULT_ESTATE_ADDRESS,
            city = DEFAULT_ESTATE_CITY,
            agentInChargeId = DEFAULT_AGENT_IN_CHARGE_ID
        ),
        emptyList()
    
    )
    
    fun provideEstateWithPictureDto() = EstateWithPictureDto(
        EstateDto(
            id = DEFAULT_ID,
            description = DEFAULT_ESTATE_DESC,
            surface = DEFAULT_ESTATE_SURFACE,
            roomNumber = DEFAULT_ESTATE_ROOM_NUMBER,
            bathroomNumber = DEFAULT_ESTATE_BATH_ROOM_NUMBER,
            numberOfBedrooms = DEFAULT_ESTATE_BED_ROOM_NUMBER,
            location = DEFAULT_ESTATE_LOCATION,
            estateType = DEFAULT_ESTATE_TYPE,
            price = DEFAULT_ESTATE_PRICE,
            pointOfInterest = emptyList(),
            sellingDate = null,
            entryDate = DEFAULT_ESTATE_ENTRY_DATE,
            status = DEFAULT_ESTATE_STATUS,
            city = DEFAULT_ESTATE_ADDRESS,
            address = DEFAULT_ESTATE_CITY,
            agentInChargeId = DEFAULT_AGENT_IN_CHARGE_ID
        ),
        emptyList()
    )
    
    fun provideAddressList(geocoder: Geocoder): MutableList<Address>? = geocoder.getFromLocation(
        DEFAULT_ESTATE_LOCATION.latitude,
        DEFAULT_ESTATE_LOCATION.longitude,
        1
    )
    
    fun provideLocationEntity() = LocationEntity(
        userLatLng = LatLng(
            DEFAULT_LATITUDE,
            DEFAULT_LONGITUDE
        )
    )
    
    
    fun providePointOfInterestList(): List<PointOfInterestEntity> = List(3) {
        PointOfInterestEntity(
            isSelected = DEFAULT_IS_SELECTED_POINT_OF_INTEREST,
            pointOfInterestEnum = DEFAULT_POINT_OF_INTEREST_ENUM,
            id = it
        )
    }
    
    fun provideEstatePictureEntities(): List<EstatePictureEntity> = List(3) {
        EstatePictureEntity(
            id = it,
            imagePath = DEFAULT_BITMAP_FILE_PATH,
            description = DEFAULT_PICTURE_TYPE
        )
    }
    
    fun provideToSanitizeEntity(): ToSanitizeEstateEntity = ToSanitizeEstateEntity(
        estateSurface = "$DEFAULT_ESTATE_SURFACE",
        estateDescription = DEFAULT_ESTATE_DESC,
        estateRoomNumber = DEFAULT_ESTATE_ROOM_NUMBER.toString(),
        estateBedroomNumber = DEFAULT_ESTATE_BED_ROOM_NUMBER.toString(),
        estateBathRoomNumber = DEFAULT_ESTATE_BATH_ROOM_NUMBER.toString(),
        estateAddress = DEFAULT_ESTATE_ADDRESS,
        estateCity = DEFAULT_ESTATE_CITY,
        estatePrice = DEFAULT_ESTATE_PRICE,
        estateType = DEFAULT_ESTATE_TYPE,
        estatePointOfInterests = providePointOfInterestList(),
        isEstateSold = false,
        estateEntryDate = DEFAULT_ESTATE_ENTRY_DATE.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
        estateSoldDate = DEFAULT_ESTATE_SOLD_DATE.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
        estatePictures = provideEstatePictureEntities(),
    )
}