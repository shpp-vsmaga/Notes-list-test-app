package com.noteslist.app.screens.notes.ui

import androidx.lifecycle.LiveData
import com.noteslist.app.common.arch.BaseViewModel
import com.noteslist.app.notes.models.view.Note

abstract class NotesScreenVM : BaseViewModel() {
    abstract val notesAction: LiveData<NotesScreenAction>
    abstract val openEditNoteAction: LiveData<Note>
    abstract val notesListData: LiveData<List<Note>>

    abstract fun logout()

    abstract fun addNote()

    abstract fun editNote(note: Note)

    companion object {
        enum class NotesScreenAction {
            LOGOUT_SUCCESS, ADD_NOTE
        }
    }
}