package com.test.thecocktaildb.dataNew.db.impl.source

import androidx.lifecycle.MutableLiveData
import com.test.thecocktaildb.dataNew.db.impl.SharedPrefsHelper
import com.test.thecocktaildb.dataNew.db.source.AppSettingLocalSource
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

    companion object Keys {
        private const val EXTRA_KEY_SHOW_NAV_TITLE = "EXTRA_KEY_SHOW_NAV_TITLE"
    }
}