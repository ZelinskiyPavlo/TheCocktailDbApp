package com.test.thecocktaildb.dataNew.network.impl.service

import com.google.gson.JsonObject
import com.test.thecocktaildb.dataNew.network.model.response.TokenNetModel
import retrofit2.http.Body
import retrofit2.http.POST

@JvmSuppressWildcards
interface AuthApiService {

    @POST("login")
    suspend fun signIn(@Body jsonObject: JsonObject): TokenNetModel

    @POST("register")
    suspend fun signUp(@Body jsonObject: JsonObject): TokenNetModel

}