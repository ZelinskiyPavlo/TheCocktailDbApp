package com.test.impl.interceptor

import com.test.network.NetConstant
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class PlatformInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        return with(original.newBuilder()) {
            header(NetConstant.Header.PLATFORM, "android")
            method(original.method, original.body)
            chain.proceed(build())
        }
    }
}