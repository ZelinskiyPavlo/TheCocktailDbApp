package com.test.cocktail_common.adapter.recyclerview

import android.view.View
import com.test.presentation.adapter.recyclerview.CustomActionListener
import com.test.presentation.model.cocktail.CocktailModel

interface CocktailItemUserActionListener : CustomActionListener {

    fun onFavoriteIconClicked(cocktail: CocktailModel)

    fun onItemClicked(cocktail: CocktailModel)

    fun onItemLongClicked(view: View, cocktail: CocktailModel): Boolean
}