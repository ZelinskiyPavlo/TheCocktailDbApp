package com.test.impl.extension

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

/**
 * Extension property that converts model to [JsonObject].
 */
internal inline fun <reified T> T.toJson(gson: Gson): JsonObject {
    return gson.toJsonTree(this) as JsonObject
}

fun JsonElement?.asStringOrEmpty(): String {
    return if (this == null || this.isJsonNull) ""
    else this.asString
}

fun JsonObject.getIfHasMember(memberName: String): JsonElement? {
    return if (has(memberName) && get(memberName) != null && !get(memberName).isJsonNull) get(memberName) else null
}

fun JsonObject.getMemberStringOrEmpty(member: String): String {
    return when {
        !has(member) -> return ""
        else -> get(member).asStringOrEmpty()
    }
}

inline fun <reified T> deserializeType() = object : TypeToken<T>() {}.type