package com.despaircorp.ui.main.estate_addition

sealed class CreateEstateAction {
    data class OnError(val message: String) : CreateEstateAction()
    
    data object Success : CreateEstateAction()
}