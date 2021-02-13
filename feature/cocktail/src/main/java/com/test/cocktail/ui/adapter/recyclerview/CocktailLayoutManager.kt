package com.test.cocktail.ui.adapter.recyclerview

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import com.test.presentation.model.cocktail.CocktailModel

class CocktailLayoutManager(
    context: Context, spanCount: Int, private val adapter: CocktailAdapter
) : GridLayoutManager(context, spanCount) {

    init {
        spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (adapter.newFullCocktailList[position] is String) return 2

                if (adapter.newFullCocktailList[position] is CocktailModel) {
                    val correspondingList = adapter.cocktailWithHeader?.entries?.find { mutableEntry ->
                        mutableEntry.value.contains(adapter.newFullCocktailList[position])
                    }?.value ?: emptyList()

                    if (correspondingList.size.rem(2) != 0) {
                        val relativePosition = correspondingList.indexOf(adapter.newFullCocktailList[position])
                        if (relativePosition == 0) return 2
                    }
                }

                return 1
            }
        }
    }
}