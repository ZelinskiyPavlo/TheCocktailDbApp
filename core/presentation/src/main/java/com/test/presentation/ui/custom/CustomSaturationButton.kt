package com.test.presentation.ui.custom

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class CustomSaturationButton: AppCompatButton {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val colorMatrix = ColorMatrix().apply { setSaturation(0.5f) }

    private val paint = Paint().apply {
        colorFilter = ColorMatrixColorFilter(colorMatrix)
    }

    var saturation: Float = 0.5f
        set(value) {
            colorMatrix.setSaturation(value)
            paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
            setLayerType(LAYER_TYPE_HARDWARE, paint)
            field = value
        }

    init {
        saturation = 0f
    }
}