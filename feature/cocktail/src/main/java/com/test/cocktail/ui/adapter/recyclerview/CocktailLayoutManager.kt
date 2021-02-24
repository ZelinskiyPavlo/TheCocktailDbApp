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
                if (adapter.processedCocktailsList[position] is String) return 2

                if (adapter.processedCocktailsList[position] is CocktailModel) {
                    val correspondingList = adapter.headerWithCocktailsMap?.entries?.find { mutableEntry ->
                        mutableEntry.value.contains(adapter.processedCocktailsList[position])
                    }?.value ?: emptyList()

                    if (correspondingList.size.rem(2) != 0) {
                        val relativePosition = correspondingList.indexOf(adapter.processedCocktailsList[position])
                        if (relativePosition == 0) return 2
                    }
                }

                return 1
            }
        }
    }
}