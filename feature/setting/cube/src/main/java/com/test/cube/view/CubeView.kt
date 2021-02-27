package com.test.cube.view

import android.animation.TimeInterpolator
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.res.use
import com.test.cube.R
import com.test.presentation.extension.getBooleanAttributeOrNull
import com.test.presentation.extension.getColorAttributeOrNull
import com.test.presentation.extension.getDimensionAttributeOrNull
import com.test.presentation.extension.getFloatAttributeOrNull

@Suppress("MemberVisibilityCanBePrivate")
internal class CubeView(context: Context, attrs: AttributeSet?) : View(context, attrs),
    CubeManager.AnimationListener {

    private val cubeModel: CubeModel = CubeModel()
    private val cubeManager: CubeManager = CubeManager(context, cubeModel, this)

    var backgroundColor: Int? = null
        get() = cubeModel.backGroundColor
        set(value) {
            if (value == null || cubeModel.backGroundColor == value) return
            cubeModel.backGroundColor = value
            applyChanges(ChangesType.DRAW_CHANGES)
            field = value
        }
    var lineColor: Int? = null
        get() = cubeModel.lineColor
        set(value) {
            if (value == null || cubeModel.lineColor == value) return
            field = value
            cubeModel.lineColor = value
            applyChanges(ChangesType.DRAW_CHANGES)
        }
    var lineStrokeWidth: Float? = null
        get() = cubeModel.lineStrokeDpWidth
        set(value) {
            if (value == null || cubeModel.lineStrokeDpWidth == value) return
            cubeModel.lineStrokeDpWidth = value
            applyChanges(ChangesType.DRAW_CHANGES)
            field = value
        }
    var pointColor: Int? = null
        get() = cubeModel.pointColor
        set(value) {
            if (value == null || cubeModel.pointColor == value) return
            cubeModel.pointColor = value
            cubeModel.isPointsEnabled = true
            applyChanges(ChangesType.DRAW_CHANGES)
            field = value
        }
    var pointRadius: Float? = null
        get() = cubeModel.pointRadius
        set(value) {
            if (value == null || cubeModel.pointRadius == value) return
            cubeModel.pointRadius = value
            cubeModel.isPointsEnabled = true
            applyChanges(ChangesType.DRAW_CHANGES)
            field = value
        }
    var willPlayOnlyTriangle: Boolean? = null
        get() = cubeModel.willPlayOnlyTriangle
        set(value) {
            if (value == null || cubeModel.willPlayOnlyTriangle == value) return
            cubeModel.willPlayOnlyTriangle = value
            applyChanges(ChangesType.ANIMATION_CHANGES)
            field = value
        }
    var willPlayOnlyCube: Boolean? = null
        get() = cubeModel.willPlayOnlyCube
        set(value) {
            if (value == null || cubeModel.willPlayOnlyCube == value) return
            cubeModel.willPlayOnlyCube = value
            applyChanges(ChangesType.ANIMATION_CHANGES)
            field = value
        }
    var triangleDuration: Long? = null
        get() = cubeModel.triangleAnimationDuration
        set(value) {
            if (value == null || cubeModel.triangleAnimationDuration == value) return
            cubeModel.triangleAnimationDuration = value
            applyChanges(ChangesType.ANIMATION_CHANGES)
            field = value
        }
    var cubeDuration: Long? = null
        get() = cubeModel.cubeAnimationDuration
        set(value) {
            if (value == null || cubeModel.cubeAnimationDuration == value) return
            cubeModel.cubeAnimationDuration = value
            applyChanges(ChangesType.ANIMATION_CHANGES)
            field = value
        }
    var delayBetweenAnimation: Long? = null
        get() = cubeModel.delayBetweenAnimation
        set(value) {
            if (value == null || cubeModel.delayBetweenAnimation == value) return
            cubeModel.delayBetweenAnimation = value
            applyChanges(ChangesType.ANIMATION_CHANGES)
            field = value
        }
    var triangleInterpolator: TimeInterpolator = AccelerateDecelerateInterpolator()
        get() = cubeModel.triangleInterpolator
        set(value) {
            if (cubeModel.triangleInterpolator == value) return
            cubeModel.triangleInterpolator = value
            applyChanges(ChangesType.ANIMATION_CHANGES)
            field = value
        }
    var cubeInterpolator: TimeInterpolator = AccelerateDecelerateInterpolator()
        get() = cubeModel.cubeInterpolator
        set(value) {
            if (cubeModel.cubeInterpolator == value) return
            cubeModel.cubeInterpolator = value
            applyChanges(ChangesType.ANIMATION_CHANGES)
            field = value
        }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.CubeView).use { typedArray ->
            backgroundColor =
                typedArray.getColorAttributeOrNull(R.styleable.CubeView_android_backgroundTint)
            lineColor =
                typedArray.getColorAttributeOrNull(R.styleable.CubeView_cv_line_color)
            lineStrokeWidth =
                typedArray.getDimensionAttributeOrNull(R.styleable.CubeView_cv_line_width)
            pointColor =
                typedArray.getColorAttributeOrNull(R.styleable.CubeView_cv_point_color)
            pointRadius =
                typedArray.getDimensionAttributeOrNull(R.styleable.CubeView_cv_point_radius)
            willPlayOnlyTriangle =
                typedArray.getBooleanAttributeOrNull(R.styleable.CubeView_cv_play_only_triangle)
            willPlayOnlyCube =
                typedArray.getBooleanAttributeOrNull(R.styleable.CubeView_cv_play_only_cube)
            triangleDuration =
                typedArray.getFloatAttributeOrNull(R.styleable.CubeView_cv_triangle_duration)?.toLong()
            cubeDuration =
                typedArray.getFloatAttributeOrNull(R.styleable.CubeView_cv_cube_duration)?.toLong()
            delayBetweenAnimation =
                typedArray.getFloatAttributeOrNull(R.styleable.CubeView_cv_delay_between_anim)?.toLong()
        }
        cubeModel.pointRadius = resources.displayMetrics.density * cubeModel.pointRadius
        cubeModel.lineStrokeDpWidth = resources.displayMetrics.density * cubeModel.lineStrokeDpWidth
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        cubeModel.width = width
        cubeModel.height = height

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) = cubeManager.drawManager.draw(canvas!!)

    override fun onAnimationUpdate() = invalidate()

    override fun onAnimationRestart() {
        handleAnimation()
    }

    private fun applyChanges(changesType: ChangesType) {
        cubeManager.applyChanges(changesType)
    }

    fun handleAnimation() {
        post {
            cubeManager.handleAnimation()
        }
    }

    enum class ChangesType {
        DRAW_CHANGES,
        ANIMATION_CHANGES
    }
}