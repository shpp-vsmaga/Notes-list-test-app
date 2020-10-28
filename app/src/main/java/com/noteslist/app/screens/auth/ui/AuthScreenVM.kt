package com.noteslist.app.screens.auth.ui

import android.util.Log
import androidx.lifecycle.LiveData
import com.noteslist.app.common.arch.BaseViewModel
import com.noteslist.app.common.livedata.EmailLiveData
import com.noteslist.app.common.livedata.EmailPasswordValidLiveData
import com.noteslist.app.common.livedata.PasswordLiveData
import com.noteslist.app.common.livedata.SingleLiveEvent

class AuthScreenVM : BaseViewModel() {
    private val _signInEvent = SingleLiveEvent<SignInEvent>()
    val signInEvent: LiveData<SignInEvent>
        get() = _signInEvent

    fun login() {
        _signInEvent.value = SignInEvent.OPEN_SIGN_IN
    }

    fun signInSuccess(userId: String) {
        Log.d("svcom", "user id - ${userId}")
        _signInEvent.value = SignInEvent.SIGN_IN_SUCCESS
    }

    fun signInError(message: String?){
        showError(message)
    }

    companion object {
        enum class SignInEvent {
            OPEN_SIGN_IN, SIGN_IN_SUCCESS
        }
    }
}