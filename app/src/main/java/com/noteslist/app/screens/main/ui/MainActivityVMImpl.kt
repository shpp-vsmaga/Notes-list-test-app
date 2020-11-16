package com.noteslist.app.screens.main.ui

import com.noteslist.app.auth.useCases.AuthUseCases
import com.noteslist.app.common.livedata.SingleLiveEvent

class MainActivityVMImpl(private val authUseCases: AuthUseCases) : MainActivityVM() {

    override val isAuthorizedAction = SingleLiveEvent<Boolean>()

    init {
        checkIsAuthorized()
    }

    private fun checkIsAuthorized() {
        isAuthorizedAction.value = authUseCases.checkIsAuthorized()
    }
}