package com.noteslist.app.screens.notes.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.noteslist.app.auth.useCases.AuthUseCases
import com.noteslist.app.common.livedata.SingleLiveEvent
import com.noteslist.app.notes.models.view.Note
import com.noteslist.app.notes.useCases.NotesUseCases
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

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
        notesUseCases.fetchNotes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Timber.e(it)
            })
            .disposeOnCleared()
    }

    private fun getNotes() {
        notesUseCases.getNotes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _notesListData.value = it
            }, {
                Timber.e(it)
            })
            .disposeOnCleared()
    }

    override fun logout() {
        notesUseCases.clearLocalCache()
            .andThen(authUseCases.logout())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _notesScreenAction.value = Companion.NotesScreenAction.LOGOUT_SUCCESS
            }, {
                showError(it.message)
            })
            .disposeOnCleared()

    }

    override fun addNote() {
        _notesScreenAction.value = Companion.NotesScreenAction.ADD_NOTE
    }

    override fun editNote(note: Note) {
        _openEditNoteAction.value = note
    }
}