package com.test.preference.impl.source

import com.test.local.source.AppSettingLocalSource
import com.test.preference.impl.SharedPrefsHelper
import javax.inject.Inject

private const val EXTRA_KEY_SHOW_NAV_TITLE = "EXTRA_KEY_SHOW_NAV_TITLE"
private const val EXTRA_KEY_SELECTED_LANGUAGE = "EXTRA_KEY_SELECTED_LANGUAGE"

private const val DEFAULT_SHOW_NAV_TITLE = true
private const val DEFAULT_SELECTED_LANGUAGE = 0

class AppSettingLocalSourceImpl @Inject constructor(private val sharedPrefsHelper: SharedPrefsHelper) :
    AppSettingLocalSource {

    override fun observeShowNavigationTitle() = sharedPrefsHelper.observeKey(
        EXTRA_KEY_SHOW_NAV_TITLE, DEFAULT_SHOW_NAV_TITLE
    )

    override var showNavigationTitle by sharedPrefsHelper.stateHandle(
        EXTRA_KEY_SHOW_NAV_TITLE,
        DEFAULT_SHOW_NAV_TITLE
    )

    override fun observeCurrentLanguage() =
        sharedPrefsHelper.observeKey(EXTRA_KEY_SELECTED_LANGUAGE, DEFAULT_SELECTED_LANGUAGE)

    override var currentLanguage by sharedPrefsHelper.stateHandle(
        EXTRA_KEY_SELECTED_LANGUAGE,
        DEFAULT_SELECTED_LANGUAGE
    )
}