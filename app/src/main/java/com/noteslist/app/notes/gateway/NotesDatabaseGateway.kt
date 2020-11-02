package com.noteslist.app.notes.gateway

import com.noteslist.app.notes.db.NotesDao
import com.noteslist.app.notes.models.view.Note
import com.noteslist.app.notes.models.NotesMapper
import io.reactivex.Completable
import io.reactivex.Flowable

class NotesDatabaseGateway(private val notesDao: NotesDao) : NotesLocalGateway {
    override fun getNotes(): Flowable<List<Note>> =
        notesDao.getAllFlowable().map {
            NotesMapper.fromDatabase(it)
        }

    override fun saveNotes(notes: List<Note>): Completable =
        notesDao.insert(NotesMapper.toDatabase(notes))

    override fun saveNote(note: Note): Completable =
        notesDao.insert(NotesMapper.toDatabase(note))

    override fun deleteNote(id: String): Completable =
        notesDao.delete(id)

    override fun deleteAll(): Completable =
        notesDao.deleteAll()
}