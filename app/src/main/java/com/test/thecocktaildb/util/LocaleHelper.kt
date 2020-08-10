package com.test.thecocktaildb.util

import android.content.Context
import android.os.Build
import android.os.LocaleList
import java.util.*

const val LANG_SHARED_PREFS = "language_shared_preferences"
const val EXTRA_KEY_SELECTED_LANGUAGE = "extra_key_selected_language"

fun getSelectedLanguageIndex(context: Context): Int {
    val sharedPreferences = context
        .getSharedPreferences(LANG_SHARED_PREFS, Context.MODE_PRIVATE)
    return sharedPreferences.getInt(EXTRA_KEY_SELECTED_LANGUAGE, 0)
}


fun setLocale(context: Context) {
    val languageIndex = getSelectedLanguageIndex(context)

    val localeCode = when (LanguageType.values()[languageIndex]) {
        LanguageType.ENGLISH -> "en"
        LanguageType.UKRAINIAN -> "uk"
    }
    val locale = Locale(localeCode)

    Locale.setDefault(locale)
    val resources = context.resources
    val configuration = resources.configuration
    configuration.setLocale(locale)
    configuration.setLayoutDirection(locale)
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N /*24*/) {
        with(LocaleList(locale)) {
            LocaleList.setDefault(this)
            configuration.setLocales(this)
        }
    }
    resources.updateConfiguration(configuration, resources.displayMetrics)
}
