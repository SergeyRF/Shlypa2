package com.example.sergey.shlypa2

/**
 * Created by sergey on 4/18/18.
 */

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

/**
 * Created by alex on 1/14/18.
 */

open class OnSwipeTouchListener : View.OnTouchListener {

    private val gestureDetector = GestureDetector(GestureListener())

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {


        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            Log.d(TAG, "onFling: ")
            var result = false
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            result = onSwipeRight()
                        } else {
                            result = onSwipeLeft()
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            result = onSwipeBottom()
                        } else {
                            result = onSwipeTop()
                        }
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }

            return result
        }


    }

   open fun onSwipeRight(): Boolean {
        Log.d(TAG, "onSwipeRight: ")
        return false
    }

    open fun onSwipeLeft(): Boolean {
        Log.d(TAG, "onSwipeLeft: ")
        return false
    }

    fun onSwipeTop(): Boolean {
        Log.d(TAG, "onSwipeTop: ")
        return false
    }

    fun onSwipeBottom(): Boolean {
        Log.d(TAG, "onSwipeBottom: ")
        return false
    }

    companion object {
        const val SWIPE_THRESHOLD = 100
        const val SWIPE_VELOCITY_THRESHOLD = 100
        val TAG = OnSwipeTouchListener::class.java.simpleName
    }

}

