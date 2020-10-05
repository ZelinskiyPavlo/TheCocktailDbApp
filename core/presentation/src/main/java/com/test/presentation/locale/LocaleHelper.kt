package com.test.presentation.locale

import android.content.Context
import android.os.Build
import android.os.LocaleList
import java.util.*

fun setLocale(context: Context) {
//    val sharedPreferences =
//        context.getSharedPreferences(LocalConstant.SHARED_PREFS, Context.MODE_PRIVATE)
//    val languageIndex =
//        // TODO: 12.09.2020 перемістити цю константу з LocalSource в Core common наприклад
//        sharedPreferences.getInt(AppSettingLocalSourceImpl.EXTRA_KEY_SELECTED_LANGUAGE, 0)
//
//    val localeCode = when (LanguageType.values()[languageIndex]) {
//        LanguageType.ENGLISH -> "en"
//        LanguageType.UKRAINIAN -> "uk"
//    }
//    val locale = Locale(localeCode)
//
//    Locale.setDefault(locale)
//    val resources = context.resources
//    val configuration = resources.configuration
//    configuration.setLocale(locale)
//    configuration.setLayoutDirection(locale)
//    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N /*24*/) {
//        with(LocaleList(locale)) {
//            LocaleList.setDefault(this)
//            configuration.setLocales(this)
//        }
//    }
//    resources.updateConfiguration(configuration, resources.displayMetrics)
}
