package com.test.thecocktaildb.data.network.impl.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class Iso8601DateDeserializer : JsonDeserializer<Date> {

    private val dateTimeFormatNoTimeZone =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private val dateTimeFormatNoSec = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val regex = "(T|\\'T\\')".toRegex()

    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Date {
        val string = json.asString
            .replace(regex, " ")
            .trim()

        return tryParseDate(string)
    }

    @Synchronized
    private fun tryParseDate(string: String): Date {
        return runCatching { dateTimeFormatNoTimeZone.parse(string) }.getOrNull()
            ?: runCatching { dateTimeFormatNoSec.parse(string) }.getOrNull()
            ?: runCatching { dateFormat.parse(string) }
                .getOrElse { throw JsonParseException(it) }
    }

}
