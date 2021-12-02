package com.test.preference.impl.source

import com.test.local.source.TokenLocalSource
import com.test.preference.impl.SharedPrefsHelper
import javax.inject.Inject

const val EXTRA_KEY_AUTH_TOKEN = "AUTH_TOKEN"
const val EXTRA_KEY_FIREBASE_TOKEN = "FIREBASE_TOKEN"

const val DEFAULT_AUTH_TOKEN = ""
const val DEFAULT_FIREBASE_TOKEN = ""

class TokenLocalSourceImpl @Inject constructor(private val sharedPrefsHelper: SharedPrefsHelper) :
    TokenLocalSource {

    override fun observeAuthToken() =
        sharedPrefsHelper.observeKey(EXTRA_KEY_AUTH_TOKEN, DEFAULT_AUTH_TOKEN)

    override var authToken by sharedPrefsHelper.stateHandle(
        EXTRA_KEY_AUTH_TOKEN,
        DEFAULT_AUTH_TOKEN
    )

    override fun observeFirebaseToken() =
        sharedPrefsHelper.observeKey(EXTRA_KEY_FIREBASE_TOKEN, DEFAULT_FIREBASE_TOKEN)

    override var firebaseToken by sharedPrefsHelper.stateHandle(
        EXTRA_KEY_FIREBASE_TOKEN,
        DEFAULT_FIREBASE_TOKEN
    )
}