package com.test.thecocktaildb.data.network.impl.extension

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File

fun Uri.getName(context: Context): String? {
    return when (scheme) {
        ContentResolver.SCHEME_CONTENT -> {
            context
                .contentResolver
                .query(this, null, null, null, null)
                ?.use { cursor ->
                    cursor.moveToFirst()
                    cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
        }

        ContentResolver.SCHEME_FILE -> {
            path?.run(::File)?.name
        }

        else -> null
    }
}