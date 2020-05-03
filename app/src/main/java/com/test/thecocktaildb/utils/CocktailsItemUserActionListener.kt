package com.test.thecocktaildb.utils

import com.test.thecocktaildb.data.Cocktail

interface CocktailsItemUserActionListener: CustomActionListener {

    fun onItemClicked(cocktail: Cocktail)
}