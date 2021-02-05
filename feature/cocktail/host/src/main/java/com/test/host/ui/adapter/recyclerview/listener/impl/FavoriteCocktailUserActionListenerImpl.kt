package com.test.thecocktaildb.presentation.ui.cocktail.adapter.recyclerview.listener.impl

import android.view.View
import android.widget.PopupMenu
import com.test.thecocktaildb.R
import com.test.thecocktaildb.presentation.model.cocktail.CocktailModel
import com.test.thecocktaildb.presentation.ui.cocktail.adapter.recyclerview.listener.source.FavoriteCocktailUserActionListener
import com.test.thecocktaildb.presentation.ui.cocktail.host.SharedHostViewModel

class FavoriteCocktailUserActionListenerImpl (
    private val viewModel: SharedHostViewModel
): FavoriteCocktailUserActionListener {

    override fun onFavoriteIconClicked(cocktail: CocktailModel) {
        viewModel.changeIsFavoriteState(cocktail)
    }

    override fun onItemClicked(cocktail: CocktailModel) {
        viewModel.updateCocktailAndNavigateDetailsFragment(cocktail)
    }

    override fun onOptionIconClicked(view: View, cocktail: CocktailModel) {
        val shortcutMenu = PopupMenu(view.context, view)
        shortcutMenu.inflate(R.menu.menu_drink_item_shortcut)
        if (cocktail.isFavorite)
            shortcutMenu.menu.findItem(R.id.menu_cocktail_history_add_favorite)
                .isVisible = false
        else
            shortcutMenu.menu.findItem(R.id.menu_cocktail_history_remove_favorite)
                .isVisible = false

        shortcutMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
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
    }
}