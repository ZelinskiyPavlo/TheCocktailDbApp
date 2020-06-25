package com.test.thecocktaildb.ui.cocktailsScreen

import com.test.thecocktaildb.data.Cocktail

interface AdapterHandler {

    fun updateCocktailAndNavigateDetailsFragment(cocktail: Cocktail)

    fun addCocktailToFavorite(cocktail: Cocktail)

    fun removeCocktailFromFavorite(cocktail: Cocktail)

}