package com.example.sergey.shlypa2.utils.glide

import android.graphics.*
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class CircleBorderTransform (private val color: Int,
                             private val borderWidth: Int) : BitmapTransformation() {

    //todo add border and fill options

    override fun transform(pool: BitmapPool,
                           toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val width = toTransform.width
        val height = toTransform.height

        val config = if (toTransform.config != null) toTransform.config else Bitmap.Config.ARGB_8888
        val bitmap = pool.get(width, height, config)

        val borderPaint = Paint()
        borderPaint.isAntiAlias = true
        borderPaint.color = color
        borderPaint.style = Paint.Style.FILL_AND_STROKE
        borderPaint.strokeWidth = borderWidth.toFloat()

        val bitmapPaint = Paint()
        bitmapPaint.isAntiAlias = true
        bitmapPaint.isFilterBitmap = true
        bitmapPaint.isDither = true

        val newWidth = width - borderWidth * 2
        val newHeight = height - borderWidth * 2
        val scaledBitmap = Bitmap.createScaledBitmap(toTransform, newWidth, newHeight, true)

        val canvas = Canvas(bitmap)

        val circleRadius = (height - borderWidth).toFloat() / 2

        canvas.drawCircle(height.toFloat() / 2, width.toFloat() / 2, circleRadius, borderPaint)
        canvas.drawBitmap(scaledBitmap, borderWidth.toFloat(), borderWidth.toFloat(), bitmapPaint)
        scaledBitmap.recycle()

        return bitmap
    }

    override fun toString(): String {
        return "CircleBorderTransform(color=$color)"
    }

    override fun equals(other: Any?): Boolean {
        return other is CircleBorderTransform && other.color == color
    }

    override fun hashCode(): Int {
        return ID.hashCode() + color * 10
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((ID + color).toByteArray(Key.CHARSET))
    }

    companion object {

        private const val ID = "com.example.sergey.shlypa2.utils.glide.CircleBorderTransform"
    }
}