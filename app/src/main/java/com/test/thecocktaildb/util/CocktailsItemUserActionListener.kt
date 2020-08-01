package com.test.thecocktaildb.util

import android.view.View
import com.test.thecocktaildb.presentationNew.model.cocktail.CocktailModel

interface CocktailsItemUserActionListener : CustomActionListener {

    fun onFavoriteIconClicked(cocktail: CocktailModel)

    fun onItemClicked(cocktail: CocktailModel)

    fun onItemLongClicked(view: View, cocktail: CocktailModel): Boolean
}