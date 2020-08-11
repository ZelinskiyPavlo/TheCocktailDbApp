package com.test.thecocktaildb.data.repository.impl.source

import androidx.lifecycle.LiveData
import com.test.thecocktaildb.data.local.source.TokenLocalSource
import com.test.thecocktaildb.data.repository.impl.source.base.BaseRepositoryImpl
import com.test.thecocktaildb.data.repository.source.TokenRepository
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val localSource: TokenLocalSource,
) : BaseRepositoryImpl(), TokenRepository {

    override val tokenLiveData: LiveData<String?> = localSource.tokenLiveData

    override var token: String?
        get() = localSource.token
        set(value) { localSource.token = value }

}