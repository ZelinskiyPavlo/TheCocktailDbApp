package com.test.thecocktaildb.feature.tabhost

import com.github.terrakok.cicerone.Router
import com.test.common.Event
import com.test.tabhost.api.TabHostNavigationApi
import com.test.thecocktaildb.di.DiConstant
import com.test.thecocktaildb.navigation.routing.Screen
import javax.inject.Inject
import javax.inject.Named

class TabHostNavigationImpl @Inject constructor(
    @param:Named(DiConstant.GLOBAL) private val router: Router
): TabHostNavigationApi {

    override fun toCocktailSearch() {
        router.navigateTo(Screen.search())
    }

    override fun toCocktailDetail(actionBarTitle: String, cocktailId: Long) {
        router.navigateTo(Screen.detail(actionBarTitle, cocktailId))
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