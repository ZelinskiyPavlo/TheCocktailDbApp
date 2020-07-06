package com.test.thecocktaildb.dataBinding.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.ui.cocktailDetailsScreen.Ingredient
import com.test.thecocktaildb.ui.cocktailDetailsScreen.IngredientsAdapter
import com.test.thecocktaildb.ui.cocktailsScreen.CocktailsAdapter
import com.test.thecocktaildb.ui.searchCocktailsScreen.SearchCocktailsAdapter

@BindingAdapter("bind:rv_cocktails", "bind:rv_adapterTag")
fun RecyclerView.setItems(items: List<Cocktail>?, tag: AdapterType) {
    val recyclerViewAdapter = when (tag) {
        AdapterType.COCKTAIL_ADAPTER -> adapter as CocktailsAdapter
        AdapterType.SEARCH_ADAPTER -> adapter as SearchCocktailsAdapter
    }

    recyclerViewAdapter.setData(items ?: emptyList())
}

enum class AdapterType {
    COCKTAIL_ADAPTER,
    SEARCH_ADAPTER,
}

@BindingAdapter("bind:rv_cocktailDetailFragment_ingredients")
fun RecyclerView.setIngredients(items: List<Ingredient>?) {
    with(adapter as IngredientsAdapter) {
        setData(items ?: emptyList())
    }
}