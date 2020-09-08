package com.test.thecocktaildb.dataNew.db.impl.typeconverter

import androidx.room.TypeConverter
import java.util.*

/**
 * [TypeConverter] for [List] of [String] to [String]
 * This stores the list of [String] as a [String] in the database, but returns it as a [List]
 */
@Suppress("unused")
internal class StringListToStringConverter {

    @TypeConverter
    fun dataToList(data: String?): List<String> {
        data ?: return Collections.emptyList()

        return data.split(",")
    }

    @TypeConverter
    fun listToData(list: List<String>): String {
        return list.joinToString(",")
    }
}