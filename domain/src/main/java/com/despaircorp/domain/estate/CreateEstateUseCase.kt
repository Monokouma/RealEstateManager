package com.despaircorp.domain.estate

import android.app.Application
import com.despaircorp.domain.connectivity.IsUserConnectedToInternetUseCase
import com.despaircorp.domain.estate.model.EstateCreationStatus
import com.despaircorp.domain.estate.model.EstateEntity
import com.despaircorp.domain.estate.model.EstateStatus
import com.despaircorp.domain.estate.model.PointOfInterestEntity
import com.despaircorp.domain.geocoder.GetLatLngFromAddressUseCase
import com.despaircorp.domain.picture.InsertPictureForEstateCreationUseCase
import com.despaircorp.domain.picture.model.EstatePictureEntity
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class CreateEstateUseCase @Inject constructor(
    private val estateDomainRepository: EstateDomainRepository,
    private val isUserConnectedToInternetUseCase: IsUserConnectedToInternetUseCase,
    private val sanitizeCreatedEstateUseCase: SanitizeCreatedEstateUseCase,
    private val application: Application,
    private val getLatLngFromAddressUseCase: GetLatLngFromAddressUseCase,
    private val insertPictureForEstateCreationUseCase: InsertPictureForEstateCreationUseCase
) {
    suspend fun invoke(
        estateSurface: String, //Required
        estateDescription: String, //Required
        estateRoomNumber: String, //Required
        estateBedroomNumber: String, //Required
        estateBathRoomNumber: String, //Required
        estateAddress: String, //Required
        estateCity: String, //Required
        estatePrice: String, //Required
        estateType: String, //Required
        estatePointOfInterests: List<PointOfInterestEntity>, //Required
        isEstateSold: Boolean, //Required
        estateEntryDate: String, //Required
        estateSoldDate: String,
        estatePictures: List<EstatePictureEntity>, //Required
        estateInChargeAgentId: Int //Required
    ): Result<Boolean> {
        var estateId = findHighestId(estateDomainRepository.getEstateEntity())
        estateId += 1
        
        
        val sanitizedForm = sanitizeCreatedEstateUseCase.invoke(
            estateSurface = estateSurface,
            estateDescription = estateDescription,
            estateRoomNumber = estateRoomNumber,
            estateBedroomNumber = estateBedroomNumber,
            estateBathRoomNumber = estateBathRoomNumber,
            estateAddress = estateAddress,
            estateCity = estateCity,
            estatePrice = estatePrice,
            estateType = estateType,
            estatePointOfInterests = estatePointOfInterests,
            isEstateSold = isEstateSold,
            estateEntryDate = estateEntryDate,
            estateSoldDate = estateSoldDate,
            estatePictures = estatePictures,
        )
        
        return if (sanitizedForm == EstateCreationStatus.SUCCESS) {
            
            //DO : Find a way to store image without bitmap (path to bitmap etc + sanitize price = Add "," )
            
            estateDomainRepository.insertNewEstate(
                EstateEntity(
                    id = estateId,
                    description = estateDescription,
                    surface = "${estateSurface}m2",
                    roomNumber = estateRoomNumber.toInt(),
                    bathroomNumber = estateBathRoomNumber.toInt(),
                    numberOfBedrooms = estateBedroomNumber.toInt(),
                    location = getLocationOrNull("$estateAddress $estateCity"),
                    estateType = estateType,
                    price = estatePrice,
                    pointOfInterest = getSelectedPointOfInterest(estatePointOfInterests),
                    sellingDate = getSellingDateOrNull(isEstateSold, estateSoldDate),
                    entryDate = getEntryDateAsLocalDate(estateEntryDate),
                    status = getEstateStatus(isEstateSold),
                    address = estateAddress,
                    city = estateCity,
                    estateInChargeAgentId
                )
            )
            
            
            insertPictureForEstateCreationUseCase.invoke(
                estatePictures,
                estateId
            )
            
            Result.success(true)
        } else {
            Result.failure(Exception(application.getString(sanitizedForm.message)))
        }
    }
    
    private suspend fun getLocationOrNull(addressWithCity: String): LatLng? =
        if (isUserConnectedToInternetUseCase.invoke().first()) {
            getLatLngFromAddressUseCase.invoke(addressWithCity)
        } else {
            null
        }
    
    
    private fun getSelectedPointOfInterest(pointOfInterestEntity: List<PointOfInterestEntity>): List<String> =
        pointOfInterestEntity.filter { it.isSelected }
            .map { application.getString(it.pointOfInterestEnum.textRes) }
    
    private fun getSellingDateOrNull(isSold: Boolean, sellingDate: String): LocalDate? =
        if (isSold) {
            LocalDate.parse(sellingDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        } else {
            null
        }
    
    private fun getEntryDateAsLocalDate(entryDate: String) =
        LocalDate.parse(entryDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    
    private fun getEstateStatus(isSold: Boolean): EstateStatus =
        if (isSold) EstateStatus.SOLD else EstateStatus.FOR_SALE
    
    private fun findHighestId(estates: List<EstateEntity>): Int = estates.maxBy { it.id }.id
}