package com.test.presentation.adapter.recyclerview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class FirstItemDividerItemDecoration(context: Context) : ItemDecoration() {

    private val attrs = intArrayOf(android.R.attr.listDivider)

    private var divider: Drawable

    private var decoratedBounds = Rect()

    init {
        val a = context.obtainStyledAttributes(attrs)
        divider = a.getDrawable(0)!!
        a.recycle()
    }

    override fun onDraw(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            if (parent.getChildAdapterPosition(child) == 0) {
                val params = child.layoutParams as RecyclerView.LayoutParams
                parent.getDecoratedBoundsWithMargins(child, decoratedBounds)
                val top: Int = decoratedBounds.top - params.topMargin
                val bottom = top + divider.intrinsicHeight
                val left = parent.paddingLeft
                val right = parent.width - parent.paddingRight
                divider.setBounds(left, top, right, bottom)
                divider.draw(c)
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(0, divider.intrinsicHeight, 0, 0)
    }
}