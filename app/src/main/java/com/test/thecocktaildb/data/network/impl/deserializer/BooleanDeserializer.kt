package com.test.thecocktaildb.data.network.impl.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class BooleanDeserializer(private val defaultValue: Boolean? = false) : JsonDeserializer<Boolean> {

    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement?, typeOfT: Type,
        context: JsonDeserializationContext
    ): Boolean? {
        return if (json == null || json.isJsonNull || !json.isJsonPrimitive) defaultValue
        else doDeserialize(json)
    }

    private fun doDeserialize(json: JsonElement): Boolean? {
        return if (json.isJsonPrimitive) {
            val jsonPrimitive = json.asJsonPrimitive
            when {
                jsonPrimitive.isBoolean -> json.asBoolean
                jsonPrimitive.isNumber -> json.asInt == 1
                jsonPrimitive.isString -> {
                    when {
                        json.asString.equals("true", ignoreCase = true) -> true
                        json.asString.equals("false", ignoreCase = true) -> false
                        else -> defaultValue
                    }
                }
                else -> defaultValue
            }
        } else defaultValue
    }
}