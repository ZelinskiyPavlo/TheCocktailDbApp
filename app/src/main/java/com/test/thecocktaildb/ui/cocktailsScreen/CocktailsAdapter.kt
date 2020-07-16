package com.test.thecocktaildb.ui.cocktailsScreen

import android.view.View
import android.widget.PopupMenu
import com.test.thecocktaildb.R
import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.ui.cocktailsScreen.fragmentHostScreen.SharedHostViewModel
import com.test.thecocktaildb.util.CocktailsItemUserActionListener
import com.test.thecocktaildb.util.recyclerViewAdapters.BaseCocktailsAdapter

class CocktailsAdapter(private val viewModel: SharedHostViewModel) :
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
                val shortcutMenu = PopupMenu(view.context, view)
                shortcutMenu.inflate(R.menu.menu_drink_item_shortcut)
                if (cocktail.isFavorite)
                    shortcutMenu.menu.findItem(R.id.menu_cocktail_history_add_favorite)
                        .isVisible = false
                else
                    shortcutMenu.menu.findItem(R.id.menu_cocktail_history_remove_favorite)
                        .isVisible = false

                shortcutMenu.setOnMenuItemClickListener { menuItem ->
                    when(menuItem.itemId) {
                        R.id.menu_cocktail_history_open -> {
                            viewModel.updateCocktailAndNavigateDetailsFragment(cocktail)
                            true
                        }
                        R.id.menu_cocktail_history_add_favorite -> {
                            viewModel.changeIsFavoriteState(cocktail)
                            true
                        }
                        R.id.menu_cocktail_history_remove_favorite -> {
                            viewModel.changeIsFavoriteState(cocktail)
                            true
                        }
                        else -> false
                    }
                }
                shortcutMenu.show()

                return true
            }
        }
    }
}