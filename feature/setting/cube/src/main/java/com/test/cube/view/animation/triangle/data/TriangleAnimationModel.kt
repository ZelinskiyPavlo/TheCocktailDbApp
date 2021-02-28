package com.test.cube.view.animation.triangle.data

import com.test.cube.view.animation.triangle.TriangleAnimationType

internal data class TriangleAnimationModel(
    var angle: Float = -1F,
    var scale: Float = -1F,
    var scaleAngle: Float = -1F,
    var runningAnimationPosition: List<Int> = emptyList(),
    var triangleAnimationType: TriangleAnimationType? = null,
    var numberOfTriangleAnimation: Int = -1
) {

    val relativeAnimationPosition: List<Int>
        get() {
            return if (runningAnimationPosition.isEmpty()) emptyList()
            else runningAnimationPosition.map { position ->
                (position / 3)
            }
        }
}