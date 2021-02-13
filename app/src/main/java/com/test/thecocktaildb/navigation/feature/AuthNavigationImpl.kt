package com.test.thecocktaildb.navigation.feature

import com.github.terrakok.cicerone.Router
import com.test.auth.navigation.outer.AuthNavigationApi
import com.test.thecocktaildb.di.DiConstant
import com.test.thecocktaildb.navigation.routing.Screen
import javax.inject.Inject
import javax.inject.Named

// TODO: 02.02.2021 maybe create BaseNavigation where i Inject Global router? UPD I think it's bad idea
class AuthNavigationImpl @Inject constructor(
    @param:Named(DiConstant.GLOBAL) private val router: Router
) : AuthNavigationApi {

    override fun toTabHost() {
        router.replaceScreen(Screen.tabHost())
    }
}