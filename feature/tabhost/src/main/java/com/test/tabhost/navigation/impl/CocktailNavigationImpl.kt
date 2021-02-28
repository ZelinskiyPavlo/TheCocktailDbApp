package com.test.tabhost.navigation.impl

import com.test.cocktail.api.CocktailNavigationApi
import com.test.tabhost.api.TabHostNavigationApi
import javax.inject.Inject

internal class CocktailNavigationImpl @Inject constructor(
    private val tabHostNavigationApi: TabHostNavigationApi
): CocktailNavigationApi {

    override fun toCocktailSearch() {
        tabHostNavigationApi.toCocktailSearch()
    }

    override fun toCocktailDetail(cocktailId: Long) {
        tabHostNavigationApi.toCocktailDetail(cocktailId)
    }

    override fun exit() {
        tabHostNavigationApi.exit()
    }
}