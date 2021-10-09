package com.test.networkimpl.interceptor

import com.test.common.constant.BaseUrl
import com.test.networkimpl.BuildConfig
import com.test.networkimpl.constant.UrlParts
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

@Suppress("SpellCheckingInspection")
class MockResponseInterceptor: Interceptor {

    companion object {
        val loginOrRegisterJsonBody = """ {
            |"token":"fNHWovE1SV2d4pcc95o-u1:APA91bEQzB4_MtJEQhajI6qye3gqJOBb8H_XtmevWU8iVqxI5kwgYyGiQFXL67tfjhPcEhnQfuPNDCCnkQ9vwmi_n3ZRd_bgrZQySp6uHDSLKDl1ZxIuS6ASvRiqc6hA1IAMXjqPpIGj"
        } """
            /** Quick solution for pretty formatting in logger. Pipe character only indicates tab */
            .replace(" ", "")
            .replace("|", "\t")

        val getProfileJsonBody = """ {
            |"id": 1,
            |"email": "zelinskiypavlo@gmail.com",
            |"name": "Pavlo",
            |"lastName": "Zelinskyi",
            |"avatar": "https://memepedia.ru/wp-content/uploads/2019/06/stonks-template-768x576.png"
        } """
            .replace(" ", "")
            .replace("|", "\t")

        val uploadAvatarJsonBody = """ {
            |"avatar":"https://mars.nasa.gov/mars2020-raw-images/pub/ods/surface/sol/00000/ids/edr/browse/fcam/FLG_0000_0666952984_226ECM_N0010044AUT_04096_00_2I3J01_800.jpg"
        } """
            .replace(" ", "")
            .replace("|", "\t")
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        if (BuildConfig.DEBUG) {
            return chain.proceed(chain.request())
                .newBuilder()
                .code(200)
                .message("Mocked Response")
                .body(getMockJsonBody(chain).toResponseBody("application/json".toMediaType()))
                .build()
        } else {
            return chain.proceed(chain.request())
        }
    }

    private fun getMockJsonBody(chain: Interceptor.Chain): String {
        return when(chain.request().url.toString()) {
            BaseUrl.Auth.plus(UrlParts.Auth.loginPart) -> loginOrRegisterJsonBody
            BaseUrl.Auth.plus(UrlParts.Auth.registerPart) -> loginOrRegisterJsonBody
            BaseUrl.Auth.plus(UrlParts.Auth.profilePart) -> getProfileJsonBody
            BaseUrl.Auth.plus(UrlParts.Upload.uploadAvatarPart) -> uploadAvatarJsonBody
            else -> throw IllegalArgumentException("Cannot find suitable mock")
        }
    }
}