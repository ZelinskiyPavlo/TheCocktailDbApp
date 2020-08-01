package com.test.thecocktaildb.ui.cocktailScreen.adapter

import android.view.View
import android.widget.PopupMenu
import com.test.thecocktaildb.R
import com.test.thecocktaildb.presentationNew.model.cocktail.CocktailModel
import com.test.thecocktaildb.ui.cocktailScreen.fragmentHostScreen.SharedHostViewModel
import com.test.thecocktaildb.util.CocktailsItemUserActionListener
import com.test.thecocktaildb.util.recyclerViewAdapter.BaseCocktailsAdapter

class CocktailAdapter(private val viewModel: SharedHostViewModel) :
    BaseCocktailsAdapter<CocktailModel>() {

    override fun setData(items: List<CocktailModel>?) {
        cocktailsList = items ?: emptyList()
        notifyDataSetChanged()
    }

    override fun getItemClickListener(): CocktailsItemUserActionListener {
        return object : CocktailsItemUserActionListener {
            override fun onFavoriteIconClicked(cocktail: CocktailModel) {
                viewModel.changeIsFavoriteState(cocktail)
            }

            override fun onItemClicked(cocktail: CocktailModel) {
                viewModel.updateCocktailAndNavigateDetailsFragment(cocktail)
            }

            override fun onItemLongClicked(view: View, cocktail: CocktailModel): Boolean {
                PopupMenu(view.context, view).apply {
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.menu_cocktail_history_open -> {
                                viewModel.updateCocktailAndNavigateDetailsFragment(cocktail)
                                true
                            }
                            else -> false
                        }
                    }
                    inflate(R.menu.menu_drink_item_shortcut)
                    show()
                }
                return true
            }
        }
    }
}