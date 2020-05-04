package com.test.thecocktaildb.utils.recyclerViewAdapters

import com.test.thecocktaildb.R
import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.utils.recyclerViewAdapters.BaseAdapter

abstract class BaseCocktailsAdapter<T> : BaseAdapter<Cocktail>() {

    abstract override fun setData(items: List<Cocktail>?)

    protected var cocktailsList: List<Cocktail> = emptyList()

    override fun getItemForPosition(position: Int): Cocktail = cocktailsList[position]

    override fun getLayoutIdForPosition(position: Int): Int = R.layout.cocktail_item

    override fun getItemCount(): Int = cocktailsList.size

}
