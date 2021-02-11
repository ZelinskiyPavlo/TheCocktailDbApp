package com.test.impl.service

import com.google.gson.JsonObject
import com.test.impl.constant.UrlParts
import com.test.network.model.response.TokenNetModel
import retrofit2.http.Body
import retrofit2.http.POST

@JvmSuppressWildcards
interface AuthApiService {

    @POST(UrlParts.Auth.loginPart)
    suspend fun signIn(@Body jsonObject: JsonObject): TokenNetModel

    @POST(UrlParts.Auth.registerPart)
    suspend fun signUp(@Body jsonObject: JsonObject): TokenNetModel

}