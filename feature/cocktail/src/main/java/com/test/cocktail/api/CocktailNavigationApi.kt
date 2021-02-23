package com.test.cocktail.api

interface CocktailNavigationApi {

    fun toCocktailSearch()

    fun toCocktailDetail(cocktailId: Long)

    fun exit()
}