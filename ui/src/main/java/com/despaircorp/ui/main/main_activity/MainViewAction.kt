package com.despaircorp.ui.main.main_activity

sealed class MainViewAction {
    data class Error(val message: Int) : MainViewAction()
    data class AgentCreationSuccess(val message: Int) : MainViewAction()
    data object SuccessDisconnection : MainViewAction()
}