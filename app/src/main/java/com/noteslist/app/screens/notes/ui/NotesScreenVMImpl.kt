package com.noteslist.app.screens.notes.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.noteslist.app.auth.useCases.AuthUseCases
import com.noteslist.app.common.livedata.SingleLiveEvent
import com.noteslist.app.notes.models.view.Note
import com.noteslist.app.notes.useCases.NotesUseCases
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NotesScreenVMImpl(
    private val authUseCases: AuthUseCases, private val notesUseCases: NotesUseCases
) : NotesScreenVM() {

    private val _notesScreenAction = SingleLiveEvent<Companion.NotesScreenAction>()
    override val notesAction: LiveData<Companion.NotesScreenAction>
        get() = _notesScreenAction

    private val _notesListData = MutableLiveData<List<Note>>()
    override val notesListData: LiveData<List<Note>>
        get() = _notesListData

    private val _openEditNoteAction = SingleLiveEvent<Note>()
    override val openEditNoteAction: LiveData<Note>
        get() = _openEditNoteAction

    init {
        fetchNotes()
        getNotes()
    }

    private fun fetchNotes() {
        viewModelScope.launch {
            notesUseCases.fetchNotes()
        }
    }

    private fun getNotes() {
        runCoroutine {
            notesUseCases.getNotes()
                .collect {
                    _notesListData.value = it
                }
        }
    }

    override fun logout() {
        runCoroutine {
            notesUseCases.clearLocalCache()
            authUseCases.logout()
            _notesScreenAction.value = Companion.NotesScreenAction.LOGOUT_SUCCESS
        }
    }

    override fun addNote() {
        _notesScreenAction.value = Companion.NotesScreenAction.ADD_NOTE
    }

    override fun editNote(note: Note) {
        _openEditNoteAction.value = note
    }
}