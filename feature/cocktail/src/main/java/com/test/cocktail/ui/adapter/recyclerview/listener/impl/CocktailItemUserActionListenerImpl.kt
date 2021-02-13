package com.test.cocktail.ui.adapter.recyclerview.listener.impl

import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.test.cocktail.R
import com.test.cocktail.ui.CocktailViewModel
import com.test.cocktail_common.adapter.recyclerview.CocktailItemUserActionListener
import com.test.presentation.model.cocktail.CocktailModel

class CocktailItemUserActionListenerImpl (
    private val viewModel: CocktailViewModel
) : CocktailItemUserActionListener {

    override fun onFavoriteIconClicked(cocktail: CocktailModel) {
        viewModel.changeIsFavoriteState(cocktail)
    }

    override fun onItemClicked(cocktail: CocktailModel) {
        viewModel.updateCocktailAndNavigateDetailsFragment(cocktail)
    }

    override fun onItemLongClicked(view: View, cocktail: CocktailModel): Boolean {
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

        return true
    }

}