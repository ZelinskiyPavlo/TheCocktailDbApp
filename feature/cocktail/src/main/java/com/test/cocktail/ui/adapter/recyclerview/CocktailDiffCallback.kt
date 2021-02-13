package com.test.cocktail.ui.adapter.recyclerview

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.test.presentation.model.cocktail.CocktailModel

class CocktailDiffCallback(
    private val oldCocktails: List<Any>,
    private val newCocktails: List<Any>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldCocktails.size
    }

    override fun getNewListSize(): Int {
        return newCocktails.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCocktail = oldCocktails[oldItemPosition]
        val newCocktail = newCocktails[newItemPosition]
        if (oldCocktail is String && newCocktail is String) {
            return oldCocktail == newCocktail
        }
        if (oldCocktail is CocktailModel && newCocktail is CocktailModel) {
            return oldCocktail.id == newCocktail.id
        }
        return false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldCocktails[oldItemPosition]
        val new = newCocktails[newItemPosition]

        if (old is String && new is String) {
            return old == new
        }
        if (old is CocktailModel && new is CocktailModel) {
            return old.image == new.image
                    && old.names.defaults == new.names.defaults
                    && old.isFavorite == new.isFavorite
        }
        return false
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldCocktails[oldItemPosition]
        val newItem = newCocktails[newItemPosition]

        val diff = Bundle()
        if (oldItem is CocktailModel && newItem is CocktailModel) {
            if (newItem.isFavorite != oldItem.isFavorite) {
                diff.putBoolean("isFavorite", newItem.isFavorite)
            }

            return if (diff.size() == 0) null
            else diff
        }
        return null
    }
}