package com.test.detail.adapter

import com.test.detail.R
import com.test.detail.model.Ingredient
import com.test.presentation.adapter.recyclerview.CustomActionListener
import com.test.presentation.adapter.recyclerview.base.BaseAdapter

internal class IngredientAdapter : BaseAdapter<Ingredient>() {

    override fun setData(items: List<Ingredient>?) {
        ingredientsList = items ?: emptyList()
        notifyDataSetChanged()
    }

    private var ingredientsList: List<Ingredient> = emptyList()

    override fun getItemForPosition(position: Int): Ingredient = ingredientsList[position]

    override fun getLayoutIdForPosition(position: Int): Int =
        R.layout.item_cocktail_details_ingredient

    override fun getItemCount(): Int = ingredientsList.size

    override fun getItemClickListener(): CustomActionListener = object : CustomActionListener {}
}