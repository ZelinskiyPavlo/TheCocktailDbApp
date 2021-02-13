package com.test.tabhost.navigation.routing

import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.test.cocktail.ui.fragment.CocktailFragment

object Screen {

    enum class Keys {
        COCKTAIL {
            override fun getScreen(): FragmentScreen = cocktail()
        },
        SETTING {
            override fun getScreen(): FragmentScreen = setting()
        };

        abstract fun getScreen(): FragmentScreen
    }

    fun cocktail() = FragmentScreen { CocktailFragment() }

    // TODO: 07.02.2021 Change to Settings fragment
    fun setting() = FragmentScreen { Fragment() }
}