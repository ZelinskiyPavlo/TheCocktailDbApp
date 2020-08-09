package com.test.thecocktaildb.ui.detail.adapter

import android.view.View
import com.test.thecocktaildb.R
import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.ui.adapter.recyclerview.CustomActionListener
import com.test.thecocktaildb.ui.adapter.recyclerview.base.BaseAdapter
import com.test.thecocktaildb.ui.adapter.recyclerview.cocktail.CocktailsItemUserActionListener
import com.test.thecocktaildb.ui.detail.Ingredient

class IngredientAdapter :
    BaseAdapter<Ingredient>() {

    override fun setData(items: List<Ingredient>?) {
        ingredientsList = items ?: emptyList()
        notifyDataSetChanged()
    }

    private var ingredientsList: List<Ingredient> = emptyList()

    override fun getItemForPosition(position: Int): Ingredient = ingredientsList[position]

    override fun getLayoutIdForPosition(position: Int): Int =
        R.layout.item_cocktail_details_ingredient

    override fun getItemCount(): Int = ingredientsList.size

    override fun getItemClickListener(): CustomActionListener {
        return object : CocktailsItemUserActionListener {
            override fun onFavoriteIconClicked(cocktail: Cocktail) {
            }

            override fun onItemClicked(cocktail: Cocktail) {}

            override fun onItemLongClicked(view: View, cocktail: Cocktail): Boolean {
                return true
            }
        }
    }


}