package com.noteslist.app.common.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class TextChangedLiveData(
    private val textLiveData: LiveData<String>,
    private val originalTextLiveData: LiveData<String>
) : LiveData<Boolean>() {

    private val textObserver = Observer<String> {
        checkChanges()
    }

    private val originalTextObserver = Observer<String> {
        checkChanges()
    }

    private fun checkChanges() {
        value =
            textLiveData.value != originalTextLiveData.value
    }

    override fun onActive() {
        super.onActive()
        textLiveData.observeForever(textObserver)
        originalTextLiveData.observeForever(originalTextObserver)
    }

    override fun onInactive() {
        super.onInactive()
        textLiveData.removeObserver(textObserver)
        originalTextLiveData.removeObserver(originalTextObserver)
    }
}