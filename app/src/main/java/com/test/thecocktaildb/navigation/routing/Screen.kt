package com.test.thecocktaildb.navigation.routing

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.test.auth.ui.AuthHostFragment
import com.test.splash.ui.SplashFragment

object Screen {

    fun auth(): FragmentScreen = FragmentScreen { AuthHostFragment() }

    fun cocktail(): FragmentScreen = TODO("Add cocktail to dependency")

    fun splash() = FragmentScreen { SplashFragment() }
}