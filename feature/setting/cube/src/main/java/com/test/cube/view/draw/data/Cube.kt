package com.test.cube.view.draw.data

import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.tan

internal class Cube(
    points: List<Float>,
    centerHeight: Float
) {
    private val topOval = Oval(points[6], points[9], points[10], centerHeight)
    private val bottomOval = Oval(points[4], centerHeight, points[0], points[3])

    fun rotateCube(angle: Float, topPoints: OvalPoints, bottomPoints: OvalPoints) {
        topOval.movePoints(angle, topPoints)
        bottomOval.movePoints(angle, bottomPoints)
    }
}

internal class Oval(
    private val left: Float,
    private val top: Float,
    private val right: Float,
    private val bottom: Float,
) {

    private val ovalCenterX = (right + left) / 2
    private val ovalCenterY = (bottom + top ) / 2

    fun movePoints(angle: Float, ovalPoints: OvalPoints) {
        val semiMajorAxis = right - ovalCenterX
        val semiMinorAxis = bottom - ovalCenterY

        var pointX = 0f
        var pointY = 0f

        fun calculatePoint(
            correctedAngle: Float,
            predicateByX: () -> Boolean,
            predicateByY: () -> Boolean
        ) {
            val theta = Math.toRadians((correctedAngle).toDouble())

            pointX = (semiMajorAxis * semiMinorAxis) / (sqrt(semiMinorAxis.pow(2) + (semiMajorAxis.pow(2)) * (tan(theta).pow(2)))).toFloat()
            pointY = (semiMajorAxis * semiMinorAxis) / (sqrt(semiMajorAxis.pow(2) + (semiMinorAxis.pow(2)) / (tan(theta).pow(2)))).toFloat()

            if (predicateByX()) pointX = pointX.times(-1)

            if (predicateByY()) pointY = pointY.times(-1)

            pointX += ovalCenterX
            pointY += ovalCenterY
        }

        var newAngle = angle
        calculatePoint(newAngle, {newAngle > 90f && newAngle < 270f}, {newAngle > 180f})
        ovalPoints.rightX = pointX
        ovalPoints.rightY = pointY

        newAngle = angle - 90f
        calculatePoint(newAngle, {newAngle > 90f && newAngle < 270f}, {newAngle > 180f || newAngle < 0f})
        ovalPoints.topX = pointX
        ovalPoints.topY = pointY

        newAngle = 180f - angle
        calculatePoint(newAngle, {newAngle > 90f || newAngle < -90f}, {newAngle > 0f})
        ovalPoints.leftX = pointX
        ovalPoints.leftY = pointY

        newAngle = angle - 270f
        calculatePoint(newAngle, {newAngle > 90f || newAngle < -90f}, {newAngle > -180f && newAngle < 0f})
        ovalPoints.bottomX = pointX
        ovalPoints.bottomY = pointY
    }
}

internal class OvalPoints(
    var leftX: Float = -1f,
    var leftY: Float = -1f,
    var topX: Float = -1f,
    var topY: Float = -1f,
    var rightX: Float = -1f,
    var rightY: Float = -1f,
    var bottomX: Float = -1f,
    var bottomY: Float = -1f
)