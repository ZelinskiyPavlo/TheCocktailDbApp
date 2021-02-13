package com.test.tabhost.navigation.inner

import com.test.cocktail.navigation.CocktailNavigationApi
import com.test.tabhost.navigation.TabHostNavigationApi
import javax.inject.Inject

class CocktailNavigationImpl @Inject constructor(
    private val tabHostNavigationApi: TabHostNavigationApi
): CocktailNavigationApi {

    override fun toCocktailSearch() {
        tabHostNavigationApi.toCocktailSearch()
    }

    override fun toCocktailDetail(actionBarTitle: String, cocktailId: Long) {
        tabHostNavigationApi.toCocktailDetail(actionBarTitle, cocktailId)
    }

    override fun exit() {
        tabHostNavigationApi.exit()
    }
}