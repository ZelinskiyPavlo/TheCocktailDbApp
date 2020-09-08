package com.test.thecocktaildb.presentation.ui.adapter.recyclerview.cocktail

import android.view.View
import com.test.thecocktaildb.presentation.model.cocktail.CocktailModel
import com.test.thecocktaildb.presentation.ui.adapter.recyclerview.CustomActionListener

interface CocktailItemUserActionListener : CustomActionListener {

    fun onFavoriteIconClicked(cocktail: CocktailModel)

    fun onItemClicked(cocktail: CocktailModel)

    fun onItemLongClicked(view: View, cocktail: CocktailModel): Boolean
}