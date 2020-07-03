package com.test.thecocktaildb.util

import android.view.View
import com.test.thecocktaildb.data.Cocktail

interface CocktailsItemUserActionListener : CustomActionListener {

    fun onFavoriteIconClicked(cocktail: Cocktail)

    fun onItemClicked(cocktail: Cocktail)

    fun onItemLongClicked(view: View, cocktail: Cocktail): Boolean
}