package com.noteslist.app.common.db

import androidx.room.TypeConverter
import org.joda.time.DateTime

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