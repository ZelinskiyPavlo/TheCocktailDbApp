package com.test.cube.view.draw

import android.content.Context
import android.graphics.Canvas
import com.test.cube.view.CubeModel
import com.test.cube.view.animation.cube.data.CubeAnimationModel
import com.test.cube.view.animation.triangle.data.TriangleAnimationModel

class DrawManager(context: Context, cubeModel: CubeModel) {

    private val drawController = DrawController(cubeModel)

    fun draw(canvas: Canvas) = drawController.draw(canvas)

    fun updateTriangleValue(triangleAnimationModel: TriangleAnimationModel) =
        drawController.updateTriangleAnimationModel(triangleAnimationModel)

    fun updateCubeValue(cubeAnimationModel: CubeAnimationModel) =
        drawController.updateCubeAnimationModel(cubeAnimationModel)

    fun applyDrawingChanges() = drawController.applyDrawingChanges()

}