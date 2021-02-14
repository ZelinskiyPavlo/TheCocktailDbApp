package com.test.cube.view

import android.animation.TimeInterpolator
import android.graphics.Color
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import com.test.cube.view.animation.triangle.data.FirstFourthAnimationPreset
import com.test.cube.view.animation.triangle.data.SecondFifthAnimationPreset
import com.test.cube.view.animation.triangle.data.ThirdSixthAnimationPreset
import com.test.cube.view.draw.data.Cube
import com.test.cube.view.draw.data.Triangle
import kotlin.math.cos
import kotlin.math.sin

data class CubeModel(
    var width: Int = -1,
    var height: Int = -1,

    @ColorInt
    var backGroundColor: Int = Color.BLACK,

    @ColorInt
    var lineColor: Int = Color.WHITE,
    @Dimension
    var lineStrokeDpWidth: Float = 1.5F,

    var triangleAnimationDuration: Long = 5000L,
    var cubeAnimationDuration: Long = 700L,
    var delayBetweenAnimation: Long = 25L,

    var triangleInterpolator: TimeInterpolator = AccelerateDecelerateInterpolator(),
    var cubeInterpolator: TimeInterpolator = AccelerateDecelerateInterpolator(),

    var isPointsEnabled: Boolean = false,
    @ColorInt
    var pointColor: Int = Color.RED,
    var pointRadius: Float = 16F,

    var willPlayOnlyTriangle: Boolean = false,
    var willPlayOnlyCube: Boolean = false
) {

    val trianglesList: List<Triangle> by lazy { getTriangles() }
    private val circlePointsList = mutableListOf<Float>()

    val cube: Cube by lazy { Cube(circlePointsList, centerHeight) }

    val numberOfTriangles
        get() = trianglesList.size

    private val centerWidth: Float
        get() = width / 2.0F
    private val centerHeight: Float
        get() = height / 2.0F

    private fun getTriangles(): List<Triangle> {
        val circleRadius = width / 2.5F
        circlePointsList.clear()

        var circlePointAngle = -30.0
        (1..6).forEach { _ ->
            circlePointAngle += 60.0

            circlePointsList
                .add(((circleRadius * cos(Math.toRadians(circlePointAngle))) + width / 2.0F).toFloat())
            circlePointsList
                .add(((circleRadius * sin(Math.toRadians(circlePointAngle))) + height / 2.0F).toFloat())
        }

        return mutableListOf<Triangle>().apply {
            add(
                Triangle(
                circlePointsList[2], circlePointsList[3], circlePointsList[4], circlePointsList[5],
                centerWidth, centerHeight, 0,
                FirstFourthAnimationPreset()
            )
            )
            add(
                Triangle(
                circlePointsList[0], circlePointsList[1], circlePointsList[2], circlePointsList[3],
                centerWidth, centerHeight, 1,
                SecondFifthAnimationPreset()
            )
            )
            add(
                Triangle(
                circlePointsList[10], circlePointsList[11], circlePointsList[0], circlePointsList[1],
                centerWidth, centerHeight, 2,
                ThirdSixthAnimationPreset()
            )
            )
            add(
                Triangle(
                circlePointsList[8], circlePointsList[9], circlePointsList[10], circlePointsList[11],
                centerWidth, centerHeight, 3,
                FirstFourthAnimationPreset()
            )
            )
            add(
                Triangle(
                circlePointsList[6], circlePointsList[7], circlePointsList[8], circlePointsList[9],
                centerWidth, centerHeight, 4,
                SecondFifthAnimationPreset()
            )
            )
            add(
                Triangle(
                circlePointsList[4], circlePointsList[5], circlePointsList[6], circlePointsList[7],
                centerWidth, centerHeight, 5,
                ThirdSixthAnimationPreset()
            )
            )
        }.toList()
    }
}