package com.despaircorp.ui.main

sealed class MainViewAction {
    data class Error(val message: Int) : MainViewAction()
    data class AgentCreationSuccess(val message: Int) : MainViewAction()
    data object SuccessDisconnection : MainViewAction()
}