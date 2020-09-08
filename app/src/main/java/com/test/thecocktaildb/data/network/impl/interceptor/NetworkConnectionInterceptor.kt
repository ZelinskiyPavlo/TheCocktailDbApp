package com.test.thecocktaildb.data.network.impl.interceptor

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.test.thecocktaildb.core.common.exception.NoInternetConnectionException
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor(
    private val connectivityManager: ConnectivityManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (isNetworkConnected()) {
            val original = chain.request()
            return with(original.newBuilder()) {
                method(original.method, original.body)
                chain.proceed(build())
            }
        } else throw NoInternetConnectionException()
    }

    private fun isNetworkConnected(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            capabilities != null
                    && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        } else {
            val activeNetwork = connectivityManager.activeNetworkInfo
            activeNetwork != null && activeNetwork.isConnected
        }
    }
}