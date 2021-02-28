package com.test.network.source

import com.test.network.source.base.BaseNetSource
import java.io.File

interface UserUploadNetSource : BaseNetSource {

    suspend fun updateUserAvatar(
        avatar: File,
        onProgressChanged: (fraction: Float, processedLength: Long, totalLength: Long) -> Unit = { _, _, _ -> }
    ): String
}