package com.test.presentation.ui.custom

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.use
import com.google.android.material.appbar.AppBarLayout
import com.test.presentation.R

class HeightProportionAppBarLayout(context: Context, attributeSet: AttributeSet) :
    AppBarLayout(context, attributeSet) {

    init {
        context.obtainStyledAttributes(attributeSet, R.styleable.HeightProportionAppBarLayout)
            .use { typedArray ->
                maxHeightProportion =
                    typedArray.getInt(
                        R.styleable.HeightProportionAppBarLayout_hpab_max_height_proportion,
                        -1
                    )
            }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    var maxHeightProportion: Int? = null
        set(value) {
            if (field == value) return
            field = value
            requestLayout()
        }

    @Suppress("NAME_SHADOWING")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        maxHeightProportion?.let {
            val heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                (resources.displayMetrics.heightPixels / it), MeasureSpec.EXACTLY
            )
            val widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                measuredWidth, MeasureSpec.EXACTLY
            )
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }
    }
}