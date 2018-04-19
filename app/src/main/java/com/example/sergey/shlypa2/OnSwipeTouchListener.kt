package com.example.sergey.shlypa2

/**
 * Created by sergey on 4/18/18.
 */

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import timber.log.Timber

/**
 * Created by alex on 1/14/18.
 */

open class OnSwipeTouchListener : View.OnTouchListener {

    private val gestureDetector = GestureDetector(GestureListener())

    var scrollListener : ((distX : Float, distY : Float) -> Unit)? = null

    var swipeTressHold = 100
    var swipeVelocityTressHold = 50

    override fun onTouch(v: View, event: MotionEvent): Boolean {

        if(gestureDetector.onTouchEvent(event)) {
            return true
        } else if(event.action == MotionEvent.ACTION_UP) {
            onActionUp()
            return true
        } else return false
    }



    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {


        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            var result = false
            onActionUp()
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > swipeTressHold && Math.abs(velocityX) > swipeVelocityTressHold) {
                        if (diffX > 0) {
                            result = onSwipeRight()
                        } else {
                            result = onSwipeLeft()
                        }
                    }
                } else {
                    if (Math.abs(diffY) > swipeTressHold && Math.abs(velocityY) > swipeVelocityTressHold) {
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

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            Timber.d("on scroll x dist $distanceX y dist $distanceY")
            scrollListener?.invoke(distanceX, distanceY)
            return super.onScroll(e1, e2, distanceX, distanceY)
        }


    }

   open fun onSwipeRight(): Boolean {
        return false
    }

    open fun onSwipeLeft(): Boolean {
        return false
    }

    open fun onActionUp() {
        Timber.d("onActionUp")
    }

    open fun onSwipeTop(): Boolean {
        return false
    }

    open fun onSwipeBottom(): Boolean {
        return false
    }

}

