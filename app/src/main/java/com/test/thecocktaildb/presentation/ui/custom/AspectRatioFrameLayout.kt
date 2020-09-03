package com.test.thecocktaildb.presentation.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.res.use
import com.test.thecocktaildb.R
import kotlin.math.roundToInt

class AspectRatioFrameLayout : FrameLayout {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    var aspectRatio: Float = 1.0F
        set(value) {
            if (field == value) return
            field = value
            requestLayout()
        }

    var mode = Mode.WIDTH
        set(value) {
            if (field == value) return
            field = value
            requestLayout()
        }

    var minWidth: Float? = null
        set(value) {
            if (field == value) return
            field = value
            requestLayout()
        }
    var minHeight: Float? = null
        set(value) {
            if (field == value) return
            field = value
            requestLayout()
        }

    private fun init(attrs: AttributeSet? = null) {
        context.obtainStyledAttributes(attrs, R.styleable.AspectRatioFrameLayout)
            .use { typedArray ->
                aspectRatio = typedArray.getFloat(
                    R.styleable.AspectRatioFrameLayout_asfl_aspect_ratio,
                    aspectRatio
                )
                mode = Mode.values()[typedArray.getInt(
                    R.styleable.AspectRatioFrameLayout_asfl_mode,
                    Mode.WIDTH.ordinal
                )]
                if (typedArray.hasValue(R.styleable.AspectRatioFrameLayout_asfl_min_width))
                    minWidth =
                        typedArray.getDimension(
                            R.styleable.AspectRatioFrameLayout_asfl_min_width,
                            -1f
                        )
                if (typedArray.hasValue(R.styleable.AspectRatioFrameLayout_asfl_min_height))
                    minHeight =
                        typedArray.getDimension(
                            R.styleable.AspectRatioFrameLayout_asfl_min_height,
                            -1f
                        )
            }
    }

    @Suppress("NAME_SHADOWING")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        when (mode) {
            Mode.WIDTH -> {
                var correctedWidth: Float? = null

                minWidth?.let {
                    if (measuredWidth < it) correctedWidth = it
                }

                val newHeight = ((correctedWidth?.toInt() ?: measuredWidth) / aspectRatio)
                    .takeIf { it.isFinite() }
                    ?.roundToInt()
                    ?.coerceAtLeast(0)
                    ?: 0
                val widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    correctedWidth?.toInt() ?: measuredWidth,
                    MeasureSpec.EXACTLY
                )
                val heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    if (minHeight != null && newHeight < minHeight!!) minHeight!!.toInt()
                    else newHeight,
                    MeasureSpec.EXACTLY
                )
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            }
            Mode.HEIGHT -> {
                var correctedHeight: Float? = null

                minHeight?.let {
                    if (measuredHeight < it) correctedHeight = it
                }

                val newWidth = ((correctedHeight?.toInt() ?: measuredHeight) / aspectRatio)
                    .takeIf { it.isFinite() }
                    ?.roundToInt()
                    ?.coerceAtLeast(0)
                    ?: 0
                val widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    if (minWidth != null && newWidth < minWidth!!) minWidth!!.toInt()
                    else newWidth,
                    MeasureSpec.EXACTLY
                )
                val heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    correctedHeight?.toInt() ?: measuredHeight,
                    MeasureSpec.EXACTLY
                )
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            }
        }
    }

    enum class Mode {
        WIDTH,
        HEIGHT
    }
}