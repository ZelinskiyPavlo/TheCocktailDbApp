package com.test.search.adapter.recyclerview

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class SearchCocktailItemDecoration (
    context: Context,
    @DimenRes horizontalDpOffSet: Int,
    @DimenRes verticalDpOffSet: Int,
): RecyclerView.ItemDecoration() {

    private val horizontalPixelOffset = context.resources.getDimensionPixelOffset(horizontalDpOffSet)
    private val verticalPixedOffSet = context.resources.getDimensionPixelOffset(verticalDpOffSet)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)

        when  {
            position == -1 -> return
            position.rem(2) == 0 -> {
                outRect.left = horizontalPixelOffset
                outRect.right = horizontalPixelOffset / 2
            }
            else -> {
                outRect.left = horizontalPixelOffset / 2
                outRect.right = horizontalPixelOffset
            }
        }

        outRect.top = verticalPixedOffSet / 2
        outRect.bottom = verticalPixedOffSet / 2
    }
}