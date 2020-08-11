package com.test.thecocktaildb.ui.adapter.recyclerview.cocktail

import android.view.View
import com.test.thecocktaildb.presentationNew.model.cocktail.CocktailModel
import com.test.thecocktaildb.ui.adapter.recyclerview.CustomActionListener

interface CocktailItemUserActionListener : CustomActionListener {

    fun onFavoriteIconClicked(cocktail: CocktailModel)

    fun onItemClicked(cocktail: CocktailModel)

    fun onItemLongClicked(view: View, cocktail: CocktailModel): Boolean
}