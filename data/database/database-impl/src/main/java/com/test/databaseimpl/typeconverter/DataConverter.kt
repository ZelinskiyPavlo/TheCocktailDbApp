package com.test.databaseimpl.typeconverter

import androidx.room.TypeConverter
import java.util.Date

/**
 * [TypeConverter] for long to [Date]
 * This stores the date as a long in the database, but returns it as a [Date]
 */
@Suppress("unused")
internal class DateConverter {

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return if (timestamp == null) null else Date(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}