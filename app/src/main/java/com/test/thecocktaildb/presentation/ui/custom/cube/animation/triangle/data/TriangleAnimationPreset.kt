package com.test.thecocktaildb.presentation.ui.custom.cube.animation.triangle.data

sealed class AnimationPreset(
    open val shrinkageScaleSx: Float?,
    open val shrinkageScaleSy: Float?,
    open val shrinkageAngleStart: Float,
    open val shrinkageAngleEnd: Float,
    open val rise_angle: Float,
    open val riseScaleSx: Float?,
    open val riseScaleSy: Float?
)

class FirstFourthAnimationPreset(
    override val shrinkageScaleSx: Float? = 1f,
    override val shrinkageScaleSy: Float? = null,
    override val shrinkageAngleStart: Float = 0f,
    override val shrinkageAngleEnd: Float = 60f,
    override val rise_angle: Float = 60f,
    override val riseScaleSx: Float? = 1f,
    override val riseScaleSy: Float? = null
): AnimationPreset(shrinkageScaleSx, shrinkageScaleSy, shrinkageAngleStart, shrinkageAngleEnd, rise_angle, riseScaleSx, riseScaleSy)

class SecondFifthAnimationPreset(
    override val shrinkageScaleSx: Float? = null,
    override val shrinkageScaleSy: Float? = 1f,
    override val shrinkageAngleStart: Float = 0f,
    override val shrinkageAngleEnd: Float = 30f,
    override val rise_angle: Float = 30f,
    override val riseScaleSx: Float? = null,
    override val riseScaleSy: Float? = 1f
): AnimationPreset(shrinkageScaleSx, shrinkageScaleSy, shrinkageAngleStart, shrinkageAngleEnd, rise_angle, riseScaleSx, riseScaleSy)

class ThirdSixthAnimationPreset(
    override val shrinkageScaleSx: Float? = 1f,
    override val shrinkageScaleSy: Float? = null,
    override val shrinkageAngleStart: Float = 0f,
    override val shrinkageAngleEnd: Float = 180f,
    override val rise_angle: Float = 180f,
    override val riseScaleSx: Float? = 1f,
    override val riseScaleSy: Float? = null
): AnimationPreset(shrinkageScaleSx, shrinkageScaleSy, shrinkageAngleStart, shrinkageAngleEnd, rise_angle, riseScaleSx, riseScaleSy)