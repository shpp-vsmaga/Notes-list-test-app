package com.noteslist.app.notes.useCases

import android.accounts.NetworkErrorException
import com.noteslist.app.common.network.ConnectivityHelper
import com.noteslist.app.notes.gateway.NotesLocalGateway
import com.noteslist.app.notes.gateway.NotesRemoteGateway
import com.noteslist.app.notes.models.Note
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class NotesUseCases(
    private val notesRemoteGateway: NotesRemoteGateway,
    private val notesLocalGateway: NotesLocalGateway,
    private val connectivityHelper: ConnectivityHelper
) {

    fun getNotes(): Flowable<List<Note>> =
        notesLocalGateway.getNotes()

    fun fetchNotes(): Completable =
        if (connectivityHelper.isOnline) {
            notesRemoteGateway.getNotes()
                .subscribeOn(Schedulers.io())
                .flatMapCompletable {
                    notesLocalGateway.saveNotes(it)
                }
        } else {
            Completable.complete()
        }


    fun addNote(text: String): Completable =
        if (connectivityHelper.isOnline) {
            notesRemoteGateway.addNote(text)
                .subscribeOn(Schedulers.io())
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
}