package com.test.thecocktaildb.util

import com.test.thecocktaildb.data.Cocktail

interface CocktailsItemUserActionListener : CustomActionListener {

    fun onFavoriteIconClicked(cocktail: Cocktail)

    fun onItemClicked(cocktail: Cocktail)
}