package com.test.thecocktaildb.ui.cocktailsScreen.callback

import com.test.thecocktaildb.data.Cocktail

interface OnFavoriteClicked {

    fun onFavoriteAdded(cocktail: Cocktail)

}