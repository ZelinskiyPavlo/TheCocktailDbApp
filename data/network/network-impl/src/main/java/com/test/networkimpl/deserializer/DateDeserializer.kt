package com.test.networkimpl.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.util.Date

class DateDeserializer : JsonDeserializer<Date> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement?, type: Type, context: JsonDeserializationContext): Date {
        return json?.asLong
            ?.run(::Date)
            ?: Date()
    }

}