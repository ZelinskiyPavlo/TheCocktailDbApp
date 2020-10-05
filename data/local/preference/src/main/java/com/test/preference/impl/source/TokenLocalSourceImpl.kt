package com.test.preference.impl.source

import androidx.lifecycle.LiveData
import com.test.local.source.TokenLocalSource
import com.test.preference.impl.SharedPrefsHelper
import com.test.preference.impl.base.BaseLocalSourceImpl
import javax.inject.Inject

class TokenLocalSourceImpl @Inject constructor(preferences: SharedPrefsHelper) :
    BaseLocalSourceImpl(preferences),
    TokenLocalSource {

    override val tokenLiveData: LiveData<String?> = sharedPrefLiveData(TOKEN, "")

    override var token: String? = sharedPrefsHelper.get(TOKEN, null)
        get() = sharedPrefsHelper.get(TOKEN, field)
        set(value) {
            sharedPrefsHelper.set(TOKEN, value)
        }

    companion object {
        const val TOKEN = "TOKEN"
    }
}