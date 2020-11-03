package com.noteslist.app.screens.auth.ui

import androidx.lifecycle.LiveData
import com.noteslist.app.common.livedata.SingleLiveEvent

class AuthScreenVMImpl : AuthScreenVM() {
    private val _signInEvent = SingleLiveEvent<Companion.SignInEvent>()
    override val signInEvent: LiveData<Companion.SignInEvent>
        get() = _signInEvent

    override fun login() {
        _signInEvent.value = Companion.SignInEvent.OPEN_SIGN_IN
    }

    override fun signInSuccess() {
        _signInEvent.value = Companion.SignInEvent.SIGN_IN_SUCCESS
    }

    override fun signInError(message: String?) {
        showError(message)
    }
}