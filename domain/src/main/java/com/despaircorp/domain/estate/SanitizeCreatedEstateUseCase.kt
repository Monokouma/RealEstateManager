package com.despaircorp.domain.estate

import android.util.Log
import com.despaircorp.domain.estate.model.EstateCreationStatus
import com.despaircorp.domain.estate.model.PointOfInterestEntity
import com.despaircorp.domain.picture.model.EstatePictureEntity
import javax.inject.Inject

class SanitizeCreatedEstateUseCase @Inject constructor(

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
        isEstateSold: Boolean,
        estateEntryDate: String, //Required
        estateSoldDate: String,
        estatePictures: List<EstatePictureEntity>, //Required
    ): EstateCreationStatus {
        return if (estateSurface.isEmpty()) {
            Log.i("Monokouma", "EMPTY_SURFACE")
            EstateCreationStatus.EMPTY_SURFACE
        } else if (estateDescription.isEmpty()) {
            Log.i("Monokouma", "EMPTY_DESCRIPTION")
            EstateCreationStatus.EMPTY_DESCRIPTION
        } else if (estateRoomNumber.isEmpty()) {
            Log.i("Monokouma", "EMPTY_ROOM_NUMBER")
            EstateCreationStatus.EMPTY_ROOM_NUMBER
        } else if (estateBedroomNumber.isEmpty()) {
            Log.i("Monokouma", "EMPTY_BEDROOM_NUMBER")
            EstateCreationStatus.EMPTY_BEDROOM_NUMBER
        } else if (estateBathRoomNumber.isEmpty()) {
            Log.i("Monokouma", "EMPTY_BATHROOM_NUMBER")
            EstateCreationStatus.EMPTY_BATHROOM_NUMBER
        } else if (estateAddress.isEmpty()) {
            Log.i("Monokouma", "EMPTY_ADDRESS")
            EstateCreationStatus.EMPTY_ADDRESS
        } else if (estateCity.isEmpty()) {
            Log.i("Monokouma", "EMPTY_CITY")
            EstateCreationStatus.EMPTY_CITY
        } else if (estatePrice.isEmpty()) {
            Log.i("Monokouma", "EMPTY_PRICE")
            EstateCreationStatus.EMPTY_PRICE
        } else if (estateType.isEmpty()) {
            Log.i("Monokouma", "EMPTY_TYPE")
            EstateCreationStatus.EMPTY_TYPE
        } else if (!isAtLeastOnePointOfInterestSelected(estatePointOfInterests)) {
            Log.i("Monokouma", "EMPTY_POINT_OF_INTEREST")
            EstateCreationStatus.EMPTY_POINT_OF_INTEREST
        } else if (estateEntryDate.isEmpty() || !estateEntryDate.contains("/")) {
            Log.i("Monokouma", "EMPTY_ENTRY_DATE")
            EstateCreationStatus.EMPTY_ENTRY_DATE
        } else if (estatePictures.isEmpty()) {
            Log.i("Monokouma", "EMPTY_PICTURE")
            EstateCreationStatus.EMPTY_PICTURE
        } else if (isEstateSold && (estateSoldDate.isEmpty() || !estateSoldDate.contains("/"))) {
            Log.i("Monokouma", "EMPTY_SOLD_DATE")
            EstateCreationStatus.EMPTY_SOLD_DATE
        } else {
            Log.i("Monokouma", "SUCCESS")
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