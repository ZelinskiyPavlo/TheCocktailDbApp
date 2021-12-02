package com.test.networkimpl.source

import android.content.Context
import androidx.core.net.toUri
import com.test.network.source.UserUploadNetSource
import com.test.networkimpl.extension.getMemberStringOrEmpty
import com.test.networkimpl.requestbody.progress.UploadProgressRequestBody.Companion.asProgressRequestBody
import com.test.networkimpl.service.UserUploadService
import com.test.networkimpl.source.base.BaseNetSourceImpl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import java.io.File

class UserUploadNetSourceImpl(
    private val context: Context,
    apiService: UserUploadService,
) : BaseNetSourceImpl<UserUploadService>(apiService),
    UserUploadNetSource {

    override suspend fun updateUserAvatar(
        avatar: File,
        onProgressChanged: (fraction: Float, processedLength: Long, totalLength: Long) -> Unit
    ): String {
        return performRequest {
            val mediaType = context.contentResolver.getType(avatar.toUri())?.toMediaTypeOrNull()
            uploadUserAvatar(
                MultipartBody.Part.createFormData(
                    "avatar",
                    avatar.name,
                    avatar.asProgressRequestBody(mediaType, onProgressChanged)
                )
            )
        }.getMemberStringOrEmpty("avatar")
    }
}