package com.example.sergey.shlypa2.testUtils

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.view.View
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import android.widget.SeekBar
import android.support.test.espresso.util.HumanReadables
import android.support.test.espresso.PerformException
import android.support.test.espresso.action.CoordinatesProvider
import android.support.test.espresso.action.Press
import android.support.test.espresso.action.Swipe
import android.support.test.espresso.action.GeneralSwipeAction
import android.support.test.espresso.action.ViewActions.actionWithAssertions



/**
 * Created by alex on 4/29/18.
 */

object CustomActions {
    fun smoothScrollTo(position: Int): ScrollToPositionViewAction {
        return ScrollToPositionViewAction(position)
    }

    fun slidePagerTo(position: Int): SlideToPositionAction {
        return SlideToPositionAction(position)
    }

    fun scrubSeekBarAction(progress: Int): ViewAction {
        return actionWithAssertions(GeneralSwipeAction(
                Swipe.SLOW,
                SeekBarThumbCoordinatesProvider(0),
                SeekBarThumbCoordinatesProvider(progress),
                Press.PINPOINT))
    }

    class ScrollToPositionViewAction constructor(private val position: Int) : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return allOf<View>(isAssignableFrom(RecyclerView::class.java), isDisplayed())
        }

        override fun getDescription(): String {
            return "smooth scroll RecyclerView to position: $position"
        }

        override fun perform(uiController: UiController, view: View) {
            val recyclerView = view as RecyclerView
            recyclerView.smoothScrollToPosition(position)
        }
    }

    class SlideToPositionAction constructor(private val position: Int) : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return allOf<View>(isAssignableFrom(ViewPager::class.java), isDisplayed())
        }

        override fun getDescription(): String {
            return "Slide view pager to position $position"
        }

        override fun perform(uiController: UiController, view: View) {
            val pager = view as ViewPager
            pager.currentItem = position
        }
    }



    class SeekBarThumbCoordinatesProvider(internal var mProgress: Int) : CoordinatesProvider {

        private fun getVisibleLeftTop(view: View): FloatArray {
            val xy = IntArray(2)
            view.getLocationOnScreen(xy)
            return floatArrayOf(xy[0].toFloat(), xy[1].toFloat())
        }

        override fun calculateCoordinates(view: View): FloatArray {
            if (view !is SeekBar) {
                throw PerformException.Builder()
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(RuntimeException(String.format("SeekBar expected"))).build()
            }
            val width = view.width - view.paddingLeft - view.paddingRight
            val progress = (if (mProgress == 0) view.progress else mProgress).toDouble()
            val xPosition = (view.paddingLeft + width * progress / view.max).toInt()
            val xy = getVisibleLeftTop(view)
            return floatArrayOf(xy[0] + xPosition, xy[1] + 10)
        }
    }


}

