package com.test.cocktail.ui.adapter.recyclerview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import com.test.presentation.model.cocktail.CocktailModel

class CocktailItemDecoration(
    context: Context,
    @DimenRes private val horizontalDpOffSet: Int,
    @DimenRes private val verticalDpOffSet: Int,
): RecyclerView.ItemDecoration() {

    private var divider: Drawable
    private val bounds = Rect()

    private val horizontalOffSet = context.resources.getDimensionPixelSize(horizontalDpOffSet)
    private val verticalOffSet = context.resources.getDimensionPixelSize(verticalDpOffSet)

    private val attrs = intArrayOf(android.R.attr.listDivider)

    init {
        val a = context.obtainStyledAttributes(attrs)
        divider = a.getDrawable(0)!!
        a.recycle()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        drawVerticalForHeader(c, parent)
    }

    private fun drawVerticalForHeader(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val left: Int
        val right: Int
        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(
                left, parent.paddingTop, right,
                parent.height - parent.paddingBottom
            )
        } else {
            left = 0
            right = parent.width
        }
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)
            if (position == -1) return

            val adapter = parent.adapter as CocktailAdapter
            val item = adapter.processedCocktailsList[position]

            if (item is String) {
                parent.getDecoratedBoundsWithMargins(child, bounds)
                val bottom = bounds.bottom + Math.round(child.translationY)
                val top: Int = bottom - divider.intrinsicHeight
                divider.setBounds(left, top, right, bottom)
                divider.draw(canvas)
            }
        }
        canvas.restore()
    }

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == -1) return

        val adapter = parent.adapter as CocktailAdapter
        val item = adapter.processedCocktailsList[position]
        val spanSizeLookup = (parent.layoutManager as CocktailLayoutManager).spanSizeLookup

        if (item is CocktailModel) {
            val spanCount = spanSizeLookup.getSpanSize(position)

            val correspondingList = adapter.headerWithCocktailsMap?.entries?.find { mutableEntry ->
                mutableEntry.value.contains(adapter.processedCocktailsList[position])
            }?.value ?: emptyList()

            if (spanCount == 2) {
                outRect.top = verticalOffSet
                outRect.bottom = verticalOffSet
                outRect.left = horizontalOffSet
                outRect.right = horizontalOffSet
                return
            }

            if (spanCount == 1) {
                if (correspondingList.size.rem(2) == 0) {
                    outRect.top = verticalOffSet
                    outRect.bottom = verticalOffSet
                    outRect.left = horizontalOffSet
                    outRect.right = horizontalOffSet / 2
                } else {
                    outRect.top = verticalOffSet
                    outRect.bottom = verticalOffSet
                    outRect.left = horizontalOffSet / 2
                    outRect.right = horizontalOffSet
                }
            }
        }
    }
}