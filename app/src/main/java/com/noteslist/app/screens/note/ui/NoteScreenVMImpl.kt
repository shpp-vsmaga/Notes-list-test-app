package com.noteslist.app.screens.note.ui

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.noteslist.app.common.livedata.SingleLiveEvent
import com.noteslist.app.common.livedata.SizeRangeTextLiveData
import com.noteslist.app.common.livedata.TextChangedLiveData
import com.noteslist.app.notes.models.view.Note
import com.noteslist.app.notes.useCases.NotesUseCases
import kotlin.coroutines.CoroutineContext

class NoteScreenVMImpl(private val notesUseCases: NotesUseCases) : NoteScreenVM() {

    private val _noteData = MutableLiveData<Note>()
    override val noteData: LiveData<Note>
        get() = _noteData

    private val _noteScreenAction = SingleLiveEvent<Companion.NoteScreenAction>()
    override val noteScreenAction: LiveData<Companion.NoteScreenAction>
        get() = _noteScreenAction

    private val _noteTextData =
        SizeRangeTextLiveData(FieldsParams.NOTE_TEXT_SIZE_MIN, FieldsParams.NOTE_TEXT_SIZE_MAX)
    override val noteTextData: SizeRangeTextLiveData
        get() = _noteTextData

    private val originalTextData = MutableLiveData<String>()

    private val _textChangedData = TextChangedLiveData(noteTextData, originalTextData)
    override val textChangedData: LiveData<Boolean>
        get() = _textChangedData

    override fun handleException(coroutineContext: CoroutineContext, error: Throwable) {
        if (error.cause is NetworkErrorException) {
            _noteScreenAction.value =
                Companion.NoteScreenAction.SHOW_OFFLINE_MESSAGE
        } else {
            showError(error.message)
        }
    }

    override fun setNote(note: Note?) {
        if (note != null) {
            _noteScreenAction.value = Companion.NoteScreenAction.SHOW_EDIT_MODE
            _noteData.value = note
            originalTextData.value = note.text
            _noteTextData.value = note.text
        } else {
            _noteScreenAction.value = Companion.NoteScreenAction.SHOW_ADD_MODE
        }
    }

    override fun saveNote() {
        if (_noteTextData.isValid) {
            if (_noteData.value != null) {
                savedEditedNote(_noteTextData.value)
            } else {
                addNewNote(_noteTextData.value)
            }
        }
    }

    private fun savedEditedNote(text: String?) {
        noteData.value?.let { note ->
            text?.let {
                val editedNote = note.copy(text = text)
                runCoroutine(withProgress = true) {
                    notesUseCases.saveNote(editedNote)
                    _noteData.value = editedNote
                    _noteScreenAction.value = Companion.NoteScreenAction.CLOSE
                }
            }
        }
    }

    private fun addNewNote(text: String?) {
        text?.let {
            runCoroutine(withProgress = true) {
                notesUseCases.addNote(text)
                _noteScreenAction.value = Companion.NoteScreenAction.CLOSE
            }
        }
    }

    override fun deleteNote() {
        noteData.value?.let { note ->
            runCoroutine(withProgress = true) {
                notesUseCases.deleteNote(note.id)
                _noteScreenAction.value = Companion.NoteScreenAction.CLOSE
            }
        }
    }
}