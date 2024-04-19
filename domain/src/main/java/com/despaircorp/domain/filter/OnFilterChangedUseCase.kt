package com.despaircorp.domain.filter

import javax.inject.Inject

class OnFilterChangedUseCase @Inject constructor(

) {
    fun invoke(
        filterArg: String,
        filterBy: FilterTypeEnum,
        toBuildQuery: MutableMap<String, String>
    ): MutableMap<String, String> {
        return when (filterBy) {
            FilterTypeEnum.SURFACE_MIN -> {
                
                val currentMap = toBuildQuery.toMutableMap()
                if (filterArg.isEmpty()) {
                    currentMap["surfaceMin"] = ""
                    
                    currentMap
                } else {
                    currentMap["surfaceMin"] = filterArg
                    currentMap
                }
            }
            
            FilterTypeEnum.SURFACE_MAX -> {
                
                val currentMap = toBuildQuery.toMutableMap()
                if (filterArg.isEmpty()) {
                    currentMap["surfaceMax"] = ""
                    currentMap
                } else {
                    currentMap["surfaceMax"] = filterArg
                    currentMap
                }
            }
            
            FilterTypeEnum.PRICE_MIN -> {
                val currentMap = toBuildQuery.toMutableMap()
                if (filterArg.isEmpty()) {
                    currentMap["priceMin"] = ""
                    currentMap
                } else {
                    currentMap["priceMin"] = filterArg
                    currentMap
                }
            }
            
            FilterTypeEnum.PRICE_MAX -> {
                val currentMap = toBuildQuery.toMutableMap()
                if (filterArg.isEmpty()) {
                    currentMap["priceMax"] = ""
                    currentMap
                } else {
                    currentMap["priceMax"] = filterArg
                    currentMap
                }
            }
            
            FilterTypeEnum.ROOM_NUMBER -> {
                val currentMap = toBuildQuery.toMutableMap()
                if (filterArg == "roomNumber=''") {
                    currentMap["roomNumber"] = ""
                    currentMap
                } else {
                    currentMap["roomNumber"] = filterArg
                    currentMap
                }
            }
            
            FilterTypeEnum.FOR_SALE -> {
                val currentMap = toBuildQuery.toMutableMap()
                currentMap["sold"] = ""
                currentMap["forSale"] = filterArg
                currentMap
            }
            
            FilterTypeEnum.SOLD -> {
                val currentMap = toBuildQuery.toMutableMap()
                currentMap["sold"] = filterArg
                currentMap["forSale"] = ""
                currentMap
            }
            
            FilterTypeEnum.ENTRY_DATE -> {
                val currentMap = toBuildQuery.toMutableMap()
                currentMap["entryDate"] = filterArg
                currentMap
            }
        }
    }
}