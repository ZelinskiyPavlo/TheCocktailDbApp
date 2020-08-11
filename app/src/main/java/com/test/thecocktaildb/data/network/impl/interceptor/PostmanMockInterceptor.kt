package com.test.thecocktaildb.data.network.impl.interceptor

import com.test.thecocktaildb.data.network.NetConstant
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class PostmanMockInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder = original.newBuilder()
        if (original.method != "GET")
            builder.addHeader(NetConstant.Header.X_MOCK_MATCH_REQUEST_BODY, "true")
        builder.method(original.method, original.body)
        return chain.proceed(builder.build())
    }
}