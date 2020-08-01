package com.test.thecocktaildb.ui.cocktailDetailsScreen

import android.view.View
import com.test.thecocktaildb.R
import com.test.thecocktaildb.presentationNew.model.cocktail.CocktailModel
import com.test.thecocktaildb.util.CocktailsItemUserActionListener
import com.test.thecocktaildb.util.CustomActionListener
import com.test.thecocktaildb.util.recyclerViewAdapter.BaseAdapter

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
            override fun onFavoriteIconClicked(cocktail: CocktailModel) {
            }

            override fun onItemClicked(cocktail: CocktailModel) {

            }

            override fun onItemLongClicked(view: View, cocktail: CocktailModel): Boolean {
                return true
            }
        }
    }


}