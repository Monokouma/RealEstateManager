package com.despaircorp.ui.main.estate_addition.point_of_interest

interface PointOfInterestListener {
    fun onAddPointOfInterestClicked(
        id: Int,
    )
    
    fun onRemovePointOfInterestClicked(
        id: Int,
    )
    
}