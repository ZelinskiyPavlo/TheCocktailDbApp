package com.test.tabhost.navigation.routing

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.test.cocktail.ui.fragment.CocktailFragment
import com.test.setting.ui.SettingFragment

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

    fun setting() = FragmentScreen { SettingFragment() }
}