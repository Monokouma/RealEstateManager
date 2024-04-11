package com.despaircorp.stubs

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import androidx.annotation.RequiresApi
import com.despaircorp.data.estate.dto.EstateDto
import com.despaircorp.data.estate.dto.EstateWithPictureDto
import com.despaircorp.data.picture.dto.PictureDto
import com.despaircorp.data.real_estate_agent.dto.RealEstateAgentDto
import com.despaircorp.domain.estate.model.EstateEntity
import com.despaircorp.domain.estate.model.EstateStatus
import com.despaircorp.domain.estate.model.EstateWithPictureEntity
import com.despaircorp.domain.picture.model.EstatePictureEntity
import com.despaircorp.domain.picture.model.EstatePictureType
import com.despaircorp.domain.real_estate_agent.model.CreatedAgentEntity
import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDate


object EntityProvidinator {
    const val DEFAULT_NAME = "DEFAULT_NAME"
    const val DEFAULT_LOGGED_IN_FALSE = false
    const val DEFAULT_ID = 1
    const val DEFAULT_IMAGE_RESOURCE = 1
    const val DEFAULT_LOGGED_IN_TRUE = true
    const val DEFAULT_ESTATE_DESC = "DEFAULT_ESTATE_DESC"
    const val DEFAULT_ESTATE_SURFACE = "DEFAULT_ESTATE_SURFACE"
    const val DEFAULT_ESTATE_ROOM_NUMBER = 2
    const val DEFAULT_ESTATE_BATH_ROOM_NUMBER = 2
    const val DEFAULT_ESTATE_BED_ROOM_NUMBER = 2
    val DEFAULT_ESTATE_LOCATION = LatLng(0.0, 0.0)
    const val DEFAULT_ESTATE_TYPE = "DEFAULT_ESTATE_TYPE"
    const val DEFAULT_ESTATE_PRICE = "10.000"
    val DEFAULT_ESTATE_ENTRY_DATE = LocalDate.of(2022, 1, 11)
    val DEFAULT_ESTATE_STATUS = EstateStatus.FOR_SALE
    const val DEFAULT_ESTATE_ADDRESS = "DEFAULT_ESTATE_ADDRESS"
    const val DEFAULT_ESTATE_CITY = "DEFAULT_ESTATE_CITY"
    
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
                latitude = DEFAULT_ESTATE_LOCATION.latitude,
                longitude = DEFAULT_ESTATE_LOCATION.longitude,
                estateType = DEFAULT_ESTATE_TYPE,
                price = DEFAULT_ESTATE_PRICE,
                pointOfInterest = emptyList(),
                sellingDate = null,
                entryDate = DEFAULT_ESTATE_ENTRY_DATE,
                status = DEFAULT_ESTATE_STATUS,
            ),
            emptyList()
        )
    }
    
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
            city = DEFAULT_ESTATE_CITY
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
                city = DEFAULT_ESTATE_CITY
            ),
            emptyList()
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
    
    fun provideEstatePicture(): List<EstatePictureEntity> = List(3) {
        EstatePictureEntity(
            it,
            createSimpleBitmap(),
            EstatePictureType.KITCHEN
        )
    }
    
    private fun createSimpleBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            this.color = Color.BLUE
        }
        canvas.drawRect(0f, 0f, 100f, 100f, paint)
        return bitmap
    }
    
    fun provideEstatePictureDto(): List<PictureDto> = List(3) {
        PictureDto(
            it,
            createSimpleBitmap(),
            EstatePictureType.KITCHEN,
            it
        )
    }
}