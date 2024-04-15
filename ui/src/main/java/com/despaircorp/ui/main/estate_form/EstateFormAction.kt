package com.despaircorp.ui.main.estate_form

sealed class EstateFormAction {
    data class OnError(val message: String) : EstateFormAction()
    
    data object Success : EstateFormAction()
}