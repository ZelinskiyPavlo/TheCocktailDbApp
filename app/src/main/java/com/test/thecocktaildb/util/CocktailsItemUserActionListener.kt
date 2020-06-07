package com.test.thecocktaildb.util

import com.test.thecocktaildb.data.Cocktail

interface CocktailsItemUserActionListener : CustomActionListener {

    fun onItemClicked(cocktail: Cocktail)
}