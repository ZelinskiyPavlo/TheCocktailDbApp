package com.test.thecocktaildb.ui.cocktailsScreen

import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.DrinkFilter

interface FragmentNavigationListener {

    fun navigateToFilterFragment()

    fun navigateToCocktailFragment(filterTypeList: List<DrinkFilter?>)
}