package com.test.thecocktaildb.ui.adapter.recyclerview.cocktail

import android.view.View
import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.ui.adapter.recyclerview.CustomActionListener

interface CocktailItemUserActionListener : CustomActionListener {

    fun onFavoriteIconClicked(cocktail: Cocktail)

    fun onItemClicked(cocktail: Cocktail)

    fun onItemLongClicked(view: View, cocktail: Cocktail): Boolean
}