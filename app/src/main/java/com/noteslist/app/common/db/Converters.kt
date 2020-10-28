package com.noteslist.app.common.db

import androidx.room.TypeConverter
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.joda.time.DateTime

class Converters {

    @TypeConverter
    fun intToBoolean(value: Int) = value == TRUE

    @TypeConverter
    fun booleanToInt(value: Boolean) = if (value) TRUE else FALSE

    @TypeConverter
    fun fromString(value: String): List<String> {
        val mapper = jacksonObjectMapper()
        return mapper.readValue<List<String>>(value)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        val mapper = jacksonObjectMapper()
        return mapper.writeValueAsString(list)
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): DateTime? {
        return value?.let { DateTime(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: DateTime?): Long? {
        return date?.millis
    }

    companion object {
        const val TRUE = 1
        const val FALSE = 0
    }
}