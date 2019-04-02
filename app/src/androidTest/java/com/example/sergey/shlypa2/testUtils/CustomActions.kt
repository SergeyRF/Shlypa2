package com.example.sergey.shlypa2.testUtils

import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.viewpager.widget.ViewPager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import android.widget.SeekBar
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.CoordinatesProvider
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Swipe
import androidx.test.espresso.action.GeneralSwipeAction
import androidx.test.espresso.action.ViewActions.actionWithAssertions



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
            return allOf<View>(isAssignableFrom(androidx.recyclerview.widget.RecyclerView::class.java), isDisplayed())
        }

        override fun getDescription(): String {
            return "smooth scroll RecyclerView to position: $position"
        }

        override fun perform(uiController: UiController, view: View) {
            val recyclerView = view as androidx.recyclerview.widget.RecyclerView
            recyclerView.smoothScrollToPosition(position)
        }
    }

    class SlideToPositionAction constructor(private val position: Int) : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return allOf<View>(isAssignableFrom(androidx.viewpager.widget.ViewPager::class.java), isDisplayed())
        }

        override fun getDescription(): String {
            return "Slide view pager to position $position"
        }

        override fun perform(uiController: UiController, view: View) {
            val pager = view as androidx.viewpager.widget.ViewPager
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

