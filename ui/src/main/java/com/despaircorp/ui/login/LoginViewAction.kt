package com.despaircorp.ui.login

sealed class LoginViewAction {
    data class Error(val message: Int) : LoginViewAction()
    
    data object AlreadyLoggedInAgent : LoginViewAction()
    
    data object SuccessLogin : LoginViewAction()
}