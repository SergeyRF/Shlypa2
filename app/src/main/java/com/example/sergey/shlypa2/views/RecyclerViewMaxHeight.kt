package com.example.sergey.shlypa2.views

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.example.sergey.shlypa2.R

class RecyclerViewMaxHeight @JvmOverloads
constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var maxHeight = Integer.MAX_VALUE

    init {
        attrs?.let {
            initAttributes(context, it)
        }
    }

    private fun initAttributes(
            context: Context,
            attrs: AttributeSet
    ) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.RecyclerViewMaxHeight)

        try {
            maxHeight = array.getDimension(R.styleable.RecyclerViewMaxHeight_rvMaxHeight, Float.MAX_VALUE)
                    .toInt()
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            array.recycle()
        }
    }

    override fun onMeasure(
            widthMeasureSpec: Int,
            heightMeasureSpec: Int
    ) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        val height = Math.min(maxHeight, measuredHeight)
        setMeasuredDimension(width, height)
    }
}