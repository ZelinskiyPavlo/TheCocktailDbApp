package com.test.search.adapter.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.test.presentation.model.cocktail.CocktailModel
import com.test.search.adapter.recyclerview.SearchCocktailAdapter

@BindingAdapter("bind:rv_cocktails")
internal fun RecyclerView.setItems(items: List<CocktailModel>?) {
    (adapter as SearchCocktailAdapter).setData(items)
}