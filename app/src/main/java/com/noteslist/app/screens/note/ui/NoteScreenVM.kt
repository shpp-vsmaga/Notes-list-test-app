package com.noteslist.app.screens.note.ui

import androidx.lifecycle.LiveData
import com.noteslist.app.common.arch.BaseViewModel
import com.noteslist.app.common.livedata.SizeRangeTextLiveData
import com.noteslist.app.notes.models.view.Note

abstract class NoteScreenVM : BaseViewModel() {

    abstract val noteData: LiveData<Note>

    abstract val noteTextData: SizeRangeTextLiveData

    abstract val textChangedData: LiveData<Boolean>

    abstract val noteScreenAction: LiveData<NoteScreenAction>

    abstract fun setNote(note: Note?)

    abstract fun saveNote()

    abstract fun deleteNote()

    companion object {
        enum class NoteScreenAction {
            CLOSE, SHOW_ADD_MODE, SHOW_EDIT_MODE, SHOW_OFFLINE_MESSAGE
        }
    }
}