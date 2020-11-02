package com.noteslist.app.screens.note.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.noteslist.app.common.arch.BaseVM
import com.noteslist.app.common.livedata.SizeRangeTextLiveData
import com.noteslist.app.notes.models.Note

interface NoteScreenVM : BaseVM {

    val noteData: LiveData<Note>

    val noteTextData: MutableLiveData<String>

    val textChangedData: LiveData<Boolean>

    val noteScreenAction: LiveData<NoteScreenAction>

    fun setNote(note: Note)

    fun saveNote()

    fun deleteNote()

    companion object {
        enum class NoteScreenAction {
            CLOSE, SHOW_ADD_MODE, SHOW_EDIT_MODE, SHOW_OFFLINE_MESSAGE
        }
    }
}