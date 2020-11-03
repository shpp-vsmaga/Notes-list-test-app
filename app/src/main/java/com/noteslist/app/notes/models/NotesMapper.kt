package com.noteslist.app.notes.models

import com.noteslist.app.notes.models.db.NoteEntity
import com.noteslist.app.notes.models.view.Note

/**
 * Set of converter functions to map note data between different app layers - domain, view
 */
object NotesMapper {

    /**
     * Converts a list db models to models from the view layer
     */
    fun fromDatabase(notes: List<NoteEntity>?): List<Note> =
        notes?.map {
            fromDatabase(it)
        } ?: emptyList()

    /**
     * Converts single db model to model from the view layer
     */
    private fun fromDatabase(note: NoteEntity): Note =
        Note(
            id = note.id,
            text = note.text,
            createdAt = note.createdAt
        )
    /**
     * Converts a list models from view layer to list of DB models
     */
    fun toDatabase(notes: List<Note>): List<NoteEntity> =
        notes.map {
            toDatabase(it)
        }

    /**
     * Converts single model from view layer to DB model
     */
    fun toDatabase(note: Note): NoteEntity =
        NoteEntity(
            id = note.id,
            text = note.text,
            createdAt = note.createdAt
        )
}
