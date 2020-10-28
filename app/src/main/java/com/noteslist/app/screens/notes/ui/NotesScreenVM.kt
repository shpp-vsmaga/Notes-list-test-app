package com.noteslist.app.screens.notes.ui

import androidx.lifecycle.LiveData
import com.noteslist.app.auth.useCases.AuthUseCases
import com.noteslist.app.common.arch.BaseViewModel
import com.noteslist.app.common.livedata.SingleLiveEvent

class NotesScreenVM(private val authUseCases: AuthUseCases) : BaseViewModel() {

    private val _notesEvent = SingleLiveEvent<NotesActions>()
    val notesEvent: LiveData<NotesActions>
        get() = _notesEvent


    fun logout() {
        authUseCases.logout()
            .subscribe({
                _notesEvent.value = NotesActions.LOGOUT
            }, {
                showError(it.message)
            })
            .disposeOnCleared()

    }

    companion object {
        enum class NotesActions {
            LOGOUT
        }
    }
}