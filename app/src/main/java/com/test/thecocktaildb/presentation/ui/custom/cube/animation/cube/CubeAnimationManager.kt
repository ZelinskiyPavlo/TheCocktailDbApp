package com.test.thecocktaildb.presentation.ui.custom.cube.animation.cube

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import com.test.thecocktaildb.presentation.ui.custom.cube.CubeModel
import com.test.thecocktaildb.presentation.ui.custom.cube.animation.cube.data.CubeAnimationModel

class CubeAnimationManager(
    private val cubeModel: CubeModel,
    private val animationListener: AnimationListener
) {

    private val propertyAngle = PropertyValuesHolder.ofFloat(PROPERTY_ANGLE, 90f, 0f)
    private val cubeAnimationModel = CubeAnimationModel()

    private var isCancelled: Boolean = false

    companion object {
        const val PROPERTY_ANGLE = "PROPERTY_ANGLE"
    }

    interface AnimationListener {

        fun onCubeAnimationUpdated(cubeAnimationModel: CubeAnimationModel)

        fun onCubeAnimationEnd()
    }

    private val valueAnimator = ValueAnimator()

    fun animate() {
        valueAnimator.cancel()
        valueAnimator.removeAllListeners()

        valueAnimator.apply {
            setValues(propertyAngle)
            startDelay = cubeModel.delayBetweenAnimation
            duration = cubeModel.cubeAnimationDuration
            interpolator = cubeModel.cubeInterpolator
            addUpdateListener { animator ->
                onCubeAnimationUpdated(animator)
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    super.onAnimationStart(animation)
                    isCancelled = false
                }

                override fun onAnimationCancel(animation: Animator?) {
                    super.onAnimationCancel(animation)
                    isCancelled = true
                }

                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    if (!isCancelled) animationListener.onCubeAnimationEnd()
                }
            })
        }

        valueAnimator.start()
    }

    private fun onCubeAnimationUpdated(valueAnimator: ValueAnimator) {
        val angle = valueAnimator.getAnimatedValue(PROPERTY_ANGLE) as Float

        cubeAnimationModel.angle = angle

        animationListener.onCubeAnimationUpdated(cubeAnimationModel)
    }

    fun isCubeAnimationRunning(): Boolean {
        return valueAnimator.isRunning
    }

    fun stopCubeAnimation(): Boolean {
        val isRunning = isCubeAnimationRunning()
        valueAnimator.removeAllListeners()
        valueAnimator.removeAllUpdateListeners()
        valueAnimator.cancel()
//        valueAnimator.end()
        return isRunning
    }
}