package com.test.repository.source

import androidx.lifecycle.LiveData
import com.test.repository.source.base.BaseRepository

interface TokenRepository : BaseRepository {

    val authTokenLiveData: LiveData<String?>
    var authToken: String?

    var firebaseToken: String?

}