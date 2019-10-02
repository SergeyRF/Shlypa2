package com.example.sergey.shlypa2.utils.spotligth

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import com.example.sergey.shlypa2.extensions.dpToPx
import com.takusemba.spotlight.shape.Shape

class Square(private val height: Int, private val width: Int) : Shape {

    override fun draw(canvas: Canvas, point: PointF, value: Float, paint: Paint) {
        val currentWidth = value * width
        val currentHeight = value * height
        canvas.drawRoundRect(
                point.x - currentWidth / 2,
                point.y - currentHeight / 2,
                point.x + currentWidth / 2,
                point.y + currentHeight / 2,
                8f.dpToPx,
                8f.dpToPx,
                paint)
    }

    override fun getHeight(): Int {
        return height
    }

    override fun getWidth(): Int {
        return width
    }
}
