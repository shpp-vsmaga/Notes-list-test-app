package com.noteslist.app.notes.models.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.noteslist.app.notes.models.db.NoteEntity.Companion.TABLE_NAME
import java.util.*


@Entity(
    tableName = TABLE_NAME
)
data class NoteEntity(
    @PrimaryKey
    val id: String,
    val text: String,
    val createdAt: Date
) {
    companion object {
        const val TABLE_NAME = "notes"
    }
}