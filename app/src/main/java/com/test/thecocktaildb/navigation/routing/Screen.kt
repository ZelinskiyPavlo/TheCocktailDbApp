package com.test.thecocktaildb.navigation.routing

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.test.auth.ui.AuthHostFragment
import com.test.detail.ui.CocktailDetailsFragment
import com.test.search.ui.SearchCocktailFragment
import com.test.splash.ui.SplashFragment
import com.test.tabhost.ui.TabHostFragment

object Screen {

    fun auth() = FragmentScreen { AuthHostFragment() }

    fun cocktail(): FragmentScreen = TODO("Add cocktail to dependency")

    fun splash() = FragmentScreen { SplashFragment() }
}