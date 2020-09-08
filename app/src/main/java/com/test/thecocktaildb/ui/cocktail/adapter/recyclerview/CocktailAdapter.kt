package com.test.thecocktaildb.ui.cocktail.adapter.recyclerview

import android.view.View
import android.widget.PopupMenu
import com.test.thecocktaildb.R
import com.test.thecocktaildb.presentationNew.model.CocktailModel
import com.test.thecocktaildb.ui.adapter.recyclerview.cocktail.CocktailItemUserActionListener
import com.test.thecocktaildb.ui.adapter.recyclerview.cocktail.base.BaseCocktailAdapter
import com.test.thecocktaildb.ui.cocktail.host.SharedHostViewModel

class CocktailAdapter(private val viewModel: SharedHostViewModel) :
    BaseCocktailAdapter<CocktailModel>() {

    override fun setData(items: List<CocktailModel>?) {
        cocktailsList = items ?: emptyList()
        notifyDataSetChanged()
    }

    override fun getItemClickListener(): CocktailItemUserActionListener {
        return object : CocktailItemUserActionListener {
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