package com.test.thecocktaildb.ui.adapter.recyclerview.cocktail.base

import com.test.thecocktaildb.R
import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.ui.adapter.recyclerview.base.BaseAdapter

// TODO: видалена в 9 гілці (версія з 8 гілки)
abstract class BaseCocktailAdapter<T> : BaseAdapter<Cocktail>() {

    abstract override fun setData(items: List<Cocktail>?)

    protected var cocktailsList: List<Cocktail> = emptyList()

    override fun getItemForPosition(position: Int): Cocktail = cocktailsList[position]

    override fun getLayoutIdForPosition(position: Int): Int = R.layout.item_cocktail

    override fun getItemCount(): Int = cocktailsList.size

}
