package com.test.thecocktaildb.ui.cocktailsScreen.callback

import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.DrinkFilter

interface FragmentEventCallback {

    fun navigateToFilterFragmentEvent()

    fun navigateToHostFragmentEvent(filterTypeList: List<DrinkFilter?>)

    fun resetFilterEvent()

    fun addCallback(listener: OnFilterApplied)

    fun removeCallback(listener: OnFilterApplied)
}