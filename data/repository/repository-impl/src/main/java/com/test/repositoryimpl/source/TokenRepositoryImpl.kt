package com.test.repositoryimpl.source

import com.test.local.source.TokenLocalSource
import com.test.repository.source.TokenRepository
import com.test.repositoryimpl.source.base.BaseRepositoryImpl
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val localSource: TokenLocalSource,
) : BaseRepositoryImpl(), TokenRepository {

    override fun observeAuthToken() = localSource.observeAuthToken()

    override var authToken = localSource.authToken

    override fun observeFirebaseToken() = localSource.observeFirebaseToken()

    override var firebaseToken = localSource.firebaseToken
}