package com.noteslist.app.notes.useCases

import com.noteslist.app.notes.gateway.NotesLocalGateway
import com.noteslist.app.notes.gateway.NotesRemoteGateway
import com.noteslist.app.notes.models.Note
import io.reactivex.Completable
import io.reactivex.Flowable

class NotesUseCases(
    private val notesRemoteGateway: NotesRemoteGateway,
    private val notesLocalGateway: NotesLocalGateway
) {

    fun getNotes(): Flowable<List<Note>> =
        notesLocalGateway.getNotes()

    fun fetchNotes(): Completable =
        notesRemoteGateway.getNotes()
            .flatMapCompletable {
                notesLocalGateway.saveNotes(it)
            }


    fun addNote(text: String): Completable =
        notesRemoteGateway.addNote(text)
            .flatMapCompletable {
                notesLocalGateway.saveNote(it)
            }


    fun saveNote(note: Note): Completable =
        notesRemoteGateway.saveNote(note).flatMapCompletable {
            notesLocalGateway.saveNote(it)
        }

    fun deleteNote(id: String): Completable =
        notesRemoteGateway.deleteNote(id)
            .andThen(notesLocalGateway.deleteNote(id))
}