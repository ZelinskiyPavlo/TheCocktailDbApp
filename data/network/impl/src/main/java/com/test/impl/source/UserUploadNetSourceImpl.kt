package com.test.impl.source

import android.content.Context
import androidx.core.net.toUri
import com.test.impl.extension.getMemberStringOrEmpty
import com.test.impl.requestbody.progress.UploadProgressRequestBody.Companion.asProgressRequestBody
import com.test.impl.service.UserUploadService
import com.test.impl.source.base.BaseNetSourceImpl
import com.test.network.source.UserUploadNetSource
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