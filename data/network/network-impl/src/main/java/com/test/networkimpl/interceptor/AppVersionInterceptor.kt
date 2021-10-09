package com.test.networkimpl.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AppVersionInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        return with(original.newBuilder()) {
            // TODO: 07.10.2021 Use different Version name
//            header(NetConstant.Header.APPLICATION_VERSION, BuildConfig.VERSION_NAME)
            method(original.method, original.body)
            chain.proceed(build())
        }
    }
}