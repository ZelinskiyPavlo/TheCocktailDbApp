package com.test.impl.requestbody.progress

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*
import java.io.File
import java.io.IOException

class UploadProgressRequestBody(
    private val delegate: RequestBody,
    private val onProgressChanged: (fraction: Float, processedLength: Long, totalLength: Long) -> Unit = { _, _, _ -> }
) : RequestBody() {

    override fun contentType(): MediaType? {
        return delegate.contentType()
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return runCatching { delegate.contentLength() }.getOrElse { -1L }
    }

    var isFirstTime = true

    /**
     * This method would be called simultaneously for each interceptor added that modify request body
     * (usually logging interceptor).
     *
     * So in order to properly track upload progress it is enough to track first writeTo call
     */
    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val countingSink = when {
            isFirstTime -> CountingSink(sink)
            else -> ForwardingSinkImpl(sink)
        }
        isFirstTime = false
        val bufferedSink = countingSink.buffer()
        delegate.writeTo(bufferedSink)
        bufferedSink.flush()
    }

    inner class CountingSink(delegate: Sink) : ForwardingSink(delegate) {

        private var bytesWritten = 0L
        private val contentLength = contentLength()

        override fun write(source: Buffer, byteCount: Long) {
            super.write(source, byteCount)
            bytesWritten += byteCount
            onProgressChanged(
                bytesWritten / contentLength.toFloat(),
                bytesWritten,
                contentLength
            )
        }
    }

    class ForwardingSinkImpl(delegate: Sink) : ForwardingSink(delegate)

    companion object {
        //region Convenience Extension Functions
        fun RequestBody.asProgressRequestBody(
            onProgressChanged: (fraction: Float, processedLength: Long, totalLength: Long) -> Unit = { _, _, _ -> }
        ): RequestBody {
            return UploadProgressRequestBody(this, onProgressChanged)
        }

        fun File.asProgressRequestBody(
            mediaType: MediaType? = null,
            onProgressChanged: (fraction: Float, processedLength: Long, totalLength: Long) -> Unit = { _, _, _ -> }
        ): RequestBody {
            return this
                .asRequestBody(mediaType)
                .asProgressRequestBody(onProgressChanged)
        }
        //endregion
    }
}