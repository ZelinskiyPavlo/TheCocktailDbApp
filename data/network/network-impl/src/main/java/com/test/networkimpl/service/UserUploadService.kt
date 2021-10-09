package com.test.networkimpl.service

import com.google.gson.JsonObject
import com.test.network.NetConstant.Header.TOKEN_HEADER
import com.test.networkimpl.constant.UrlParts
import okhttp3.MultipartBody
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

@JvmSuppressWildcards
interface UserUploadService {

    @Multipart
    @POST(UrlParts.Upload.uploadAvatarPart)
    @Headers(TOKEN_HEADER)
    suspend fun uploadUserAvatar(
        @Part avatar: MultipartBody.Part
    ): JsonObject
}