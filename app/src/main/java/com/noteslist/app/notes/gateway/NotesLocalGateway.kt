package com.noteslist.app.notes.gateway

import com.noteslist.app.notes.models.view.Note
import kotlinx.coroutines.flow.Flow

/**
 * Interface that describes a local repository for storing notes
 */
interface NotesLocalGateway {

    /**
     * Used to retrieve list of notes from the local repo
     * @return [Flow] ]with list of notes
     */
    fun getNotes(): Flow<List<Note>>

    /**
     * Used to save list of notes to the local repo
     * @param notes - list of note models to be saved
     */
    suspend fun saveNotes(notes: List<Note>)

    /**
     * Used to a single note to the local repo
     * @param note - model of Note to be saved
     */
    suspend fun saveNote(note: Note)

    /**
     * Used to delete single note from the local repo
     * @param id - of the Note in the local epo
     */
    suspend fun deleteNote(id: String)

    /**
     * Used to clear local repo of notes
     */
    suspend fun deleteAll()
}