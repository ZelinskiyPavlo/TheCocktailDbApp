package com.test.thecocktaildb.ui.search.adapter

import android.view.View
import com.test.thecocktaildb.presentationNew.model.cocktail.CocktailModel
import com.test.thecocktaildb.ui.adapter.recyclerview.cocktail.CocktailItemUserActionListener
import com.test.thecocktaildb.ui.adapter.recyclerview.cocktail.base.BaseCocktailAdapter
import com.test.thecocktaildb.ui.search.SearchCocktailViewModel

class SearchCocktailAdapter(private val searchCocktailViewModel: SearchCocktailViewModel) :
    BaseCocktailAdapter<CocktailModel>() {

    override fun setData(items: List<CocktailModel>?) {
        cocktailsList = items ?: emptyList()
        notifyDataSetChanged()
    }

    override fun getItemClickListener(): CocktailItemUserActionListener {
        return object : CocktailItemUserActionListener {
            override fun onFavoriteIconClicked(cocktail: CocktailModel) {
            }

            override fun onItemClicked(cocktail: CocktailModel) {
                searchCocktailViewModel.saveCocktailAndNavigateDetailsFragment(cocktail)
            }

            override fun onItemLongClicked(view: View, cocktail: CocktailModel): Boolean {
                return true
            }
        }
    }
}