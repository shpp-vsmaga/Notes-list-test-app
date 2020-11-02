package com.noteslist.app.common.livedata

import androidx.lifecycle.MutableLiveData

class SizeRangeTextLiveData(
    private val minSize: Int,
    private val maxSize: Int
) :
    MutableLiveData<String>() {

    //default value for empty is false
    var isValid = false
        private set

    override fun setValue(value: String?) {
        validate(value)
        super.setValue(value)
    }

    override fun postValue(value: String?) {
        validate(value)
        super.postValue(value)
    }

    private fun validate(value: String?) {
        val filteredValue = value?.trim()
        isValid = filteredValue?.isNotEmpty() == true && filteredValue.length in minSize..maxSize
    }
}