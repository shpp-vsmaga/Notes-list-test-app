package com.noteslist.app.common.livedata

import androidx.lifecycle.MutableLiveData
import com.noteslist.app.common.utils.TextUtils.isEmailValid

class EmailLiveData : MutableLiveData<String>() {

    var isValid = false
        private set

    override fun setValue(value: String?) {
        isValid = isEmailValid(value)
        super.setValue(value)
    }

    override fun postValue(value: String?) {
        isValid = isEmailValid(value)
        super.postValue(value)
    }
}