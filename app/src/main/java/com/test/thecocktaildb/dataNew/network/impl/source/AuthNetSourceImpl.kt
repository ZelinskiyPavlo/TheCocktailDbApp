package com.test.thecocktaildb.dataNew.network.impl.source

import com.google.gson.JsonObject
import com.test.thecocktaildb.dataNew.network.impl.service.AuthApiService
import com.test.thecocktaildb.dataNew.network.impl.source.base.BaseNetSourceImpl
import com.test.thecocktaildb.dataNew.network.source.AuthNetSource
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
}