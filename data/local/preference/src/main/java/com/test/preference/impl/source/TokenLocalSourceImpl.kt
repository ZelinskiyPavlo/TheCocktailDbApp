package com.test.preference.impl.source

import androidx.lifecycle.LiveData
import com.test.local.source.TokenLocalSource
import com.test.preference.impl.SharedPrefsHelper
import com.test.preference.impl.base.BaseLocalSourceImpl
import javax.inject.Inject

class TokenLocalSourceImpl @Inject constructor(preferences: SharedPrefsHelper) :
    BaseLocalSourceImpl(preferences),
    TokenLocalSource {

    override val authTokenLiveData: LiveData<String?> = sharedPrefLiveData(AUTH_TOKEN, "")

    override var authToken: String? = sharedPrefsHelper.get(AUTH_TOKEN, null)
        get() = sharedPrefsHelper.get(AUTH_TOKEN, field)
        set(value) {
            sharedPrefsHelper.set(AUTH_TOKEN, value)
        }

    override var firebaseToken: String? = sharedPrefsHelper.get(FIREBASE_TOKEN, null)
        get() = sharedPrefsHelper.get(FIREBASE_TOKEN, field)
        set(value) {
            sharedPrefsHelper.set(FIREBASE_TOKEN, value)
        }

    companion object {
        const val AUTH_TOKEN = "AUTH_TOKEN"
        const val FIREBASE_TOKEN = "FIREBASE_TOKEN"
    }
}