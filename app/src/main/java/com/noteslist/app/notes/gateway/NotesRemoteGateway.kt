package com.noteslist.app.notes.gateway

import com.noteslist.app.notes.models.view.Note
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Interface that describes a remote repository for storing notes
 */
interface NotesRemoteGateway {

    /**
     * Used to retrieve list of notes from the remote repo
     * @return RX Single with list of notes from the response
     */
    fun getNotes(): Single<List<Note>>

    /**
     * Used to add new note to the remote repo
     * @param text of new note
     * @return RX Single with Note model, that contains added text and generated id in
     * the remote repo
     */
    fun addNote(text: String): Single<Note>

    /**
     * Used to edit existing note in the remote repo
     * @param note - note model to be edited with new fields values
     * @return RX Single with Note model, that contains edited text
     */
    fun saveNote(note: Note): Single<Note>

    /**
     * Used to delete existing note in the remote repo
     * @param id - id of the note model to be deleted from the remote repo
     * @return RX Single with Note model, that contains edited text
     */
    fun deleteNote(id: String): Completable
}