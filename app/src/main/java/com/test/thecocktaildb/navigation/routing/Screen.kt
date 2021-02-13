package com.test.thecocktaildb.navigation.routing

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.test.auth.ui.AuthHostFragment
import com.test.detail.ui.CocktailDetailsFragment
import com.test.search.ui.SearchCocktailFragment
import com.test.splash.ui.SplashFragment
import com.test.tabhost.ui.TabHostFragment

object Screen {

    fun auth() = FragmentScreen { AuthHostFragment() }

    fun tabHost() = FragmentScreen { TabHostFragment() }

    fun splash() = FragmentScreen { SplashFragment() }

    fun search() = FragmentScreen { SearchCocktailFragment() }

    fun detail(actionBarTitle: String, cocktailId: Long) =
        FragmentScreen { CocktailDetailsFragment.newInstance(actionBarTitle, cocktailId) }
}