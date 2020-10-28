package com.noteslist.app.notes.models

import com.noteslist.app.notes.db.NoteEntity

object NotesConverter {
    fun fromDatabase(notes: List<NoteEntity>?): List<Note> =
        notes?.map {
            fromDatabase(it)
        } ?: emptyList()


    fun fromDatabase(note: NoteEntity): Note =
        Note(
            id = note.id,
            text = note.text
        )

    fun toDatabase(notes: List<Note>): List<NoteEntity> =
        notes.map {
            toDatabase(it)
        }


    fun toDatabase(note: Note): NoteEntity =
        NoteEntity(
            id = note.id,
            text = note.text
        )
}
