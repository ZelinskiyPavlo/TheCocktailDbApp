package com.test.search.adapter.recyclerview

import android.view.View
import android.widget.ImageButton
import com.test.cocktail_common.adapter.recyclerview.CocktailItemUserActionListener
import com.test.cocktail_common.adapter.recyclerview.base.BaseCocktailAdapter
import com.test.presentation.model.cocktail.CocktailModel
import com.test.search.R
import com.test.search.ui.SearchCocktailViewModel

class SearchCocktailAdapter(private val searchCocktailViewModel: SearchCocktailViewModel) :
    BaseCocktailAdapter<CocktailModel>() {

    override fun setData(items: List<CocktailModel>?) {
        cocktailsList = items ?: emptyList()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.findViewById<ImageButton>(R.id.item_cocktail_favorite_btn).visibility = View.GONE
    }

    override fun getLayoutIdForPosition(position: Int): Int = R.layout.item_cocktail

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