package com.test.thecocktaildb.dataNew.network.impl.interceptor

import android.os.Build
import com.test.thecocktaildb.dataNew.network.NetConstant
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class PlatformVersionInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        return with(original.newBuilder()) {
            header(
                NetConstant.Header.PLATFORM_VERSION,
                Build.VERSION.RELEASE ?: "API ${Build.VERSION.SDK_INT}"
            )
            method(original.method, original.body)
            chain.proceed(build())
        }
    }
}