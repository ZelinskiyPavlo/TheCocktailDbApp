package com.test.thecocktaildb.ui.adapter.recyclerview.cocktail.base

import com.test.thecocktaildb.R
import com.test.thecocktaildb.presentationNew.model.cocktail.CocktailModel
import com.test.thecocktaildb.ui.adapter.recyclerview.base.BaseAdapter

abstract class BaseCocktailAdapter<T> : BaseAdapter<CocktailModel>() {

    abstract override fun setData(items: List<CocktailModel>?)

    protected var cocktailsList: List<CocktailModel> = emptyList()

    override fun getItemForPosition(position: Int): CocktailModel = cocktailsList[position]

    override fun getLayoutIdForPosition(position: Int): Int = R.layout.item_cocktail

    override fun getItemCount(): Int = cocktailsList.size

}
