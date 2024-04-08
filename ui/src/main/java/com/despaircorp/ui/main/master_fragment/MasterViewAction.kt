package com.despaircorp.ui.main.master_fragment

sealed class MasterViewAction {
    data class ShowConnectedToInternetToast(val message: Int) : MasterViewAction()
}