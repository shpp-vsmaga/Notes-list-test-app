package com.noteslist.app.notes.gateway

import com.noteslist.app.notes.models.view.Note
import kotlinx.coroutines.flow.Flow

/**
 * Interface that describes a remote repository for storing notes
 */
interface NotesRemoteGateway {

    /**
     * Used to retrieve list of notes from the remote repo
     * @return RX Single with list of notes from the response
     */
    fun getNotes(): Flow<List<Note>>

    /**
     * Used to add new note to the remote repo
     * @param text of new note
     * @return RX Single with Note model, that contains added text and generated id in
     * the remote repo
     */
    suspend fun addNote(text: String): Note

    /**
     * Used to edit existing note in the remote repo
     * @param note - note model to be edited with new fields values
     * @return RX Single with Note model, that contains edited text
     */
    suspend fun saveNote(note: Note): Note

    /**
     * Used to delete existing note in the remote repo
     * @param id - id of the note model to be deleted from the remote repo
     * @return RX Single with Note model, that contains edited text
     */
    suspend fun deleteNote(id: String)
}