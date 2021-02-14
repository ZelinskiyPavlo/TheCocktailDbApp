package com.test.tabhost.navigation.api

interface TabHostNavigationApi {

    fun toCocktailSearch()

    fun toCocktailDetail(actionBarTitle: String, cocktailId: Long)

    fun toProfile()

    fun toCube()

    fun toSeekBar()

    fun exit()
}