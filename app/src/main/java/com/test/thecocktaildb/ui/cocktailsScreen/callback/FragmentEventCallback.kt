package com.test.thecocktaildb.ui.cocktailsScreen.callback

import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.DrinkFilter
import com.test.thecocktaildb.ui.cocktailsScreen.sortType.CocktailSortType

interface FragmentEventCallback {

    fun navigateToFilterFragmentEvent()

    fun navigateToHostFragmentEvent(filterTypeList: List<DrinkFilter?>)

    fun resetFilterEvent()

    fun applySortingEvent(cocktailSortType: CocktailSortType?)

    fun addCallback(listener: OnFilterApplied)

    fun removeCallback(listener: OnFilterApplied)
}