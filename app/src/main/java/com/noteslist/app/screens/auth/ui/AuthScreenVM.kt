package com.noteslist.app.screens.auth.ui

import androidx.lifecycle.LiveData
import com.noteslist.app.common.arch.BaseViewModel

abstract class AuthScreenVM : BaseViewModel() {
    abstract val signInEvent: LiveData<SignInEvent>

    abstract fun login()

    abstract fun signInSuccess()

    abstract fun signInError(message: String?)

    companion object {
        enum class SignInEvent {
            OPEN_SIGN_IN, SIGN_IN_SUCCESS
        }
    }
}