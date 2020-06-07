package com.test.thecocktaildb.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.ui.cocktailDetailsScreen.Ingredient
import com.test.thecocktaildb.ui.cocktailDetailsScreen.IngredientsAdapter
import com.test.thecocktaildb.ui.cocktailsScreen.CocktailsAdapter
import com.test.thecocktaildb.ui.cocktailsScreen.CocktailsViewModel
import com.test.thecocktaildb.ui.searchCocktailsScreen.SearchCocktailsAdapter
import com.test.thecocktaildb.ui.searchCocktailsScreen.SearchCocktailsViewModel

@BindingAdapter("app:image_url")
fun ImageView.loadImage(url: String?) =
    url?.let { Glide.with(this.context).load(url).into(this) }

@BindingAdapter("app:items", "app:adapter_tag")
fun RecyclerView.setItems(items: List<Cocktail>?, tag: ViewModel) {
    val recyclerViewAdapter = when (tag) {
        is CocktailsViewModel -> adapter as CocktailsAdapter
        is SearchCocktailsViewModel -> adapter as SearchCocktailsAdapter
        else -> throw ClassCastException()
    }

    recyclerViewAdapter.setData(items ?: emptyList())
}

@BindingAdapter("app:ingredients")
fun RecyclerView.setIngredients(items: List<Ingredient>?) {
    with(adapter as IngredientsAdapter) {
        setData(items ?: emptyList())
    }
}