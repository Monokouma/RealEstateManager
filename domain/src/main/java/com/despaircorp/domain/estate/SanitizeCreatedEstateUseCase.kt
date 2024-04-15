package com.despaircorp.domain.estate

import com.despaircorp.domain.estate.model.EstateCreationStatus
import com.despaircorp.domain.estate.model.PointOfInterestEntity
import com.despaircorp.domain.estate.model.ToSanitizeEstateEntity
import javax.inject.Inject

class SanitizeCreatedEstateUseCase @Inject constructor(

) {
    fun invoke(
        toSanitizeEstateEntity: ToSanitizeEstateEntity
    ): EstateCreationStatus {
        
        return if (toSanitizeEstateEntity.estateSurface.isEmpty()) {
            EstateCreationStatus.EMPTY_SURFACE
        } else if (toSanitizeEstateEntity.estateDescription.isEmpty()) {
            EstateCreationStatus.EMPTY_DESCRIPTION
        } else if (toSanitizeEstateEntity.estateRoomNumber.isEmpty()) {
            EstateCreationStatus.EMPTY_ROOM_NUMBER
        } else if (toSanitizeEstateEntity.estateBedroomNumber.isEmpty()) {
            EstateCreationStatus.EMPTY_BEDROOM_NUMBER
        } else if (toSanitizeEstateEntity.estateBathRoomNumber.isEmpty()) {
            EstateCreationStatus.EMPTY_BATHROOM_NUMBER
        } else if (toSanitizeEstateEntity.estateAddress.isEmpty()) {
            EstateCreationStatus.EMPTY_ADDRESS
        } else if (toSanitizeEstateEntity.estateCity.isEmpty()) {
            EstateCreationStatus.EMPTY_CITY
        } else if (toSanitizeEstateEntity.estatePrice.isEmpty()) {
            EstateCreationStatus.EMPTY_PRICE
        } else if (toSanitizeEstateEntity.estateType.isEmpty()) {
            EstateCreationStatus.EMPTY_TYPE
        } else if (!isAtLeastOnePointOfInterestSelected(toSanitizeEstateEntity.estatePointOfInterests)) {
            EstateCreationStatus.EMPTY_POINT_OF_INTEREST
        } else if (toSanitizeEstateEntity.estateEntryDate.isEmpty() || !toSanitizeEstateEntity.estateEntryDate.contains(
                "/"
            )
        ) {
            EstateCreationStatus.EMPTY_ENTRY_DATE
        } else if (toSanitizeEstateEntity.estatePictures.isEmpty()) {
            EstateCreationStatus.EMPTY_PICTURE
        } else if (toSanitizeEstateEntity.isEstateSold && (toSanitizeEstateEntity.estateSoldDate.isEmpty() || !toSanitizeEstateEntity.estateSoldDate.contains(
                "/"
            ))
        ) {
            EstateCreationStatus.EMPTY_SOLD_DATE
        } else {
            EstateCreationStatus.SUCCESS
        }
    }
    
    private fun isAtLeastOnePointOfInterestSelected(pointOfInterest: List<PointOfInterestEntity>): Boolean {
        var isOneSelected = false
        
        pointOfInterest.forEach {
            if (it.isSelected) {
                isOneSelected = true
            }
        }
        return isOneSelected
    }
}