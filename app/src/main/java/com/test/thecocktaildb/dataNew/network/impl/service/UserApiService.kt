package com.test.thecocktaildb.dataNew.network.impl.service

import com.test.thecocktaildb.dataNew.network.NetConstant.Header.TOKEN_HEADER
import com.test.thecocktaildb.dataNew.network.model.UserNetModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserApiService {

    @Headers(TOKEN_HEADER)
    @GET("users/profile")
    suspend fun getUser(): UserNetModel

    @Headers(TOKEN_HEADER)
    @POST("users/profile")
    suspend fun updateUser(@Body user: UserNetModel)

}