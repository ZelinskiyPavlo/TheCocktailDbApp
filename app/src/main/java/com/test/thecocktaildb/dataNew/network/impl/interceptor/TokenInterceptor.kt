package com.test.thecocktaildb.dataNew.network.impl.interceptor

import com.test.thecocktaildb.dataNew.network.NetConstant
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class TokenInterceptor(
    private val getToken: () -> String?
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val token = original.header(NetConstant.Header.AUTHORIZATION)
        val request = original.newBuilder()

        if (token != null) {
            val tokenPref = getToken()

            if (token != tokenPref) {
                request.header(NetConstant.Header.AUTHORIZATION, tokenPref ?: "")
            }
        }

        request.method(original.method, original.body)
        return chain.proceed(request.build())
    }

}