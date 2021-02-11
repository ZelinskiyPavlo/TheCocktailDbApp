package com.test.impl.service

import com.test.impl.constant.UrlParts
import com.test.network.NetConstant.Header.TOKEN_HEADER
import com.test.network.model.UserNetModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserApiService {

    @Headers(TOKEN_HEADER)
    @GET(UrlParts.Auth.profilePart)
    suspend fun getUser(): UserNetModel

    @Headers(TOKEN_HEADER)
    @POST(UrlParts.Auth.profilePart)
    suspend fun updateUser(@Body user: UserNetModel)

}