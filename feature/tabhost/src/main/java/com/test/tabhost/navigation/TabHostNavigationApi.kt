package com.test.tabhost.navigation

interface TabHostNavigationApi {

    fun toCocktailSearch()

    fun toCocktailDetail(actionBarTitle: String, cocktailId: Long)

    fun exit()
}