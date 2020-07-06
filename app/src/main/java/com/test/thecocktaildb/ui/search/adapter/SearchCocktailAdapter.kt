package com.test.thecocktaildb.ui.search.adapter

import android.view.View
import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.ui.adapter.recyclerview.cocktail.CocktailItemUserActionListener
import com.test.thecocktaildb.ui.adapter.recyclerview.cocktail.base.BaseCocktailAdapter
import com.test.thecocktaildb.ui.search.SearchCocktailViewModel

class SearchCocktailAdapter(private val searchCocktailViewModel: SearchCocktailViewModel) :
    BaseCocktailAdapter<Cocktail>() {

    override fun setData(items: List<Cocktail>?) {
        cocktailsList = items ?: emptyList()
        notifyDataSetChanged()
    }

    override fun getItemClickListener(): CocktailItemUserActionListener {
        return object : CocktailItemUserActionListener {
            override fun onFavoriteIconClicked(cocktail: Cocktail) {
            }

            override fun onItemClicked(cocktail: Cocktail) {
                searchCocktailViewModel.saveCocktailAndNavigateDetailsFragment(cocktail)
            }

            override fun onItemLongClicked(view: View, cocktail: Cocktail): Boolean {
                return true
            }
        }
    }
}