package com.test.thecocktaildb.feature.tabhost

import com.github.terrakok.cicerone.Router
import com.test.common.Event
import com.test.tabhost.api.TabHostNavigationApi
import com.test.thecocktaildb.di.DiConstant
import com.test.thecocktaildb.navigation.routing.Screen
import com.test.thecocktaildb.navigation.state.NavigationStateHolder
import javax.inject.Inject
import javax.inject.Named

class TabHostNavigationImpl @Inject constructor(
    @param:Named(DiConstant.GLOBAL) private val router: Router,
    navigationStateHolder: NavigationStateHolder
): TabHostNavigationApi {

    override val selectedTabEvent: Event<Int>? = navigationStateHolder.tabHostSelectedTabEvent

    override fun toCocktailSearch() {
        router.navigateTo(Screen.search())
    }

    override fun toCocktailDetail(cocktailId: Long) {
        router.navigateTo(Screen.detail(cocktailId))
    }

    override fun toProfile() {
        router.navigateTo(Screen.profile())
    }

    override fun toCube() {
        router.navigateTo(Screen.cube())
    }

    override fun toSeekBar() {
        router.navigateTo(Screen.seekBar())
    }

    override fun exit() {
        router.exit()
    }
}