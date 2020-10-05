package com.test.cube.view.draw

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import com.test.thecocktaildb.presentation.ui.custom.cube.CubeModel
import com.test.thecocktaildb.presentation.ui.custom.cube.animation.cube.data.CubeAnimationModel
import com.test.thecocktaildb.presentation.ui.custom.cube.animation.triangle.TriangleAnimationType
import com.test.thecocktaildb.presentation.ui.custom.cube.animation.triangle.data.TriangleAnimationModel
import com.test.thecocktaildb.presentation.ui.custom.cube.draw.data.OvalPoints
import com.test.thecocktaildb.presentation.ui.custom.cube.draw.data.Triangle

class DrawController(
    private val cubeModel: CubeModel
) {

    private var triangleAnimationModel: TriangleAnimationModel? = null
    private var cubeAnimationModel: CubeAnimationModel? = null
    private val currentTrianglePath = Path()
    private val rotateMatrix = Matrix()

    private var isCubeAnimationRunning: Boolean = false
    private val topCubePath = Path()
    private val bottomCubeFacePath = Path()
    private val topOvalPoints = OvalPoints()
    private val bottomOvalPoints = OvalPoints()

    private val foregroundPaint = Paint().apply {
        color = cubeModel.lineColor
        strokeWidth = cubeModel.lineStrokeDpWidth
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        style = Paint.Style.STROKE
    }
    private val currentAnimatedPoints = FloatArray(6)
    private val pointPaint = Paint().apply {
        color = cubeModel.pointColor
        strokeWidth = cubeModel.pointRadius
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        style = Paint.Style.FILL
    }

    fun updateTriangleAnimationModel(triangleAnimationModel: TriangleAnimationModel) {
        this.triangleAnimationModel = triangleAnimationModel
        isCubeAnimationRunning = false
    }

    fun updateCubeAnimationModel(cubeAnimationModel: CubeAnimationModel) {
        this.cubeAnimationModel = cubeAnimationModel
        isCubeAnimationRunning = true
    }

    fun draw(canvas: Canvas) {
        drawBackground(canvas)
        drawForeground(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawColor(cubeModel.backGroundColor)
    }

    private fun drawForeground(canvas: Canvas) {
        if (isCubeAnimationRunning) drawCube(canvas)
        else drawTriangles(canvas)
    }

    private fun drawTriangles(canvas: Canvas) {
        // Unnecessary object allocation here (creating new list everyTime)
        val relativeAnimationPosition = triangleAnimationModel?.relativeAnimationPosition ?: emptyList()
        val numberOfTriangles = cubeModel.numberOfTriangles

        (0 until numberOfTriangles).forEach { i ->
            if (relativeAnimationPosition.contains(i).not()) drawTriangle(canvas, i, false)
        }

        relativeAnimationPosition.forEachIndexed { index, trianglePosition ->
            drawTriangle(
                canvas,
                trianglePosition,
                true,
                when(triangleAnimationModel?.runningAnimationPosition!![index] % 3){
                    0 -> TriangleAnimationType.ROTATE_BY_Z
                    1 -> TriangleAnimationType.ROTATE_BY_Y_SHRINKAGE
                    2 -> TriangleAnimationType.ROTATE_BY_Y_RISE
                    else -> throw IllegalStateException("Error when calculating animation type")
                })
        }
    }

    private fun drawTriangle(
        canvas: Canvas,
        position: Int,
        isAnimation: Boolean,
        triangleAnimationType: TriangleAnimationType? = null
    ) {
        val trianglesList = cubeModel.trianglesList

        if (trianglesList.isNullOrEmpty() || position > trianglesList.size - 1) return

        val triangle = trianglesList[position]

        currentTrianglePath.rewind()
        rotateMatrix.reset()

        currentTrianglePath.moveTo(triangle.x1, triangle.y1)
        currentTrianglePath.lineTo(triangle.x2, triangle.y2)
        currentTrianglePath.lineTo(triangle.x3, triangle.y3)
        currentTrianglePath.close()

        if (isAnimation) drawTriangleWithAnimation(canvas, triangle, triangleAnimationType!!)
        else {
            drawTriangle(canvas, currentTrianglePath)
            if (cubeModel.isPointsEnabled) drawPoints(canvas, triangle)
        }
    }

    private fun drawTriangleWithAnimation(
        canvas: Canvas,
        triangle: Triangle,
        triangleAnimationType: TriangleAnimationType
    ) {
        val angle = triangleAnimationModel!!.angle
        val scale = triangleAnimationModel!!.scale
        val scaleAngle = triangleAnimationModel!!.scaleAngle

        when (triangleAnimationType) {
            TriangleAnimationType.ROTATE_BY_Z ->
                rotateMatrix.setRotate(
                    angle,
                    triangle.centerPoint.x,
                    triangle.centerPoint.y
                )
            TriangleAnimationType.ROTATE_BY_Y_SHRINKAGE -> {
                rotateMatrix.postRotate(
                    60F,
                    triangle.centerPoint.x,
                    triangle.centerPoint.y
                )
                rotateMatrix.postScale(
                    triangle.animationPreset.shrinkageScaleSx ?: scale,
                    triangle.animationPreset.shrinkageScaleSy ?: scale,
                    triangle.centerPoint.x,
                    triangle.centerPoint.y
                )
                rotateMatrix.postRotate(
                    scaleAngle.times(-1),
                    triangle.centerPoint.x,
                    triangle.centerPoint.y
                )
            }
            TriangleAnimationType.ROTATE_BY_Y_RISE -> {
                rotateMatrix.postRotate(
                    triangle.animationPreset.rise_angle,
                    triangle.centerPoint.x,
                    triangle.centerPoint.y
                )
                rotateMatrix.postScale(
                    triangle.animationPreset.riseScaleSx ?: scale,
                    triangle.animationPreset.riseScaleSy ?: scale,
                    triangle.centerPoint.x,
                    triangle.centerPoint.y
                )
                rotateMatrix.postRotate(
                    triangle.animationPreset.rise_angle.times(-1),
                    triangle.centerPoint.x,
                    triangle.centerPoint.y
                )
            }
        }

        currentTrianglePath.transform(rotateMatrix)
        drawTriangle(canvas, currentTrianglePath)

        if (cubeModel.isPointsEnabled) drawPoints(canvas, rotateMatrix, triangle)
    }

    private fun drawTriangle(canvas: Canvas, triangle: Path) {
        canvas.drawPath(triangle, foregroundPaint)
    }

    private fun drawCube(canvas: Canvas) {
        val angle = cubeAnimationModel!!.angle

        cubeModel.cube.rotateCube(angle, topOvalPoints, bottomOvalPoints)

        formTopCubeShape()
        formBottomCubeShape()

        if (cubeModel.isPointsEnabled) {
            drawPoints(canvas, topOvalPoints)
            drawPoints(canvas, bottomOvalPoints)
        }

        topCubePath.addPath(bottomCubeFacePath)
        canvas.drawPath(topCubePath, foregroundPaint)
    }

    private fun formTopCubeShape() {
        topCubePath.rewind()

        with(topOvalPoints) {
            topCubePath.moveTo(leftX, leftY)
            topCubePath.lineTo(topX, topY)
            topCubePath.moveTo(leftX, leftY)
            topCubePath.lineTo(bottomOvalPoints.leftX, bottomOvalPoints.leftY)

            topCubePath.moveTo(topX, topY)
            topCubePath.lineTo(rightX, rightY)
            topCubePath.moveTo(topX, topY)
            topCubePath.lineTo(bottomOvalPoints.topX, bottomOvalPoints.topY)

            topCubePath.moveTo(rightX, rightY)
            topCubePath.lineTo(bottomX, bottomY)
            topCubePath.moveTo(rightX, rightY)
            topCubePath.lineTo(bottomOvalPoints.rightX, bottomOvalPoints.rightY)

            topCubePath.moveTo(bottomX, bottomY)
            topCubePath.lineTo(leftX, leftY)
            topCubePath.moveTo(bottomX, bottomY)
            topCubePath.lineTo(bottomOvalPoints.bottomX, bottomOvalPoints.bottomY)
        }
    }

    private fun formBottomCubeShape() {
        bottomCubeFacePath.rewind()

        with(bottomOvalPoints) {
            bottomCubeFacePath.moveTo(leftX, leftY)
            bottomCubeFacePath.lineTo(topX, topY)
            bottomCubeFacePath.lineTo(rightX, rightY)
            bottomCubeFacePath.lineTo(bottomX, bottomY)
            bottomCubeFacePath.lineTo(leftX, leftY)
        }
    }

    private fun drawPoints(canvas: Canvas, triangle: Triangle) {
        canvas.drawPoint(triangle.x1, triangle.y1, pointPaint)
        canvas.drawPoint(triangle.x2, triangle.y2, pointPaint)
        canvas.drawPoint(triangle.x3, triangle.y3, pointPaint)
    }

    private fun drawPoints(canvas: Canvas, ovalPoints: OvalPoints) {
        canvas.drawPoint(ovalPoints.leftX, ovalPoints.leftY, pointPaint)
        canvas.drawPoint(ovalPoints.topX, ovalPoints.topY, pointPaint)
        canvas.drawPoint(ovalPoints.rightX, ovalPoints.rightY, pointPaint)
        canvas.drawPoint(ovalPoints.bottomX, ovalPoints.bottomY, pointPaint)
    }

    private fun drawPoints(canvas: Canvas, matrix: Matrix, triangle: Triangle) {
        currentAnimatedPoints[0] = triangle.x1
        currentAnimatedPoints[1] = triangle.y1
        currentAnimatedPoints[2] = triangle.x2
        currentAnimatedPoints[3] = triangle.y2
        currentAnimatedPoints[4] = triangle.x3
        currentAnimatedPoints[5] = triangle.y3

        matrix.mapPoints(currentAnimatedPoints)
        canvas.drawPoints(currentAnimatedPoints, pointPaint)
    }

    fun applyDrawingChanges() {
        foregroundPaint.apply {
            color = cubeModel.lineColor
            strokeWidth = cubeModel.lineStrokeDpWidth
        }
        pointPaint.apply {
            color = cubeModel.pointColor
            strokeWidth = cubeModel.pointRadius
        }
    }
}