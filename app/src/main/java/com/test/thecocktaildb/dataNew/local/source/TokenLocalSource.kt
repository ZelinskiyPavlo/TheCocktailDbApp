package com.test.thecocktaildb.dataNew.local.source

import androidx.lifecycle.LiveData

interface TokenLocalSource {
    val tokenLiveData: LiveData<String?>
    var token: String?
}