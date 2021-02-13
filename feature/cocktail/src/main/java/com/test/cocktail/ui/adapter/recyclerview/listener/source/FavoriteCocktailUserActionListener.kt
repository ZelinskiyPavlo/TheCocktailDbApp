package com.test.cocktail.ui.adapter.recyclerview.listener.source

import android.view.View
import com.test.presentation.adapter.recyclerview.CustomActionListener
import com.test.presentation.model.cocktail.CocktailModel

interface FavoriteCocktailUserActionListener: CustomActionListener {

    fun onFavoriteIconClicked(cocktail: CocktailModel)

    fun onItemClicked(cocktail: CocktailModel)

    fun onOptionIconClicked(view: View, cocktail: CocktailModel)
}