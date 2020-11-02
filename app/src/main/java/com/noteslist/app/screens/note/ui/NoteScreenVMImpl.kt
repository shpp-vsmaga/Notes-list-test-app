package com.noteslist.app.screens.note.ui

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.noteslist.app.common.arch.BaseViewModel
import com.noteslist.app.common.livedata.SingleLiveEvent
import com.noteslist.app.common.livedata.SizeRangeTextLiveData
import com.noteslist.app.common.livedata.TextChangedLiveData
import com.noteslist.app.notes.models.Note
import com.noteslist.app.notes.useCases.NotesUseCases
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class NoteScreenVMImpl(private val notesUseCases: NotesUseCases) : BaseViewModel(), NoteScreenVM {

    private val _noteData = MutableLiveData<Note>()
    override val noteData: LiveData<Note>
        get() = _noteData

    private val _noteScreenAction = SingleLiveEvent<NoteScreenVM.Companion.NoteScreenAction>()
    override val noteScreenAction: LiveData<NoteScreenVM.Companion.NoteScreenAction>
        get() = _noteScreenAction

    private val _noteTextData =
        SizeRangeTextLiveData(FieldsParams.NOTE_TEXT_SIZE_MIN, FieldsParams.NOTE_TEXT_SIZE_MAX)
    override val noteTextData: SizeRangeTextLiveData
        get() = _noteTextData

    private val originalTextData = MutableLiveData<String>()

    private val _textChangedData = TextChangedLiveData(noteTextData, originalTextData)
    override val textChangedData: LiveData<Boolean>
        get() = _textChangedData

    init {
        _noteScreenAction.value = NoteScreenVM.Companion.NoteScreenAction.SHOW_ADD_MODE
    }

    override fun setNote(note: Note) {
        _noteScreenAction.value = NoteScreenVM.Companion.NoteScreenAction.SHOW_EDIT_MODE
        _noteData.value = note
        originalTextData.value = note.text
        _noteTextData.value = note.text
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
                notesUseCases.saveNote(editedNote)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { showProgress() }
                    .doOnComplete { hideProgress() }
                    .doOnError { hideProgress() }
                    .subscribe({
                        _noteData.value = editedNote
                        _noteScreenAction.value = NoteScreenVM.Companion.NoteScreenAction.CLOSE
                    }, { error ->
                        if (error.cause is NetworkErrorException) {
                            _noteScreenAction.value =
                                NoteScreenVM.Companion.NoteScreenAction.SHOW_OFFLINE_MESSAGE
                        } else {
                            showError(error.message)
                        }
                        Timber.e(error)
                    })
                    .disposeOnCleared()
            }
        }
    }

    private fun addNewNote(text: String?) {
        text?.let {
            notesUseCases.addNote(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .doOnComplete { hideProgress() }
                .doOnError { hideProgress() }
                .subscribe({
                    _noteScreenAction.value = NoteScreenVM.Companion.NoteScreenAction.CLOSE
                }, { error ->
                    if (error.cause is NetworkErrorException) {
                        _noteScreenAction.value =
                            NoteScreenVM.Companion.NoteScreenAction.SHOW_OFFLINE_MESSAGE
                    } else {
                        showError(error.message)
                    }
                    Timber.e(error)
                })
                .disposeOnCleared()
        }
    }

    override fun deleteNote() {
        noteData.value?.let { note ->
            notesUseCases.deleteNote(note.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .doOnComplete { hideProgress() }
                .doOnError { hideProgress() }
                .subscribe({
                    _noteScreenAction.value = NoteScreenVM.Companion.NoteScreenAction.CLOSE
                }, { error ->
                    if (error.cause is NetworkErrorException) {
                        _noteScreenAction.value =
                            NoteScreenVM.Companion.NoteScreenAction.SHOW_OFFLINE_MESSAGE
                    } else {
                        showError(error.message)
                    }
                    Timber.e(error)
                })
                .disposeOnCleared()
        }
    }
}