package com.test.thecocktaildb.dataNew.repository.source

import androidx.lifecycle.LiveData
import com.test.thecocktaildb.dataNew.repository.source.base.BaseRepository

interface TokenRepository : BaseRepository {

    val tokenLiveData: LiveData<String?>
    var token: String?

}