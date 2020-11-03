package com.noteslist.app.common.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Observable for checking a change of text value stored in other LiveData
 * Used for cases when we need to show some actions if original text was changed in the text input fields
 *
 * @param originalTextLiveData - LiveData contains original text editing
 * @param textLiveData - LiveData contain new update text from the text field
 */
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

    /**
     * Checks if new text value is differ from the original
     */
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