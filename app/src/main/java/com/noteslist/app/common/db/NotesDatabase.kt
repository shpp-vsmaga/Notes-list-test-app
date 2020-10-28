package com.noteslist.app.common.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.noteslist.app.common.db.NotesDatabase.Companion.DATABASE_VERSION
import com.noteslist.app.notes.db.NoteEntity
import com.noteslist.app.notes.db.NotesDao


@Database(
    entities = [
        NoteEntity::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun notesDao(): NotesDao

    companion object {
        const val DATABASE_VERSION = 1
    }
}