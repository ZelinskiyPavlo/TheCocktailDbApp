package com.test.thecocktaildb.dataNew.db.impl.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * [TypeConverter] for [List] of [String] to [String]
 * This stores the list of [String] as a [String] in the database, but returns it as a [List]
 */
@Suppress("unused")
internal class StringListToStringConverter {

    private val gson = Gson()
    private val listType = object : TypeToken<List<String>>() {}.type

    @TypeConverter
    fun dataToList(data: String?): List<String> {
        data ?: return Collections.emptyList()

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun listToData(list: List<String>): String {
        return gson.toJson(list)
    }
}