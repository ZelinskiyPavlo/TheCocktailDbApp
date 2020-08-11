package com.test.thecocktaildb.util.locale

import android.content.Context
import android.os.Build
import android.os.LocaleList
import com.test.thecocktaildb.data.local.LocalConstant
import com.test.thecocktaildb.data.local.impl.source.AppSettingLocalSourceImpl
import java.util.*

fun setLocale(context: Context) {
    val sharedPreferences =
        context.getSharedPreferences(LocalConstant.SHARED_PREFS, Context.MODE_PRIVATE)
    val languageIndex =
        sharedPreferences.getInt(AppSettingLocalSourceImpl.EXTRA_KEY_SELECTED_LANGUAGE, 0)

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
