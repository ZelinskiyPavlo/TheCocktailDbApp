package com.test.cocktail_common.adapter.recyclerview.base

import com.test.presentation.adapter.recyclerview.base.BaseAdapter
import com.test.presentation.model.cocktail.CocktailModel


abstract class BaseCocktailAdapter<T> : BaseAdapter<CocktailModel>() {

    abstract override fun setData(items: List<CocktailModel>?)

    protected var cocktailsList: List<CocktailModel> = emptyList()

    override fun getItemForPosition(position: Int): CocktailModel = cocktailsList[position]

    override fun getItemCount(): Int = cocktailsList.size

}
