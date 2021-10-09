package com.test.repositoryimpl.source

import androidx.lifecycle.LiveData
import com.test.local.source.TokenLocalSource
import com.test.repository.source.TokenRepository
import com.test.repositoryimpl.source.base.BaseRepositoryImpl
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val localSource: TokenLocalSource,
) : BaseRepositoryImpl(), TokenRepository {

    override val authTokenLiveData: LiveData<String?> = localSource.authTokenLiveData

    override var authToken: String?
        get() = localSource.authToken
        set(value) { localSource.authToken = value }

    override var firebaseToken: String?
        get() = localSource.firebaseToken
        set(value) { localSource.firebaseToken = value }

}