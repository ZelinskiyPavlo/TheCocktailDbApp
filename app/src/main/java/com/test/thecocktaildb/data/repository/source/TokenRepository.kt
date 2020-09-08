package com.test.thecocktaildb.data.repository.source

import androidx.lifecycle.LiveData
import com.test.thecocktaildb.data.repository.source.base.BaseRepository

interface TokenRepository : BaseRepository {

    val tokenLiveData: LiveData<String?>
    var token: String?

}