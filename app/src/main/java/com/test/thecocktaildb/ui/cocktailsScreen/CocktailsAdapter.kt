package com.test.thecocktaildb.ui.cocktailsScreen

import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.ui.cocktailsScreen.favoriteScreen.FavoriteViewModel
import com.test.thecocktaildb.util.CocktailsItemUserActionListener
import com.test.thecocktaildb.util.recyclerViewAdapters.BaseCocktailsAdapter

class CocktailsAdapter(private val viewModel: AdapterHandler) :
    BaseCocktailsAdapter<Cocktail>() {

    override fun setData(items: List<Cocktail>?) {
        cocktailsList = items ?: emptyList()
        notifyDataSetChanged()
    }

    override fun getItemClickListener(): CocktailsItemUserActionListener {
        return object : CocktailsItemUserActionListener {
            override fun onFavoriteIconClicked(cocktail: Cocktail) {
                if (viewModel is CocktailsViewModel) viewModel.addCocktailToFavorite(cocktail)
                if (viewModel is FavoriteViewModel) viewModel.removeCocktailFromFavorite(cocktail)
            }

            override fun onItemClicked(cocktail: Cocktail) {
                viewModel.updateCocktailAndNavigateDetailsFragment(cocktail)
            }
        }
    }
}