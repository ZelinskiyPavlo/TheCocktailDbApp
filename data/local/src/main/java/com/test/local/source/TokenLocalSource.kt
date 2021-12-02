package com.test.local.source

import kotlinx.coroutines.flow.Flow

interface TokenLocalSource {

    fun observeAuthToken(): Flow<String>

    var authToken: String

    fun observeFirebaseToken(): Flow<String>

    var firebaseToken: String
}