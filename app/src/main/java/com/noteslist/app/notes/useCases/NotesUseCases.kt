package com.noteslist.app.notes.useCases

import android.accounts.NetworkErrorException
import com.noteslist.app.common.network.ConnectivityHelper
import com.noteslist.app.notes.gateway.NotesLocalGateway
import com.noteslist.app.notes.gateway.NotesRemoteGateway
import com.noteslist.app.notes.models.view.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

/**
 * Contains all possible actions and business logic of Notes
 */
class NotesUseCases(
    private val notesRemoteGateway: NotesRemoteGateway,
    private val notesLocalGateway: NotesLocalGateway,
    private val connectivityHelper: ConnectivityHelper
) {

    /**
     * Used to get actual list of notes
     * @return  [Flow] will always contain actual data from the local storage
     */
    fun getNotes(): Flow<List<Note>> =
        notesLocalGateway.getNotes()

    /**
     * Used to force update of notes list in local storage from the remote
     */
    suspend fun fetchNotes() {
        if (connectivityHelper.isOnline) {
            val notes = notesRemoteGateway.getNotes()
            notes.collect {
                notesLocalGateway.saveNotes(it)
            }
        }
    }

    /**
     * Used to add new note
     * @param text of new note
     */
    suspend fun addNote(text: String) {
        if (connectivityHelper.isOnline) {
            val note = notesRemoteGateway.addNote(text)
            notesLocalGateway.saveNote(note)
        } else {
            throw Exception(NetworkErrorException())
        }
    }


    /**
     * Used to edit existing note
     * @param note - note model to be edited with new fields values
     */
    suspend fun saveNote(note: Note) {
        if (connectivityHelper.isOnline) {
            val editedNote = notesRemoteGateway.saveNote(note)
            notesLocalGateway.saveNote(editedNote)
        } else {
            throw Exception(NetworkErrorException())
        }
    }

    /**
     * Used to delete existing note
     * @param id - id of the note model to be deleted
     */
    suspend fun deleteNote(id: String) {
        if (connectivityHelper.isOnline) {
            notesRemoteGateway.deleteNote(id)
            notesLocalGateway.deleteNote(id)
        } else {
            throw Exception(NetworkErrorException())
        }
    }

    /**
     * Used to clear local storage on logout
     */
    suspend fun clearLocalCache() =
        notesLocalGateway.deleteAll()

}