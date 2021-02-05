package com.test.splash.navigation

import android.os.Bundle

interface SplashNavigationApi {

    fun toAuth()

    fun toCocktail(navigationData: Bundle)
}