package com.noteslist.app.notes.gateway

import com.noteslist.app.notes.models.Note
import io.reactivex.Completable
import io.reactivex.Flowable

interface NotesLocalGateway {

    fun getNotes(): Flowable<List<Note>>

    fun saveNotes(notes: List<Note>): Completable

    fun saveNote(note: Note): Completable

    fun deleteNote(id: String): Completable
}