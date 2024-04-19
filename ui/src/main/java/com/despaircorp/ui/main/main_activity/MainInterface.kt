package com.despaircorp.ui.main.main_activity

import com.despaircorp.domain.estate.model.EstateTypeEntity
import com.despaircorp.domain.estate.model.PointOfInterestEntity
import com.despaircorp.domain.filter.FilterTypeEnum

interface MainInterface {
    fun onFilterChangedListener(filterArg: String, filterBy: FilterTypeEnum)
    
    fun onPointOfInterestForFilteringChanged(pointOfInterestEntities: List<PointOfInterestEntity>)
    
    fun onEstateTypeForFilteringChanged(estateTypeEntities: List<EstateTypeEntity>)
    
    fun onApplyFilter()
    
    fun onResetFilter()
}