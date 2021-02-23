package com.test.thecocktaildb.feature.auth

import com.github.terrakok.cicerone.Router
import com.test.auth.api.AuthNavigationApi
import com.test.common.Event
import com.test.thecocktaildb.di.DiConstant
import com.test.thecocktaildb.navigation.routing.Screen
import com.test.thecocktaildb.navigation.state.NavigationStateHolder
import javax.inject.Inject
import javax.inject.Named

class AuthNavigationImpl @Inject constructor(
    @param:Named(DiConstant.GLOBAL) private val router: Router,
    private val navigationStateHolder: NavigationStateHolder
) : AuthNavigationApi {

    override fun toTabHost() {
        if (navigationStateHolder.deepLinkModel == null)
            router.replaceScreen(Screen.tabHost())
        else
            navigationStateHolder.deferredDeepLinkEvent.value =
                Event(navigationStateHolder.deepLinkModel!!)
    }
}