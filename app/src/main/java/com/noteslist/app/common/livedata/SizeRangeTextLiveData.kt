package com.noteslist.app.common.livedata

import androidx.lifecycle.MutableLiveData

/**
 * Observable for storing a String values that have some limits to it's size
 * Have additional boolean field 'isValid' - true if text is in possible range of size
 *
 * @param minSize - minimum size for text
 * @param maxSize - maximum size for text
 */
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

    /**
     * Provide validation of new value to be in range of possible size
     */
    private fun validate(value: String?) {
        val filteredValue = value?.trim()
        isValid = filteredValue?.isNotEmpty() == true && filteredValue.length in minSize..maxSize
    }
}