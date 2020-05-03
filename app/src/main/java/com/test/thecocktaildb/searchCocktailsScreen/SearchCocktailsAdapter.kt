package com.test.thecocktaildb.searchCocktailsScreen

import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.utils.recyclerViewAdapters.BaseCocktailsAdapter
import com.test.thecocktaildb.utils.CocktailsItemUserActionListener
import timber.log.Timber

class SearchCocktailsAdapter(private val searchCocktailsViewModel: SearchCocktailsViewModel) :
    BaseCocktailsAdapter<Cocktail>() {

    override fun setData(items: List<Cocktail>) {
        cocktailsList = items
        notifyDataSetChanged()
    }

    override fun getItemClickListener(): CocktailsItemUserActionListener {
        return object : CocktailsItemUserActionListener {

            override fun onItemClicked(cocktail: Cocktail) {
                searchCocktailsViewModel.saveClickedCocktail(cocktail)
                searchCocktailsViewModel.navigateToCocktailDetailsFragment(cocktail)
            }
        }
    }
}