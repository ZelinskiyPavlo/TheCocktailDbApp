package com.test.preference.impl.source

import androidx.lifecycle.MutableLiveData
import com.test.local.source.AppSettingLocalSource
import com.test.preference.impl.SharedPrefsHelper
import javax.inject.Inject

class AppSettingLocalSourceImpl @Inject constructor(private val sharedPrefsHelper: SharedPrefsHelper):
    AppSettingLocalSource {

    override val showNavigationTitleLiveData: MutableLiveData<Boolean> =
        object : MutableLiveData<Boolean>(sharedPrefsHelper.getBoolean(EXTRA_KEY_SHOW_NAV_TITLE, true)) {
            override fun setValue(value: Boolean?) {
                super.setValue(value)
                return  sharedPrefsHelper.putBoolean(EXTRA_KEY_SHOW_NAV_TITLE, value ?: true)
            }
            override fun getValue(): Boolean? {
                return sharedPrefsHelper.getBoolean(EXTRA_KEY_SHOW_NAV_TITLE, true)
            }
        }

    override val currentLanguageLiveData: MutableLiveData<Int> =
        object : MutableLiveData<Int>(sharedPrefsHelper.getInt(EXTRA_KEY_SELECTED_LANGUAGE, 0)) {
            override fun setValue(value: Int?) {
                super.setValue(value)
                return  sharedPrefsHelper.putInt(EXTRA_KEY_SELECTED_LANGUAGE, value ?: 0)
            }
            override fun getValue(): Int? {
                return sharedPrefsHelper.getInt(EXTRA_KEY_SELECTED_LANGUAGE, 0)
            }
        }

    companion object Keys {
        private const val EXTRA_KEY_SHOW_NAV_TITLE = "EXTRA_KEY_SHOW_NAV_TITLE"
        const val EXTRA_KEY_SELECTED_LANGUAGE = "EXTRA_KEY_SELECTED_LANGUAGE"
    }
}