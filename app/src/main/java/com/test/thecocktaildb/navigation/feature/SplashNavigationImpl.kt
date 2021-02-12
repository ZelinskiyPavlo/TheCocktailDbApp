package com.test.thecocktaildb.navigation.feature

import com.github.terrakok.cicerone.Router
import com.test.splash.navigation.SplashNavigationApi
import com.test.thecocktaildb.di.DiConstant.GLOBAL
import com.test.thecocktaildb.navigation.routing.Screen
import javax.inject.Inject
import javax.inject.Named

class SplashNavigationImpl @Inject constructor(
    @param:Named(GLOBAL) private val router: Router
) : SplashNavigationApi {

    override fun toAuth() {
        router.replaceScreen(Screen.auth())
    }

    override fun toTabHost() {
        router.replaceScreen(Screen.tabHost())
    }
}