package com.example.sergey.shlypa2.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.example.sergey.shlypa2.R

class ArchView @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        View(context, attrs, defStyleAttr) {

    private var path: Path = Path()

    private val paint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        setShadowLayer(16F, 0f, 0f, ContextCompat.getColor(context,
                R.color.shadowColor))
    }

    private var arcHeight = -1F

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        attrs?.let {
            val array = context.obtainStyledAttributes(it, R.styleable.ArchView)
            val color = array.getColor(R.styleable.ArchView_arch_color, Color.BLUE)
            paint.color = color
            arcHeight = array.getDimension(R.styleable.ArchView_arch_height, -1F)
            array.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawPath(path, paint)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if(arcHeight == -1F) arcHeight = height * 0.9F
        if (changed) createPath()
    }

    private fun createPath() {
        path = Path()

        val yNull = (height - arcHeight)
        val yMax = height.toFloat() * 0.9f
        val xMin = -5F
        val xMax = width + 5F
        path.lineTo(xMin, yMax)
        path.cubicTo(width.toFloat() * 0.15f, yNull, width.toFloat() * 0.4F, yNull,
                width.toFloat() / 2, yNull)
        path.cubicTo(width.toFloat() * 0.6f, yNull, width.toFloat() * 0.85f, yNull,
                xMax, yMax)
        path.lineTo(xMax, 0f)

        path.close()
    }
}