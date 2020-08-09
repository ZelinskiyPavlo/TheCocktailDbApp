package com.test.thecocktaildb.ui.cocktail.adapter.recyclerview

import android.view.View
import android.widget.PopupMenu
import com.test.thecocktaildb.R
import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.ui.adapter.recyclerview.cocktail.CocktailsItemUserActionListener
import com.test.thecocktaildb.ui.adapter.recyclerview.cocktail.base.BaseCocktailsAdapter
import com.test.thecocktaildb.ui.cocktail.host.SharedHostViewModel

class CocktailAdapter(private val viewModel: SharedHostViewModel) :
    BaseCocktailsAdapter<Cocktail>() {

    override fun setData(items: List<Cocktail>?) {
        cocktailsList = items ?: emptyList()
        notifyDataSetChanged()
    }

    override fun getItemClickListener(): CocktailsItemUserActionListener {
        return object : CocktailsItemUserActionListener {
            override fun onFavoriteIconClicked(cocktail: Cocktail) {
                viewModel.changeIsFavoriteState(cocktail)
            }

            override fun onItemClicked(cocktail: Cocktail) {
                viewModel.updateCocktailAndNavigateDetailsFragment(cocktail)
            }

            override fun onItemLongClicked(view: View, cocktail: Cocktail): Boolean {
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