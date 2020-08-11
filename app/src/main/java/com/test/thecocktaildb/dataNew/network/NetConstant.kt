package com.test.thecocktaildb.dataNew.network

object NetConstant {

    object Header {
        const val AUTHORIZATION = "Authorization"
        const val PLATFORM = "Platform"
        const val PLATFORM_VERSION = "PlatformVersion"
        const val X_MOCK_MATCH_REQUEST_BODY = "x-mock-match-request-body"
        const val APPLICATION_VERSION = "Application-Version"
        const val TOKEN_HEADER = "$AUTHORIZATION: token for replace"
    }

    object BaseUrl {
        const val COCKTAIL = "https://www.thecocktaildb.com/api/json/v1/1/"

        const val Auth = "https://devlightschool.ew.r.appspot.com/"
    }
}