package com.noteslist.app.common.livedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class EmailPasswordValidLiveData(
    private val emailLiveData: EmailLiveData,
    private val passwordLiveData: PasswordLiveData
) : MutableLiveData<Boolean>() {

    private val emailObserver = Observer<String> {
        updateValue()
    }

    private val passwordObserver = Observer<String> {
        updateValue()
    }

    private fun updateValue() {
        value =
            emailLiveData.isValid && passwordLiveData.isValid
    }


    override fun onActive() {
        super.onActive()
        emailLiveData.observeForever(emailObserver)
        passwordLiveData.observeForever(passwordObserver)
    }

    override fun onInactive() {
        super.onInactive()
        emailLiveData.removeObserver(emailObserver)
        passwordLiveData.removeObserver(passwordObserver)
    }
}