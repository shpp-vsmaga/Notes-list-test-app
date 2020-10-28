package com.noteslist.app.common.livedata

import androidx.lifecycle.MutableLiveData

class PasswordLiveData : MutableLiveData<String>() {

    var isValid = false
        private set

    override fun setValue(value: String?) {
        updateValidState(value)
        super.setValue(value)
    }

    override fun postValue(value: String?) {
        updateValidState(value)
        super.postValue(value)
    }

    private fun updateValidState(value: String?) {
        isValid = value?.isNotEmpty() == true
    }
}