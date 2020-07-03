package com.test.thecocktaildb.ui.cocktailsScreen.callback

import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.DrinkFilter
import com.test.thecocktaildb.ui.cocktailsScreen.sortType.CocktailSortType

interface OnFilterApplied {

    fun applyFilter(filterTypeList: List<DrinkFilter?>)

    fun resetFilter()

    fun applySorting(cocktailSortType: CocktailSortType?)

}