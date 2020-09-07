package com.test.thecocktaildb.presentation.ui.custom

import android.animation.*
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.use
import com.test.thecocktaildb.R
import com.test.thecocktaildb.presentation.extension.getBooleanAttributeOrNull
import com.test.thecocktaildb.presentation.extension.getColorAttributeOrNull
import com.test.thecocktaildb.presentation.extension.getDimensionAttributeOrNull
import com.test.thecocktaildb.presentation.extension.getIntegerAttributeOrNull
import kotlin.math.max
import kotlin.math.min

class SeekBarView(context: Context, attrs: AttributeSet? = null) :
    View(context, attrs) {

    var min: Int = 1
        set(value) {
            field = value
            minThumbValue = value
        }

    var max: Int = 100
        set(value) {
            field = value
            maxThumbValue = field
        }

    var backgroundBarStrokeWidth: Int

    var foregroundBarStrokeWidth: Int

    var progressBackground: Int

    var progressForeground: Int

    var thumbColorActive: Int = Color.RED

    var thumbSizeActive: Float = context.resources.displayMetrics.density * 32F

    var thumbColorInActive: Int = Color.CYAN

    var thumbSizeInactive: Float = context.resources.displayMetrics.density * 24F

    var isRoundedCorners: Boolean = true

    var leftBackgroundGradientColor: Int? = null

    var rightBackgroundGradientColor: Int? = null

    var leftForegroundGradientColor: Int? = null

    var rightForegroundGradientColor: Int? = null

    var seekBarChangeListener: SeekBarChangeListener? = null

    private val seekBarPaint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private var minThumbValue: Int = 0

    private var maxThumbValue: Int = 0

    private var lastMinThumbValue = minThumbValue

    private var lastMaxThumbValue = maxThumbValue

    private var selectedThumb: Int = THUMB_NONE

    private var touchRadius: Int

    private var offset: Int = 0

    private var sidePadding: Int = 0

    private var isBackgroundGradientEnabled: Boolean = false

    private var isForegroundGradientEnabled: Boolean = false

    //region SetRange
    private var setRangeMin: Int = 0
    private var setRangeMax: Int = 0
    private var setRangeValueAnimator = ValueAnimator()
    private var isSetRangeAnimationIsRunning: Boolean = false
    //endregion

    //region AnimateThumb
    private var currentColor: Int = thumbColorInActive
        set(value) {
            if (value == field) return
            invalidate()
            field = value
        }
    private var currentSize: Float = thumbSizeInactive
        set(value) {
            if (value == field) return
            invalidate()
            field = value
        }

    private var colorValueAnimator = ValueAnimator()
    private var sizeValueAnimator = ValueAnimator()

    private var isAnimationIsRunning: Boolean = false
    private var currentThumb: Int = THUMB_NONE

    private var animatorSet = AnimatorSet()
    //endregion

    init {
        min = 1
        max = 100

        progressBackground = Color.BLACK
        progressForeground = Color.WHITE

        backgroundBarStrokeWidth = (resources.displayMetrics.density * 8F).toInt()
        foregroundBarStrokeWidth = (resources.displayMetrics.density * 8F).toInt()

        touchRadius = (resources.displayMetrics.density * 40F).toInt()
        sidePadding = (resources.displayMetrics.density * 16F).toInt()

        context.obtainStyledAttributes(attrs, R.styleable.SeekBarView).use { typedArray ->
            with(typedArray) {
                getIntegerAttributeOrNull(R.styleable.SeekBarView_sbv_min_value)
                    ?.let { min = it }
                getIntegerAttributeOrNull(R.styleable.SeekBarView_sbv_max_value)
                    ?.let { max = it }
                getBooleanAttributeOrNull(R.styleable.SeekBarView_sbv_is_rounded_corners)
                    ?.let { isRoundedCorners = it }
                getColorAttributeOrNull(R.styleable.SeekBarView_sbv_progress_background)
                    ?.let { progressBackground = it }
                getColorAttributeOrNull(R.styleable.SeekBarView_sbv_progress_foreground)
                    ?.let { progressForeground = it }
                getColorAttributeOrNull(R.styleable.SeekBarView_sbv_thumb_active_color)
                    ?.let { thumbColorActive = it }
                getDimensionAttributeOrNull(R.styleable.SeekBarView_sbv_thumb_active_size)
                    ?.let { thumbSizeActive = it }
                getColorAttributeOrNull(R.styleable.SeekBarView_sbv_thumb_inactive_color)
                    ?.let { thumbColorInActive = it }
                getDimensionAttributeOrNull(R.styleable.SeekBarView_sbv_thumb_inactive_size)
                    ?.let { thumbSizeInactive = it }

                leftBackgroundGradientColor =
                    getColorAttributeOrNull(R.styleable.SeekBarView_sbv_left_background_gradient_color)
                rightBackgroundGradientColor =
                    getColorAttributeOrNull(R.styleable.SeekBarView_sbv_right_background_gradient_color)
                leftForegroundGradientColor =
                    getColorAttributeOrNull(R.styleable.SeekBarView_sbv_left_foreground_gradient_color)
                rightForegroundGradientColor =
                    getColorAttributeOrNull(R.styleable.SeekBarView_sbv_right_foreground_gradient_color)
            }
        }

        if (leftBackgroundGradientColor != null || rightBackgroundGradientColor != null)
            isBackgroundGradientEnabled = true

        if (leftForegroundGradientColor != null || rightForegroundGradientColor != null)
            isForegroundGradientEnabled = true
    }

    override fun getSuggestedMinimumWidth(): Int {
        return (resources.displayMetrics.density * 40F).toInt()
    }

    override fun getSuggestedMinimumHeight(): Int {
        return (resources.displayMetrics.density * 40F).toInt()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val measuredWidth = when (widthMode) {
            MeasureSpec.EXACTLY ->
                if (width < suggestedMinimumWidth) suggestedMinimumWidth
                else width
            else -> width
        }

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val measuredHeight = when (heightMode) {
            MeasureSpec.EXACTLY ->
                if (height < suggestedMinimumHeight) suggestedMinimumHeight
                else height
            else ->
                if (thumbSizeActive + sidePadding < suggestedMinimumHeight) suggestedMinimumHeight
                else (thumbSizeActive + sidePadding).toInt()
        }
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paddingLeft = this.paddingLeft + sidePadding
        val paddingRight = this.paddingRight + sidePadding
        val width = width - paddingLeft - paddingRight
        val verticalCenter = height / 2f
        val minimumX = paddingLeft + (minThumbValue / max.toFloat()) * width
        val maximumX = paddingLeft + (maxThumbValue / max.toFloat()) * width

        updatePaint(backgroundBarStrokeWidth, progressBackground, isRoundedCorners)
        if (isBackgroundGradientEnabled) {
            seekBarPaint.shader = LinearGradient(
                0f,
                verticalCenter,
                paddingRight + width.toFloat() + backgroundBarStrokeWidth / 2,
                verticalCenter,
                intArrayOf(leftBackgroundGradientColor ?: progressBackground, rightBackgroundGradientColor ?: progressBackground),
                floatArrayOf(0f, 1f),
                Shader.TileMode.REPEAT)
        }
        canvas.drawLine(
            paddingLeft + 0f,
            verticalCenter,
            paddingLeft + width.toFloat(),
            verticalCenter,
            seekBarPaint
        )

        updatePaint(foregroundBarStrokeWidth, progressForeground, isRoundedCorners)
        if (isForegroundGradientEnabled) {
            seekBarPaint.shader = LinearGradient(
                minimumX,
                verticalCenter,
                maximumX,
                verticalCenter,
                intArrayOf(leftForegroundGradientColor ?: progressForeground, rightForegroundGradientColor ?: progressForeground),
                floatArrayOf(0f, 1f),
                Shader.TileMode.REPEAT)
        }
        canvas.drawLine(minimumX, verticalCenter, maximumX, verticalCenter, seekBarPaint)

        if (currentThumb == THUMB_MIN)
            drawThumb(canvas, minimumX, true)
        else drawThumb(canvas, minimumX)

        if (currentThumb == THUMB_MAX)
            drawThumb(canvas, maximumX, true)
        else
            drawThumb(canvas, maximumX)
    }

    private fun updatePaint(strokeWidth: Int, color: Int, roundedCaps: Boolean) {
        seekBarPaint.shader = null
        seekBarPaint.color = color
        seekBarPaint.strokeWidth = strokeWidth.toFloat()
        seekBarPaint.strokeCap = if (roundedCaps) Paint.Cap.ROUND else Paint.Cap.SQUARE
    }

    private fun drawThumb(
        canvas: Canvas,
        centerX: Float,
        isAnimated: Boolean = false
    ) {
        val radius =
            if (isAnimated) currentSize / 2
            else thumbSizeInactive / 2

        val centerY = height / 2F

        if (!isAnimated) {
            seekBarPaint.shader = null
            seekBarPaint.color = thumbColorInActive
            seekBarPaint.style = Paint.Style.FILL
        } else {
            seekBarPaint.shader = null
            seekBarPaint.color = currentColor
            seekBarPaint.style = Paint.Style.FILL
        }

        canvas.drawCircle(centerX, centerY, radius, seekBarPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var changed = false
        val paddingLeft = this.paddingLeft + sidePadding
        val paddingRight = this.paddingRight + sidePadding
        val width = width - paddingLeft - paddingRight
        val mx = when {
            event.x < paddingLeft ->
                0
            paddingLeft <= event.x && event.x <= (this.width - paddingRight) ->
                ((event.x - paddingLeft) / width * max).toInt()
            else -> max
        }
        val leftThumbX = (paddingLeft + (minThumbValue / max.toFloat() * width)).toInt()
        val rightThumbX = (paddingLeft + (maxThumbValue / max.toFloat() * width)).toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                when {
                    isInsideRadius(event, leftThumbX, height / 2, touchRadius) -> {
                        selectedThumb = THUMB_MIN
                        offset = mx - minThumbValue
                        changed = true
                        parent.requestDisallowInterceptTouchEvent(true)
                        seekBarChangeListener?.onStartedSeeking()
                        stopSetRangeAnimation()
                        animateThumb(THUMB_MIN)
                        isPressed = true
                    }
                    isInsideRadius(event, rightThumbX, height / 2, touchRadius) -> {
                        selectedThumb = THUMB_MAX
                        offset = maxThumbValue - mx
                        changed = true
                        parent.requestDisallowInterceptTouchEvent(true)
                        seekBarChangeListener?.onStartedSeeking()
                        stopSetRangeAnimation()
                        animateThumb(THUMB_MAX)
                        isPressed = true
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> {
                when (selectedThumb) {
                    THUMB_MIN -> {
                        minThumbValue = max(min(mx - offset, max - 1), 0)
                        changed = true
                    }
                    THUMB_MAX -> {
                        maxThumbValue = min(max(mx + offset, 1), max)
                        changed = true
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                stopThumbAnimation()
                animateThumb(selectedThumb, true)

                selectedThumb = THUMB_NONE
                seekBarChangeListener?.onStoppedSeeking()

                isPressed = false
            }
        }
        keepMinWindow(selectedThumb)

        if (!changed) {
            return false
        }

        invalidate()
        if (lastMinThumbValue != minThumbValue || lastMaxThumbValue != maxThumbValue) {
            lastMinThumbValue = minThumbValue
            lastMaxThumbValue = maxThumbValue
            seekBarChangeListener?.onValueChanged(minThumbValue, maxThumbValue)
        }
        return true
    }

    //region SetRange
    fun setRange(start: Int, end: Int, animate: Boolean) {
        setRangeMin = start
        setRangeMax = end
        if (animate) animateSetRange()
        else {
            minThumbValue = start.coerceAtLeast(min)
            maxThumbValue = end.coerceAtMost(max)
            invalidate()
        }
    }

    private fun animateSetRange() {
        val propertyMin = PropertyValuesHolder.ofInt(THUMB_MIN.toString(), minThumbValue, setRangeMin)
        val propertyMax = PropertyValuesHolder.ofInt(THUMB_MAX.toString(), maxThumbValue, setRangeMax)

        setRangeValueAnimator = ValueAnimator().apply {
            duration = 1500L
            setValues(propertyMin, propertyMax)
            addUpdateListener {
                minThumbValue = it.getAnimatedValue(THUMB_MIN.toString()) as Int
                maxThumbValue = it.getAnimatedValue(THUMB_MAX.toString()) as Int
                seekBarChangeListener?.onValueChanged(minThumbValue, maxThumbValue)
                invalidate()
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(p0: Animator?) {
                    seekBarChangeListener?.onStartedSeeking()
                    isSetRangeAnimationIsRunning = true
                }

                override fun onAnimationEnd(p0: Animator?) {
                    if (isSetRangeAnimationIsRunning) {
                        minThumbValue = setRangeMin.coerceAtLeast(min)
                        maxThumbValue = setRangeMax.coerceAtMost(max)
                        invalidate()
                        isSetRangeAnimationIsRunning = false
                        seekBarChangeListener?.onStoppedSeeking()
                    }
                }

                override fun onAnimationRepeat(p0: Animator?) {
                }
            })
        }
        setRangeValueAnimator.start()
    }

    private fun stopSetRangeAnimation() {
        setRangeValueAnimator.removeAllListeners()
        setRangeValueAnimator.cancel()
        if (isSetRangeAnimationIsRunning) setRangeValueAnimator.end()
    }
    //endregion

    //region AnimateThumb
    private fun animateThumb(thumb: Int, isReversed: Boolean = false) {
        fun startAnimation() {
            animatorSet.removeAllListeners()
            animatorSet.cancel()
            sizeValueAnimator = ValueAnimator.ofFloat(thumbSizeInactive, thumbSizeActive).apply {
                addUpdateListener { currentSize = it.animatedValue as Float }
            }

            colorValueAnimator = ValueAnimator.ofArgb(thumbColorInActive, thumbColorActive).apply {
                addUpdateListener { currentColor = it.animatedValue as Int }
            }

            animatorSet = AnimatorSet().apply {
                playTogether(colorValueAnimator, sizeValueAnimator)
                duration = 1000L
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(p0: Animator?) {
                        isAnimationIsRunning = true
                        currentThumb = thumb
                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        if (isReversed) {
                            isAnimationIsRunning = false
                            currentThumb = THUMB_NONE
                        }
                    }

                    override fun onAnimationRepeat(p0: Animator?) {
                    }
                })
            }
            animatorSet.start()
        }

        fun reverseAnimation() {
            animatorSet.removeAllListeners()
            animatorSet.cancel()
            sizeValueAnimator = ValueAnimator.ofFloat(thumbSizeActive, thumbSizeInactive).apply {
                addUpdateListener { currentSize = it.animatedValue as Float }
            }

            colorValueAnimator = ValueAnimator.ofArgb(thumbColorActive, thumbColorInActive).apply {
                addUpdateListener { currentColor = it.animatedValue as Int }
            }

            animatorSet = AnimatorSet().apply {
                playTogether(colorValueAnimator, sizeValueAnimator)
                duration = 1000L
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(p0: Animator?) {
                        isAnimationIsRunning = true
                        currentThumb = thumb
                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        if (isReversed) {
                            isAnimationIsRunning = false
                            currentThumb = THUMB_NONE
                        }
                    }

                    override fun onAnimationRepeat(p0: Animator?) {
                    }
                })
            }
            animatorSet.start()
        }

        if (!isReversed) startAnimation()
        else reverseAnimation()
    }

    private fun stopThumbAnimation() {
        animatorSet.removeAllListeners()
        animatorSet.cancel()
        animatorSet.end()
    }
    //endregion

    private fun isInsideRadius(event: MotionEvent, cx: Int, cy: Int, radius: Int): Boolean {
        val correctedRadius =
            if (radius < thumbSizeInactive / 2) (thumbSizeInactive / 2).toInt()
            else radius
        val dx = event.x - cx
        val dy = event.y - cy
        return (dx * dx) + (dy * dy) < (correctedRadius * correctedRadius)
    }

    private fun keepMinWindow(base: Int) {
        if (base == THUMB_MAX) {
            if (maxThumbValue <= minThumbValue + 1) {
                minThumbValue = maxThumbValue - 1
            }
        } else if (base == THUMB_MIN) {
            if (minThumbValue > maxThumbValue - 1) {
                maxThumbValue = minThumbValue + 1
            }
        }
    }

    companion object {
        private const val THUMB_NONE = 0
        private const val THUMB_MIN = 1
        private const val THUMB_MAX = 2
    }

    interface SeekBarChangeListener {

        fun onStartedSeeking()

        fun onStoppedSeeking()

        fun onValueChanged(minThumbValue: Int, maxThumbValue: Int)
    }
}