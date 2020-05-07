package com.test.thecocktaildb.cocktailDetailsScreen

import com.test.thecocktaildb.R
import com.test.thecocktaildb.utils.CustomActionListener
import com.test.thecocktaildb.utils.recyclerViewAdapters.BaseAdapter

class IngredientsAdapter :
    BaseAdapter<Ingredient>() {

    override fun setData(items: List<Ingredient>?) {
        ingredientsList = items ?: emptyList()
        notifyDataSetChanged()
    }

    private var ingredientsList: List<Ingredient> = emptyList()

    override fun getItemForPosition(position: Int): Ingredient = ingredientsList[position]

    override fun getLayoutIdForPosition(position: Int): Int =
        R.layout.cocktail_details_ingredient_item

    override fun getItemCount(): Int = ingredientsList.size

    override fun getItemClickListener(): CustomActionListener {
        return object : CustomActionListener {}
    }


}