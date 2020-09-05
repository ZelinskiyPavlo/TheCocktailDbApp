package com.test.thecocktaildb.presentation.extension

import android.content.res.TypedArray
import android.graphics.Color

fun TypedArray.getColorAttributeOrNull(index: Int): Int? {
    return if (hasValue(index)) getColor(index, Color.RED)
    else null
}

fun TypedArray.getDimensionAttributeOrNull(index: Int): Float? {
    return if (hasValue(index)) getDimension(index, -100f)
    else null
}

fun TypedArray.getFloatAttributeOrNull(index: Int): Float? {
    return if (hasValue(index)) getFloat(index, 100f)
    else null
}

fun TypedArray.getBooleanAttributeOrNull(index: Int): Boolean? {
    return if (hasValue(index)) getBoolean(index, false)
    else null
}