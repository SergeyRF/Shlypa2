package com.example.sergey.shlypa2.extensions

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.SeekBar
import androidx.constraintlayout.motion.widget.MotionLayout
import com.example.sergey.shlypa2.BuildConfig
import java.util.*

/**
 * Created by alex on 4/10/18.
 */

fun SeekBar.onChange(listener: ((seekBar: SeekBar?, progress: Int, fromUser: Boolean) -> Unit)?) {
    this.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            listener?.invoke(seekBar, progress, fromUser)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }
    })
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.isVisible(): Boolean {
    return this.visibility == View.VISIBLE
}

fun View.setVisibility(b: Boolean) {
    visibility = if (b) View.VISIBLE else View.GONE
}

fun View.hideSmooth() {
    this.animate().alpha(0f).start()
}

fun EditText.showKeyboard() {
    this.requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    currentFocus?.windowToken?.let {
        imm.hideSoftInputFromWindow(it, 0)
    }
}

fun View.dimen(id: Int): Int = resources.getDimensionPixelSize(id)

fun View.onDrawn(delay: Long = 0, block: () -> Unit) {
    val view = this
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            view.viewTreeObserver.removeOnGlobalLayoutListener(this)
            Handler().postDelayed({
                block.invoke()
            }, delay)
        }
    })
}

fun View.onPreDraw(block: () -> Unit) {
    val view = this
    view.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            view.viewTreeObserver.removeOnPreDrawListener(this)
            block.invoke()
            return true
        }
    })
}
fun EditText.onActionDone(returnCompleted: Boolean = true,
                          returnNotCompleted: Boolean = true, block: () -> Unit) {
    onAction(EditorInfo.IME_ACTION_DONE, returnCompleted, returnNotCompleted, block)
}

fun EditText.onActionNext(returnCompleted: Boolean = true,
                          returnNotCompleted: Boolean = true,
                          block: () -> Unit) {
    onAction(EditorInfo.IME_ACTION_NEXT, returnCompleted, returnNotCompleted, block)
}

fun EditText.onTouchRightDrawable(block: () -> Unit) {
    setOnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_UP) {
            if (event.rawX >= (right - compoundDrawables[2].bounds.width())) {
                block.invoke()
                return@setOnTouchListener true
            }
        }
        false
    }
}

fun EditText.onAction(action: Int,
                      returnCompleted: Boolean = true,
                      returnNotCompleted: Boolean = true,
                      block: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        return@setOnEditorActionListener when (actionId) {
            action -> {
                block.invoke()
                returnCompleted
            }
            else -> returnNotCompleted
        }
    }
}

/**
 * Returns a random element.
 */
fun <E> List<E>.random(): E? = if (size > 0) get(Random().nextInt(size)) else null

inline fun Any.debug(block: () -> Unit) {
    if (BuildConfig.DEBUG) {
        block.invoke()
    }
}

val Int.dpToPx: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.pxToDp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()

fun MotionLayout.onTransitionCompletedOnce(action: () -> Unit) {
    setTransitionListener(object : MotionLayout.TransitionListener {
        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}

        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}

        override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
            action.invoke()
            p0?.setTransitionListener(null)
        }
    })
}