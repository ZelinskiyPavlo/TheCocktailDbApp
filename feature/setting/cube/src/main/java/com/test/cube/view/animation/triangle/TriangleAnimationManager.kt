package com.test.cube.view.animation.triangle

import android.animation.*
import com.test.cube.view.CubeModel
import com.test.cube.view.animation.triangle.data.TriangleAnimationModel
import com.test.cube.view.draw.data.Triangle
import com.test.presentation.util.resettableLazy
import com.test.presentation.util.resettableManager

class TriangleAnimationManager(
    private val cubeModel: CubeModel,
    private val animationListener: AnimationListener
) {

    companion object {
        const val PROPERTY_ANGLE = "PROPERTY_ANGLE"
        const val PROPERTY_SCALE = "PROPERTY_SCALE"
        const val PROPERTY_SCALE_ANGLE = "PROPERTY_SCALE_ANGLE"
    }

    private var animatorSet = AnimatorSet()
    private val animationModel = TriangleAnimationModel()

    private val animationPositions = mutableListOf<Int>()

    private val lazyManager = resettableManager()
    private val animatorList by resettableLazy(lazyManager) { createAnimatorList() }

    private val flattenAllAnimationList by resettableLazy(lazyManager) {
        animatorSet.childAnimations.flatMapTo(mutableListOf()) { animator ->
            if (animator is AnimatorSet) animator.childAnimations
            else listOf(animator)
        }.flatMap { animator ->
            if (animator is AnimatorSet) animator.childAnimations
            else listOf(animator)
        }
    }

    private var totalDuration = cubeModel.triangleAnimationDuration
    private var oneTriangleDuration = totalDuration / 6
    private var rotateDuration = oneTriangleDuration / 2
    private var shrinkageDuration = oneTriangleDuration / 4
    private var riseDuration = oneTriangleDuration / 4
    private var rotateDelayDuration = (shrinkageDuration * 0.25).toLong()

    private var isCancelled: Boolean = false

    interface AnimationListener {

        fun onTriangleAnimationUpdated(triangleAnimationModel: TriangleAnimationModel)

        fun onTriangleAnimationEnd()
    }

    fun animate() {
        animatorSet.removeAllListeners()
        animatorSet.cancel()

        if (lazyManager.managedDelegates.isEmpty()) animatorSet = AnimatorSet()

        animatorSet.playSequentially(animatorList)
        animatorSet.addListener(object : AnimatorListenerAdapter() {
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
                if (!isCancelled) animationListener.onTriangleAnimationEnd()
            }
        })

        animatorSet.start()
    }

    private fun createAnimatorList(): List<Animator> {
        totalDuration = cubeModel.triangleAnimationDuration
        oneTriangleDuration = totalDuration / 6
        rotateDuration = oneTriangleDuration / 2
        shrinkageDuration = oneTriangleDuration / 4
        riseDuration = oneTriangleDuration / 4
        rotateDelayDuration = (shrinkageDuration * 0.25).toLong()

        val animatorList = mutableListOf<Animator>()

        animatorList.add(
            createValueAnimator(
                cubeModel.trianglesList[0],
                TriangleAnimationType.ROTATE_BY_Z,
                rotateDuration
            )
        )
        cubeModel.trianglesList.forEachIndexed { index, triangle ->
            if (index != 5) animatorList.add(createMainTriangleAnimatorSet(triangle))
            else animatorList.add(createLastTriangleAnimatorSet(triangle))
        }
        return animatorList
    }

    private fun createMainTriangleAnimatorSet(triangle: Triangle): AnimatorSet {
        return createAnimatorSet(
            AnimationSequence.TOGETHER,
            listOf(
                createAnimatorSet(
                    AnimationSequence.SEQUENTIALLY,
                    listOf(
                        createValueAnimator(
                            triangle,
                            TriangleAnimationType.ROTATE_BY_Y_SHRINKAGE,
                            shrinkageDuration
                        ),
                        createValueAnimator(
                            triangle,
                            TriangleAnimationType.ROTATE_BY_Y_RISE,
                            riseDuration
                        )
                    )
                ),
                createValueAnimator(
                    cubeModel.trianglesList[triangle.triangleNumber.inc()],
                    TriangleAnimationType.ROTATE_BY_Z,
                    delay = rotateDelayDuration,
                    animationDuration = rotateDuration
                )
            )
        )
    }

    private fun createLastTriangleAnimatorSet(triangle: Triangle): AnimatorSet {
        return createAnimatorSet(
            AnimationSequence.SEQUENTIALLY,
            listOf(
                createValueAnimator(
                    triangle,
                    TriangleAnimationType.ROTATE_BY_Y_SHRINKAGE,
                    shrinkageDuration
                ),
                createValueAnimator(triangle, TriangleAnimationType.ROTATE_BY_Y_RISE, riseDuration)
            )
        )
    }

    private fun createAnimatorSet(
        animationSequence: AnimationSequence,
        valueAnimators: List<Animator>,
        animationDuration: Long? = null,
        triangleInterpolator: TimeInterpolator = cubeModel.triangleInterpolator
    ): AnimatorSet {
        return AnimatorSet().apply {
            animationDuration?.let { duration = it }
            interpolator = triangleInterpolator

            when (animationSequence) {
                AnimationSequence.SEQUENTIALLY ->
                    this.playSequentially(valueAnimators)
                AnimationSequence.TOGETHER ->
                    this.playTogether(valueAnimators)
            }
        }
    }

    private fun createValueAnimator(
        triangle: Triangle,
        triangleAnimationType: TriangleAnimationType,
        animationDuration: Long? = null,
        triangleInterpolator: TimeInterpolator = cubeModel.triangleInterpolator,
        delay: Long = 0L
    ): ValueAnimator {
        return when (triangleAnimationType) {
            TriangleAnimationType.ROTATE_BY_Z -> {
                val propertyAngle = PropertyValuesHolder.ofFloat(PROPERTY_ANGLE, 0F, -60F)

                return ValueAnimator().apply {
                    setValues(propertyAngle)
                    startDelay = delay
                    animationDuration?.let { duration = it }
                    interpolator = triangleInterpolator
                    addUpdateListener { valueAnimator ->
                        onAnimationUpdate(valueAnimator, TriangleAnimationType.ROTATE_BY_Z)
                    }
                }
            }

            TriangleAnimationType.ROTATE_BY_Y_SHRINKAGE -> {
                val propertyScale = PropertyValuesHolder.ofFloat(PROPERTY_SCALE, 1f, 0.0f)
                val propertyAngle = PropertyValuesHolder.ofFloat(
                    PROPERTY_SCALE_ANGLE,
                    triangle.animationPreset.shrinkageAngleStart,
                    triangle.animationPreset.shrinkageAngleEnd
                )

                ValueAnimator().apply {
                    setValues(propertyAngle, propertyScale)
                    animationDuration?.let { duration = it }
                    interpolator = triangleInterpolator
                    addUpdateListener { valueAnimator ->
                        onAnimationUpdate(
                            valueAnimator,
                            TriangleAnimationType.ROTATE_BY_Y_SHRINKAGE
                        )
                    }
                }
            }

            TriangleAnimationType.ROTATE_BY_Y_RISE -> {
                val propertyScale = PropertyValuesHolder.ofFloat(PROPERTY_SCALE, 0f, 1f)

                ValueAnimator().apply {
                    setValues(propertyScale)
                    interpolator = triangleInterpolator
                    animationDuration?.let { duration = it }
                    addUpdateListener { valueAnimator ->
                        onAnimationUpdate(valueAnimator, TriangleAnimationType.ROTATE_BY_Y_RISE)
                    }
                }
            }
        }
    }

    private fun onAnimationUpdate(
        valueAnimator: ValueAnimator,
        triangleAnimationType: TriangleAnimationType
    ) {
        when (triangleAnimationType) {
            TriangleAnimationType.ROTATE_BY_Z ->
                animationModel.angle = valueAnimator.getAnimatedValue(PROPERTY_ANGLE) as Float
            TriangleAnimationType.ROTATE_BY_Y_SHRINKAGE -> {
                animationModel.scale = valueAnimator.getAnimatedValue(PROPERTY_SCALE) as Float
                animationModel.scaleAngle =
                    valueAnimator.getAnimatedValue(PROPERTY_SCALE_ANGLE) as Float
            }
            TriangleAnimationType.ROTATE_BY_Y_RISE ->
                animationModel.scale = valueAnimator.getAnimatedValue(PROPERTY_SCALE) as Float
        }


        animationModel.triangleAnimationType = triangleAnimationType
        animationModel.runningAnimationPosition = getRunningAnimationPosition()
        animationModel.numberOfTriangleAnimation = animatorSet.childAnimations.size

        animationListener.onTriangleAnimationUpdated(animationModel)
    }

    private fun getRunningAnimationPosition(): List<Int> {
        animationPositions.clear()
        flattenAllAnimationList.forEachIndexed { index, animator ->
            if (animator.isRunning) animationPositions.add(index)
        }
        return animationPositions
    }

    fun isTriangleAnimationRunning(): Boolean {
        return animatorSet.isRunning
    }

    fun stopTriangleAnimation() {
        animatorSet.removeAllListeners()
        animatorSet.cancel()
        animatorSet.end()
    }

    fun applyAnimationChanges() {
        lazyManager.reset()
    }

    enum class AnimationSequence {
        TOGETHER,
        SEQUENTIALLY
    }
}