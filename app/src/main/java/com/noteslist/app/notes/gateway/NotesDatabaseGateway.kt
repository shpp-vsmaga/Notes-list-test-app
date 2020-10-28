package com.noteslist.app.notes.gateway

import com.noteslist.app.notes.db.NotesDao
import com.noteslist.app.notes.models.Note
import com.noteslist.app.notes.models.NotesConverter
import io.reactivex.Completable
import io.reactivex.Flowable

class NotesDatabaseGateway(private val notesDao: NotesDao) : NotesLocalGateway {
    override fun getNotes(): Flowable<List<Note>> =
        notesDao.getAllFlowable().map {
            NotesConverter.fromDatabase(it)
        }

    override fun saveNotes(notes: List<Note>): Completable =
        notesDao.insert(NotesConverter.toDatabase(notes))

    override fun saveNote(note: Note): Completable =
        notesDao.insert(NotesConverter.toDatabase(note))

    override fun deleteNote(id: String): Completable =
        notesDao.delete(id)
}