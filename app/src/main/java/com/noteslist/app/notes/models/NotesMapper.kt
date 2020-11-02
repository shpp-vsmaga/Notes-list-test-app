package com.noteslist.app.notes.models

import com.noteslist.app.notes.models.db.NoteEntity
import com.noteslist.app.notes.models.view.Note

object NotesMapper {
    fun fromDatabase(notes: List<NoteEntity>?): List<Note> =
        notes?.map {
            fromDatabase(it)
        } ?: emptyList()


    private fun fromDatabase(note: NoteEntity): Note =
        Note(
            id = note.id,
            text = note.text,
            createdAt = note.createdAt
        )

    fun toDatabase(notes: List<Note>): List<NoteEntity> =
        notes.map {
            toDatabase(it)
        }


    fun toDatabase(note: Note): NoteEntity =
        NoteEntity(
            id = note.id,
            text = note.text,
            createdAt = note.createdAt
        )
}
