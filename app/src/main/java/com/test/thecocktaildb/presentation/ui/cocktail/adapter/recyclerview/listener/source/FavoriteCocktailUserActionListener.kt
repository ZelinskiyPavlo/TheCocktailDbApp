package com.test.thecocktaildb.presentation.ui.cocktail.adapter.recyclerview.listener.source

import android.view.View
import com.test.thecocktaildb.presentation.model.cocktail.CocktailModel
import com.test.thecocktaildb.presentation.ui.adapter.recyclerview.CustomActionListener

interface FavoriteCocktailUserActionListener: CustomActionListener {

    fun onFavoriteIconClicked(cocktail: CocktailModel)

    fun onItemClicked(cocktail: CocktailModel)

    fun onOptionIconClicked(view: View, cocktail: CocktailModel)
}