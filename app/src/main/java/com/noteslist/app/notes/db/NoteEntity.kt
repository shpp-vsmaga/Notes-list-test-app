package com.noteslist.app.notes.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.noteslist.app.notes.db.NoteEntity.Companion.TABLE_NAME


@Entity(
    tableName = TABLE_NAME
)
data class NoteEntity(
    @PrimaryKey
    val id: String,
    val text: String
) {
    companion object {
        const val TABLE_NAME = "notes"
    }
}