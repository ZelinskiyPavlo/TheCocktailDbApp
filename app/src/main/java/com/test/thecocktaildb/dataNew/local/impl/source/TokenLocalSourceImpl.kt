package com.test.thecocktaildb.dataNew.local.impl.source

import androidx.lifecycle.LiveData
import com.test.thecocktaildb.dataNew.local.impl.SharedPrefsHelper
import com.test.thecocktaildb.dataNew.local.impl.base.BaseLocalSourceImpl
import com.test.thecocktaildb.dataNew.local.source.TokenLocalSource
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