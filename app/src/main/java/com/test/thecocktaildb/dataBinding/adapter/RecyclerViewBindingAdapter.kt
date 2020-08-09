package com.test.thecocktaildb.dataBinding.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.ui.cocktail.adapter.recyclerview.CocktailAdapter
import com.test.thecocktaildb.ui.detail.Ingredient
import com.test.thecocktaildb.ui.detail.adapter.IngredientAdapter
import com.test.thecocktaildb.ui.search.adapter.SearchCocktailAdapter
import timber.log.Timber

@BindingAdapter("bind:rv_cocktails", "bind:rv_adapterTag")
fun RecyclerView.setItems(items: List<Cocktail>?, tag: AdapterType) {
    val recyclerViewAdapter = when (tag) {
        AdapterType.COCKTAIL_ADAPTER -> adapter as CocktailAdapter
        AdapterType.SEARCH_ADAPTER -> adapter as SearchCocktailAdapter
    }
    Timber.i("Binding adapter with items")
    items?.forEach { Timber.i("$items \n ") }
    recyclerViewAdapter.setData(items ?: emptyList())
}

enum class AdapterType {
    COCKTAIL_ADAPTER,
    SEARCH_ADAPTER,
}

@BindingAdapter("bind:rv_cocktailDetailFragment_ingredients")
fun RecyclerView.setIngredients(items: List<Ingredient>?) {
    with(adapter as IngredientAdapter) {
        setData(items ?: emptyList())
    }
}