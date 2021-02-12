package com.test.thecocktaildb.navigation.feature

import com.github.terrakok.cicerone.Router
import com.test.search.navigation.SearchNavigationApi
import com.test.thecocktaildb.di.DiConstant
import com.test.thecocktaildb.navigation.routing.Screen
import javax.inject.Inject
import javax.inject.Named

class SearchNavigationImpl @Inject constructor(
    @param:Named(DiConstant.GLOBAL) private val router: Router
): SearchNavigationApi {

    override fun toCocktailDetail(actionBarTitle: String, cocktailId: Long) {
        router.navigateTo(Screen.detail(actionBarTitle, cocktailId))
    }
}