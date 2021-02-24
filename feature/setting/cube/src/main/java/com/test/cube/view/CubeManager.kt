package com.test.cube.view

import android.content.Context
import com.test.cube.view.animation.cube.CubeAnimationManager
import com.test.cube.view.animation.cube.data.CubeAnimationModel
import com.test.cube.view.animation.triangle.TriangleAnimationManager
import com.test.cube.view.animation.triangle.data.TriangleAnimationModel
import com.test.cube.view.draw.DrawManager

internal class CubeManager(
    context: Context,
    private val cubeModel: CubeModel,
    private val listener: AnimationListener
): TriangleAnimationManager.AnimationListener, CubeAnimationManager.AnimationListener {

    val drawManager = DrawManager(context, cubeModel)
    private val triangleAnimationManager = TriangleAnimationManager(cubeModel, this)
    private val cubeAnimationManager = CubeAnimationManager(cubeModel, this)

    interface AnimationListener {

        fun onAnimationUpdate()

        fun onAnimationRestart()
    }

    fun handleAnimation() {
        when {
            isAnimationRunning() -> stopAllAnimation()
            else -> animate()
        }
    }

    private fun animate(isTriangleEnd: Boolean = false) {
        when {
            cubeModel.willPlayOnlyTriangle -> triangleAnimationManager.animate()
            cubeModel.willPlayOnlyCube -> cubeAnimationManager.animate()
            isTriangleEnd -> cubeAnimationManager.animate()
            else -> triangleAnimationManager.animate()
        }
    }

    fun isAnimationRunning(): Boolean {
        return triangleAnimationManager.isTriangleAnimationRunning()
                || cubeAnimationManager.isCubeAnimationRunning()
    }

    fun applyChanges(changesType: CubeView.ChangesType) {
        when (changesType) {
            CubeView.ChangesType.DRAW_CHANGES -> {
                drawManager.applyDrawingChanges()
                if (isAnimationRunning().not()) listener.onAnimationUpdate()
            }
            CubeView.ChangesType.ANIMATION_CHANGES -> {
                cubeAnimationManager.stopCubeAnimation()
                triangleAnimationManager.stopTriangleAnimation()
                drawManager.updateCubeValue(CubeAnimationModel())
                drawManager.updateTriangleValue(TriangleAnimationModel())
                triangleAnimationManager.applyAnimationChanges()
                listener.onAnimationRestart()
            }
        }
    }

    private fun stopAllAnimation() {
        val isCubeAnimationRunning = cubeAnimationManager.stopCubeAnimation()
        if (!isCubeAnimationRunning) triangleAnimationManager.stopTriangleAnimation()
        drawManager.updateCubeValue(CubeAnimationModel())
        drawManager.updateTriangleValue(TriangleAnimationModel())
        listener.onAnimationUpdate()
    }

    override fun onTriangleAnimationUpdated(triangleAnimationModel: TriangleAnimationModel) {
        drawManager.updateTriangleValue(triangleAnimationModel)
        listener.onAnimationUpdate()
    }

    override fun onCubeAnimationUpdated(cubeAnimationModel: CubeAnimationModel) {
        drawManager.updateCubeValue(cubeAnimationModel)
        listener.onAnimationUpdate()
    }

    override fun onTriangleAnimationEnd() {
        animate(true)
    }

    override fun onCubeAnimationEnd() {
        listener.onAnimationRestart()
    }
}