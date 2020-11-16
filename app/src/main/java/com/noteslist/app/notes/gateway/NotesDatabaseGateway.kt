package com.noteslist.app.notes.gateway

import com.noteslist.app.notes.db.NotesDao
import com.noteslist.app.notes.models.NotesMapper
import com.noteslist.app.notes.models.view.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of NotesLocalGateway based on the Room ORM
 */
class NotesDatabaseGateway(private val notesDao: NotesDao) : NotesLocalGateway {

    /**
     * Flowable will always contain the actual data from DB if it was changed with
     * the save instance
     */
    override fun getNotes(): Flow<List<Note>> =
        notesDao.getAllFlow().map {
            NotesMapper.fromDatabase(it)
        }

    override suspend fun saveNotes(notes: List<Note>) =
        notesDao.insert(NotesMapper.toDatabase(notes))

    override suspend fun saveNote(note: Note) =
        notesDao.insert(NotesMapper.toDatabase(note))

    override suspend fun deleteNote(id: String) =
        notesDao.delete(id)

    override suspend fun deleteAll() =
        notesDao.deleteAll()
}