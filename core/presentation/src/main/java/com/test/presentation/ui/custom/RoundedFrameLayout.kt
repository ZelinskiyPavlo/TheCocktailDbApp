package com.test.presentation.ui.custom

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import androidx.core.content.res.use
import androidx.core.graphics.withSave
import com.test.presentation.R
import com.test.presentation.extension.containsFlag
import kotlin.math.max
import kotlin.math.min

class RoundedFrameLayout : FrameLayout {

    private val is26Plus = Build.VERSION.SDK_INT >= 26

    private val boundsF = RectF()
    private val eraserPath = Path()

    var cornerRadius: Float = 0.toFloat()
        set(value) {
            if (field == value) return
            field = value
            cornersFlag = cornersFlag
        }

    @Suppress("MemberVisibilityCanBePrivate")
    var cornersFlag: Int = CornerFlag.ALL
        set(value) {
            field = max(0, value)
            if (field == CornerFlag.ALL) cornersRadii.fill(cornerRadius)
            else {
                cornersRadii[0] =
                    if (cornersFlag containsFlag (CornerFlag.LEFT_TOP)) cornerRadius else 0.0F
                cornersRadii[1] =
                    if (cornersFlag containsFlag (CornerFlag.LEFT_TOP)) cornerRadius else 0.0F
                cornersRadii[2] =
                    if (cornersFlag containsFlag (CornerFlag.RIGHT_TOP)) cornerRadius else 0.0F
                cornersRadii[3] =
                    if (cornersFlag containsFlag (CornerFlag.RIGHT_TOP)) cornerRadius else 0.0F
                cornersRadii[4] =
                    if (cornersFlag containsFlag (CornerFlag.RIGHT_BOTTOM)) cornerRadius else 0.0F
                cornersRadii[5] =
                    if (cornersFlag containsFlag (CornerFlag.RIGHT_BOTTOM)) cornerRadius else 0.0F
                cornersRadii[6] =
                    if (cornersFlag containsFlag (CornerFlag.LEFT_BOTTOM)) cornerRadius else 0.0F
                cornersRadii[7] =
                    if (cornersFlag containsFlag (CornerFlag.LEFT_BOTTOM)) cornerRadius else 0.0F
            }
            requestLayout()
        }
    private val cornersRadii = FloatArray(8) { cornerRadius }
    private val clampedCornerRadii = FloatArray(8) { cornerRadius }

    private val roundPaint = object : Paint() {
        init {
            isDither = true
            isAntiAlias = true
            isFilterBitmap = true
            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        }
    }

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        setWillNotDraw(false)

        context.obtainStyledAttributes(attrs, R.styleable.RoundedFrameLayout).use { typedArray ->
            cornersFlag =
                typedArray.getInt(R.styleable.RoundedFrameLayout_rfl_corners, CornerFlag.ALL)

            if (typedArray.hasValue(R.styleable.RoundedFrameLayout_rfl_corners_radius)) {
                cornerRadius = typedArray.getDimension(
                    R.styleable.RoundedFrameLayout_rfl_corners_radius,
                    resources.displayMetrics.density * 4.0F
                )
            }
        }
    }

    private fun invalidateEraserPath() {
        val maxAllowedRadius = (min(width, height) / 2.0F)

        clampedCornerRadii.forEachIndexed { index, _ ->
            clampedCornerRadii[index] = cornersRadii[index].coerceAtMost(maxAllowedRadius)
        }

        if (clampedCornerRadii.any { it > 0.0F }) {
            eraserPath.rewind()
            eraserPath.addRoundRect(boundsF, clampedCornerRadii, Path.Direction.CW)
            eraserPath.close()
        } else {
            eraserPath.reset()
        }

        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                if (!eraserPath.isEmpty && elevation > 0.0F) outline.setConvexPath(eraserPath)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        boundsF.set(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())

        invalidateEraserPath()
    }

    override fun draw(canvas: Canvas?) {
        canvas ?: return

        if (!eraserPath.isEmpty) {
            if (is26Plus) {
                canvas.withSave {
                    canvas.clipPath(eraserPath)
                    super.draw(canvas)
                }
            } else {
                canvas.saveLayer(null, null)
                super.draw(canvas)
                canvas.drawPath(eraserPath, roundPaint)
                canvas.restore()
            }
        } else super.draw(canvas)
    }

    object CornerFlag {
        const val ALL = 0
        const val LEFT_TOP = 1
        const val RIGHT_TOP = 2
        const val LEFT_BOTTOM = 4
        const val RIGHT_BOTTOM = 8
    }
}