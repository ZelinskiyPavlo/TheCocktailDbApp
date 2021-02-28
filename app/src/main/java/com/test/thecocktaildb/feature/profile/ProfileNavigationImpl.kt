package com.test.thecocktaildb.feature.profile

import com.github.terrakok.cicerone.Router
import com.test.profile.api.ProfileNavigationApi
import com.test.thecocktaildb.di.DiConstant
import com.test.thecocktaildb.navigation.routing.Screen
import javax.inject.Inject
import javax.inject.Named

class ProfileNavigationImpl @Inject constructor(
    @param:Named(DiConstant.GLOBAL) private val router: Router
): ProfileNavigationApi {

    override fun exit() {
        router.exit()
    }

    override fun logOut() {
        router.newRootScreen(Screen.auth())
    }
}