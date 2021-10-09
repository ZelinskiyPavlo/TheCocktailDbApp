package com.test.networkimpl.source

import com.google.gson.JsonObject
import com.test.network.source.AuthNetSource
import com.test.networkimpl.service.AuthApiService
import com.test.networkimpl.source.base.BaseNetSourceImpl
import javax.inject.Inject

class AuthNetSourceImpl @Inject constructor(apiService: AuthApiService) :
    BaseNetSourceImpl<AuthApiService>(apiService), AuthNetSource {

    override suspend fun signIn(email: String, password: String): String {
        return performRequest {
            signIn(
                JsonObject().apply {
                    addProperty("email", email)
                    addProperty("password", password)
                }
            ).token
        }
    }

    override suspend fun signUp(
        name: String,
        lastName: String,
        email: String,
        password: String
    ): String {
        return performRequest {
            signUp(
                JsonObject().apply {
                    addProperty("name", name)
                    addProperty("lastName", lastName)
                    addProperty("email", email)
                    addProperty("password", password)
                }
            ).token
        }
    }
}