package com.noteslist.app.common.db

import androidx.room.TypeConverter
import java.util.*

/**
 * Contains converter functions for Room for not supported data types in SQLite
 */
class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}