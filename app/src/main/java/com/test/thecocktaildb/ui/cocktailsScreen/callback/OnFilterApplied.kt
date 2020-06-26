package com.test.thecocktaildb.ui.cocktailsScreen.callback

import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.DrinkFilter

interface OnFilterApplied {

    fun applyFilter(filterTypeList: List<DrinkFilter?>)

    fun resetFilter()

}