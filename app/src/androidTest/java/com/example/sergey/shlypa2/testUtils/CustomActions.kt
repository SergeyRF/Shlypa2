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
}

