package com.test.thecocktaildb.navigation.routing

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.test.auth.ui.AuthHostFragment
import com.test.cube.ui.CubeFragment
import com.test.detail.ui.CocktailDetailsFragment
import com.test.profile.ui.ProfileFragment
import com.test.search.ui.SearchCocktailFragment
import com.test.seekbar.ui.RangeSeekBarFragment
import com.test.tabhost.ui.TabHostFragment

object Screen {

    fun auth() = FragmentScreen { AuthHostFragment() }

    fun tabHost() = FragmentScreen { TabHostFragment() }

    fun search() = FragmentScreen { SearchCocktailFragment() }

    fun detail(cocktailId: Long) =
        FragmentScreen { CocktailDetailsFragment.newInstance(cocktailId) }

    fun profile() = FragmentScreen { ProfileFragment() }

    fun cube() = FragmentScreen { CubeFragment() }

    fun seekBar() = FragmentScreen { RangeSeekBarFragment() }
}