package com.test.impl.interceptor

import com.test.network.NetConstant
import com.test.impl.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AppVersionInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        return with(original.newBuilder()) {
            header(NetConstant.Header.APPLICATION_VERSION, BuildConfig.VERSION_NAME)
            method(original.method, original.body)
            chain.proceed(build())
        }
    }
}