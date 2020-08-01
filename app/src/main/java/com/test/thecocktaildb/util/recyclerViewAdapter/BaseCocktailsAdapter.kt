package com.test.thecocktaildb.util.recyclerViewAdapter

import com.test.thecocktaildb.R
import com.test.thecocktaildb.presentationNew.model.cocktail.CocktailModel

abstract class BaseCocktailsAdapter<T> : BaseAdapter<CocktailModel>() {

    abstract override fun setData(items: List<CocktailModel>?)

    protected var cocktailsList: List<CocktailModel> = emptyList()

    override fun getItemForPosition(position: Int): CocktailModel = cocktailsList[position]

    override fun getLayoutIdForPosition(position: Int): Int = R.layout.item_cocktail

    override fun getItemCount(): Int = cocktailsList.size

}
