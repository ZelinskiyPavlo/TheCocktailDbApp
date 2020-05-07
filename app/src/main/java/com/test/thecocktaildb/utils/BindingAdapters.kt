package com.test.thecocktaildb.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.thecocktaildb.cocktailDetailsScreen.CocktailDetailsViewModel
import com.test.thecocktaildb.cocktailDetailsScreen.Ingredient
import com.test.thecocktaildb.cocktailDetailsScreen.IngredientsAdapter
import com.test.thecocktaildb.cocktailsScreen.CocktailsAdapter
import com.test.thecocktaildb.cocktailsScreen.CocktailsViewModel
import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.searchCocktailsScreen.SearchCocktailsAdapter
import com.test.thecocktaildb.searchCocktailsScreen.SearchCocktailsViewModel
import timber.log.Timber

@BindingAdapter("app:image_url")
fun /*ImageView.*/loadImage(imageView: ImageView, url: String?) =
    url?.let { Glide.with(imageView.context).load(url).into(imageView) }

@BindingAdapter("app:items", "app:adapter_tag")
fun /*RecyclerView.*/setItems(recyclerView: RecyclerView, items: List<Cocktail>?, tag: ViewModel) {

    val recyclerViewAdapter = when (tag) {
        is CocktailsViewModel -> recyclerView.adapter as CocktailsAdapter
//        is CocktailsViewModel -> adapter as CocktailsAdapter
        is SearchCocktailsViewModel -> recyclerView.adapter as SearchCocktailsAdapter
//        is SearchCocktailsViewModel -> adapter as SearchCocktailsAdapter
        else -> throw ClassCastException()
    }

    Timber.d("SetData Binding adapter setItems called")
    recyclerViewAdapter.setData(items ?: emptyList())
}

// Temporary solution (I guess)
@BindingAdapter("app:ingredients")
fun setIngredients(recyclerView: RecyclerView, items: List<Ingredient>?) {
    with(recyclerView.adapter as IngredientsAdapter) {
        Timber.d("SetIngredients called")
        setData(items ?: emptyList())
    }
}