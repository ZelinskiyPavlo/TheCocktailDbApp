package com.test.repository.source

import com.test.repository.source.base.BaseRepository
import kotlinx.coroutines.flow.Flow

interface TokenRepository : BaseRepository {

    fun observeAuthToken(): Flow<String>

    var authToken: String

    fun observeFirebaseToken(): Flow<String>

    var firebaseToken: String
}