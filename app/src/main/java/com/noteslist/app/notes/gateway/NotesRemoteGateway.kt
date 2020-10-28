package com.noteslist.app.notes.gateway

import com.noteslist.app.notes.models.Note
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface NotesRemoteGateway {

    fun getNotes(): Flowable<List<Note>>

    fun addNote(text: String): Single<Note>

    fun saveNote(note: Note): Single<Note>

    fun deleteNote(id: String): Completable
}