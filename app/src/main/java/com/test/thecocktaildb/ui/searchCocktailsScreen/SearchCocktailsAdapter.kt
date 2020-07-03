package com.test.thecocktaildb.ui.searchCocktailsScreen

import android.view.View
import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.util.CocktailsItemUserActionListener
import com.test.thecocktaildb.util.recyclerViewAdapters.BaseCocktailsAdapter

class SearchCocktailsAdapter(private val searchCocktailsViewModel: SearchCocktailsViewModel) :
    BaseCocktailsAdapter<Cocktail>() {

    override fun setData(items: List<Cocktail>?) {
        cocktailsList = items ?: emptyList()
        notifyDataSetChanged()
    }

    override fun getItemClickListener(): CocktailsItemUserActionListener {
        return object : CocktailsItemUserActionListener {
            override fun onFavoriteIconClicked(cocktail: Cocktail) {
            }

            override fun onItemClicked(cocktail: Cocktail) {
                searchCocktailsViewModel.saveCocktailAndNavigateDetailsFragment(cocktail)
            }

            override fun onItemLongClicked(view: View, cocktail: Cocktail): Boolean {
                return true
            }
        }
    }
}