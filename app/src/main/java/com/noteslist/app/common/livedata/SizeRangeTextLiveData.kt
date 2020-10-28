package com.noteslist.app.common.livedata

import androidx.lifecycle.MutableLiveData

class SizeRangeTextLiveData(
    private val minSize: Int,
    private val maxSize: Int,
    private val checkIfEmpty: Boolean = true
) :
    MutableLiveData<String>() {

    //default value for empty is false, but if we should ignore empty values, we need
    //to set true as default value
    var isValid = !checkIfEmpty
        private set

    override fun setValue(value: String?) {
        val filteredValue = value?.trim()
        isValid = if (checkIfEmpty) {
            filteredValue?.isNotEmpty() == true && filteredValue.length >= minSize && filteredValue.length <= maxSize
        } else {
            (filteredValue == null || filteredValue.isEmpty()) || filteredValue.length in minSize..maxSize
        }
        super.setValue(value)
    }

    override fun postValue(value: String?) {
        val filteredValue = value?.trim()
        isValid = if (checkIfEmpty) {
            filteredValue?.isNotEmpty() == true && filteredValue.length >= minSize && filteredValue.length <= maxSize
        } else {
            (filteredValue == null || filteredValue.isEmpty()) || filteredValue.length in minSize..maxSize
        }
        super.postValue(value)
    }
}