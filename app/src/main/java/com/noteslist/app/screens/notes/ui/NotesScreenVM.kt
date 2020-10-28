package com.noteslist.app.screens.notes.ui

import androidx.lifecycle.LiveData
import com.noteslist.app.common.arch.BaseVM
import com.noteslist.app.common.livedata.SingleLiveEvent
import com.noteslist.app.notes.models.Note

interface NotesScreenVM : BaseVM {
    val notesAction: LiveData<NotesScreenAction>
    val openEditNoteAction: LiveData<Note>
    val notesListData: LiveData<List<Note>>

    fun logout()

    fun addNote()

    fun editNote(note: Note)

    companion object {
        enum class NotesScreenAction {
            LOGOUT, ADD_NOTE
        }
    }
}