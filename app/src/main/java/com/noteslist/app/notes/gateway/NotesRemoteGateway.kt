package com.noteslist.app.notes.gateway

import com.noteslist.app.notes.models.view.Note
import io.reactivex.Completable
import io.reactivex.Single

interface NotesRemoteGateway {

    fun getNotes(): Single<List<Note>>

    fun addNote(text: String): Single<Note>

    fun saveNote(note: Note): Single<Note>

    fun deleteNote(id: String): Completable
}