package com.despaircorp.ui.main.estate_form.estate_type

interface EstateTypeListener {
    fun onAddEstateTypeClicked(
        id: Int,
    )
    
    fun onRemoveEstateTypeClicked(
        id: Int,
    )
    
    fun onTypeClicked(
        id: Int,
    )
}