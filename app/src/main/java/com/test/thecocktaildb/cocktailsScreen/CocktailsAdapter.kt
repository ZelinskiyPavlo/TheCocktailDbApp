package com.test.thecocktaildb.cocktailsScreen

import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.utils.recyclerViewAdapters.BaseCocktailsAdapter
import com.test.thecocktaildb.utils.CocktailsItemUserActionListener

class CocktailsAdapter(private val cocktailsViewModel: CocktailsViewModel) :
    BaseCocktailsAdapter<Cocktail>() {

    override fun setData(items: List<Cocktail>?) {
        cocktailsList = items ?: emptyList()
        notifyDataSetChanged()
    }

    override fun getItemClickListener(): CocktailsItemUserActionListener {
        return object : CocktailsItemUserActionListener {

            override fun onItemClicked(cocktail: Cocktail) {
                cocktailsViewModel.navigateToCocktailDetailsFragment(cocktail)
            }
        }
    }
}