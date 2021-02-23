package com.test.tabhost.api

import com.test.common.Event

interface TabHostNavigationApi {

    val selectedTabEvent: Event<Int>?

    fun toCocktailSearch()

    fun toCocktailDetail(cocktailId: Long)

    fun toProfile()

    fun toCube()

    fun toSeekBar()

    fun exit()
}