package com.noteslist.app.notes.useCases

import android.accounts.NetworkErrorException
import com.noteslist.app.common.network.ConnectivityHelper
import com.noteslist.app.notes.gateway.NotesLocalGateway
import com.noteslist.app.notes.gateway.NotesRemoteGateway
import com.noteslist.app.notes.models.view.Note
import io.reactivex.Completable
import io.reactivex.Flowable

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
     * @return RX Flowable will always contain actual data from the local storage
     */
    fun getNotes(): Flowable<List<Note>> =
        notesLocalGateway.getNotes()

    /**
     * Used to force update of notes list in local storage from the remote
     * @return RX Completable that signals about operation complete or error
     */
    fun fetchNotes(): Completable =
        if (connectivityHelper.isOnline) {
            notesRemoteGateway.getNotes()
                .flatMapCompletable {
                    notesLocalGateway.saveNotes(it)
                }
        } else {
            Completable.complete()
        }

    /**
     * Used to add new note
     * @param text of new note
     * @return RX Completable that signals about operation complete or error
     */
    fun addNote(text: String): Completable =
        if (connectivityHelper.isOnline) {
            notesRemoteGateway.addNote(text)
                .flatMapCompletable {
                    notesLocalGateway.saveNote(it)
                }
        } else {
            Completable.error {
                Exception(
                    NetworkErrorException()
                )
            }
        }

    /**
     * Used to edit existing note
     * @param note - note model to be edited with new fields values
     * @return RX Completable that signals about operation complete or error
     */
    fun saveNote(note: Note): Completable =
        if (connectivityHelper.isOnline) {
            notesRemoteGateway.saveNote(note)
                .flatMapCompletable {
                    notesLocalGateway.saveNote(it)
                }
        } else {
            Completable.error {
                Exception(
                    NetworkErrorException()
                )
            }
        }

    /**
     * Used to delete existing note
     * @param id - id of the note model to be deleted
     * @return RX Completable that signals about operation complete or error
     */
    fun deleteNote(id: String): Completable =
        if (connectivityHelper.isOnline) {
            notesRemoteGateway.deleteNote(id)
                .andThen(notesLocalGateway.deleteNote(id))
        } else {
            Completable.error {
                Exception(
                    NetworkErrorException()
                )
            }
        }

    /**
     * Used to clear local storage on logout
     * @return RX Completable that signals about operation complete or error
     */
    fun clearLocalCache(): Completable =
        notesLocalGateway.deleteAll()

}