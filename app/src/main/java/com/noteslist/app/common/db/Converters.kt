package com.noteslist.app.common.db

import androidx.room.TypeConverter
import org.joda.time.DateTime

/**
 * Contains converter functions for Room for not supported data types in SQLite
 */
class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): DateTime? {
        return value?.let { DateTime(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: DateTime?): Long? {
        return date?.millis
    }
}