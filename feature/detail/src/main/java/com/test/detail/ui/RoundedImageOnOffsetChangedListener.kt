package com.test.detail.ui

import android.content.Context
import android.widget.LinearLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.math.MathUtils
import com.test.detail.databinding.FragmentCocktailDetailsBinding
import kotlin.math.max

internal class RoundedImageOnOffsetChangedListener(
    private val viewDataBinding: FragmentCocktailDetailsBinding,
    context: Context
) : AppBarLayout.OnOffsetChangedListener {

    /** I can't understand why i need to use lazy properties event if I attach this listener in
     *  onResume() function
     * */

    private val maxImageWidth: Int by lazy { viewDataBinding.cocktailImage.width }
    private val imageContainerHeight: Int by lazy { viewDataBinding.imageContainer.height }
    private val minImageWidth: Float = context.resources.displayMetrics.density * 32F
    private val imageMarginHorizontal: Float = context.resources.displayMetrics.density * 48F
    private val imageMarginVertical: Float = context.resources.displayMetrics.density * 16F
    private val layoutParams: LinearLayout.LayoutParams
            by lazy { viewDataBinding.cocktailImage.layoutParams as LinearLayout.LayoutParams }

    private var cachedImageWidth: Int? = null
    private val maxRadius: Float by lazy {
        max(viewDataBinding.ablDetails.width, viewDataBinding.ablDetails.height).toFloat() / 2.0F
    }

    private var setupFlag = false

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        if (setupFlag) {
            viewDataBinding.imageContainer.layoutParams.height =
                viewDataBinding.cocktailImage.height
            cachedImageWidth = maxImageWidth
            viewDataBinding.imageContainer.requestLayout()

            setupFlag = false
        }

        val fraction = (-verticalOffset).toFloat() / appBarLayout.totalScrollRange.toFloat()

        val currentImageWidth =
            MathUtils.lerp(maxImageWidth.toFloat(), minImageWidth, fraction).toInt()
        val currentMarginVertical =
            MathUtils.lerp(0f, (imageContainerHeight / 2 - imageMarginVertical), fraction).toInt()
        val currentMarginHorizontal =
            MathUtils.lerp(0f, imageMarginHorizontal, fraction).toInt()

        with(layoutParams) {
            width = currentImageWidth
            setMargins(
                currentMarginHorizontal,
                currentMarginVertical,
                currentMarginHorizontal,
                currentMarginVertical
            )
        }

        cachedImageWidth = currentImageWidth
        viewDataBinding.cocktailImage.layoutParams = layoutParams
        viewDataBinding.cocktailImage.cornerRadius = MathUtils.lerp(0.0F, maxRadius, fraction)
    }
}