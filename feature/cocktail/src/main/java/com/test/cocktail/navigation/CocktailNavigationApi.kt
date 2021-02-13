package com.test.cocktail.navigation

interface CocktailNavigationApi {

    fun toCocktailSearch()

    fun toCocktailDetail(actionBarTitle: String, cocktailId: Long)

    fun exit()
}