package com.test.cube.view.draw.data

import android.graphics.PointF
import com.test.thecocktaildb.presentation.ui.custom.cube.animation.triangle.data.AnimationPreset

class Triangle(
    val x1: Float,
    val y1: Float,
    val x2: Float,
    val y2: Float,
    val x3: Float,
    val y3: Float,
    val triangleNumber: Int,
    val animationPreset: AnimationPreset
) {

    val centerPoint: PointF by lazy { findCenterOfTriangle(x1, y1, x2, y2, x3, y3) }

    private fun findCenterOfTriangle(
        x1: Float, y1: Float,
        x2: Float, y2: Float,
        x3: Float, y3: Float
    ): PointF {
        val centerX = (x1 + x2 + x3) / 3
        val centerY = (y1 + y2 + y3) / 3
        return PointF(centerX, centerY)
    }
}