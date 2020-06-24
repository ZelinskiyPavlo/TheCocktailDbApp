package com.test.thecocktaildb.ui.cocktailsScreen

import com.test.thecocktaildb.data.Cocktail

interface AdapterHandler {

    fun updateCocktailAndNavigateDetailsFragment(cocktail: Cocktail)

}