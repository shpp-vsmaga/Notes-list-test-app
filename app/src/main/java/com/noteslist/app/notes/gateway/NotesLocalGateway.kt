package com.noteslist.app.notes.gateway

import com.noteslist.app.notes.models.view.Note
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Interface that describes a local repository for storing notes
 */
interface NotesLocalGateway {

    /**
     * Used to retrieve list of notes from the local repo
     * @return RX Flowable with list of notes
     */
    fun getNotes(): Flowable<List<Note>>

    /**
     * Used to save list of notes to the local repo
     * @param notes - list of note models to be saved
     * @return RX Completable that signals about operation complete or error
     */
    fun saveNotes(notes: List<Note>): Completable

    /**
     * Used to a single note to the local repo
     * @param note - model of Note to be saved
     * @return RX Completable that signals about operation complete or error
     */
    fun saveNote(note: Note): Completable

    /**
     * Used to delete single note from the local repo
     * @param id - of the Note in the local epo
     * @return RX Completable that signals about operation complete or error
     */
    fun deleteNote(id: String): Completable

    /**
     * Used to clear local repo of notes
     * @return RX Completable that signals about operation complete or error
     */
    fun deleteAll(): Completable
}