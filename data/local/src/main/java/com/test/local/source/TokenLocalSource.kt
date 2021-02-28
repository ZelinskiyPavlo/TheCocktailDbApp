package com.test.local.source

import androidx.lifecycle.LiveData

interface TokenLocalSource {
    val authTokenLiveData: LiveData<String?>
    var authToken: String?

    var firebaseToken: String?
}