package com.example.sergey.shlypa2.ui.fragments


import android.animation.ValueAnimator
import android.annotation.SuppressLint
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateInterpolator
import com.example.sergey.shlypa2.OnSwipeTouchListener
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.extensions.hide
import com.example.sergey.shlypa2.extensions.show
import com.example.sergey.shlypa2.screens.game.RoundViewModel
import com.github.florent37.kotlin.pleaseanimate.please
import kotlinx.android.synthetic.main.fragment_game.*
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 */
class GameFragment : androidx.fragment.app.Fragment() {


    lateinit var viewModel: RoundViewModel

    private var cardYPath: Int? = null

    val onSwipeTouchListener = object : OnSwipeTouchListener() {
        override fun onActionUp() {
            cv_word.translationY = 0F
            cv_word.translationX = 0F
        }

        override fun onSwipeTop(): Boolean {
            viewModel.answerWord(true)
            cv_word.hide()
            return true
        }

        override fun onSwipeBottom(): Boolean {
            viewModel.answerWord(false)
            cv_word.hide()
            return true
        }
    }

    private val guideOnGlobal = object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            view?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
            animateGuide()
        }
    }

    //todo can cause problems better to keep it in a viewmodel
    var timerStop: Boolean = false
    private var guideAnimated = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(activity!!).get(RoundViewModel::class.java)

        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.wordLiveData.observe(this, Observer { word: Word? ->
            if (word != null) {
                onNextWord(word)
            }
        })

        onSwipeTouchListener.scrollListener = { x, y ->
            cv_word.translationY = cv_word.translationY - y
        }

        viewModel.timerLiveData.observe(this, Observer { time -> time?.let { onTimer(it) } })

        viewModel.answeredCountLiveData.observe(this, Observer { answered ->
            answered?.let { onAnsweredCount(it) }
        })

        containerGame.setOnClickListener { }
        containerGame.setOnTouchListener(onSwipeTouchListener)

        tv_PlayGame.hide()
        timerLinear.setOnClickListener {
            if (timerStop) {
                resumeTimer()
            } else {
                pauseTimer()
            }
        }

        tv_PlayGame.setOnClickListener {
            resumeTimer()
        }

        btTrue.setOnClickListener {
            viewModel.answerWord(true)
        }

        if(!guideAnimated) {
            view.viewTreeObserver.addOnGlobalLayoutListener (guideOnGlobal)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!timerStop) {
            viewModel.startTimer()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseTimer()
    }

    private fun pauseTimer() {
        tv_PlayGame.show()
        containerGame.setOnTouchListener(null)
        viewModel.pauseTimer()
        tv_word.hide()
        ibStopTime.setImageResource(R.drawable.ic_play_circle_outline_black_24dp)
        timerStop = true
    }

    private fun resumeTimer() {
        viewModel.startTimer()
        tv_PlayGame.hide()
        tv_word.show()
        containerGame.setOnTouchListener(onSwipeTouchListener)
        ibStopTime.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp)
        timerStop = false
    }

    @SuppressLint("SetTextI18n")
    private fun onTimer(time: Int) {
        val minutes = time / 60
        val seconds = time % 60

        tvTime.text = "%02d:%02d".format(minutes, seconds)
    }

    fun onNextWord(word: Word) {
        Timber.d("on Next word")
        tv_word.text = word.word

        Handler().postDelayed({
            if (cv_word != null) cv_word.show()
        }, 100)

        runWordAppearAnimation()
    }

    fun onAnsweredCount(answered: Pair<Int, Int>) {
        tvAnsweredCount.text = answered.first.toString()
    }

    private fun uselessHideAnimation() {
        please(10) {
            animate(tvGuideLabel) {
                invisible()
            }

            animate(ivHand) {
                invisible()
            }
        }.start()
    }

    private fun animateGuide() {
        guideAnimated = true
        please {
            animate(ivHand) {
                visible()
            }

            animate(tvGuideLabel) {
                visible()
            }
        }.thenCouldYou(1000, interpolator = AccelerateInterpolator()) {
            animate(ivHand) {
                topOfHisParent(marginDp = 32f)
                invisible()
            }

            animate(tvGuideLabel) {
                topOfHisParent()
                invisible()
            }
        }.withEndAction {
            tvGuideLabel?.setText(R.string.skip)
        }.thenCouldYou(10) {
            animate(ivHand) {
                visible()
                originalPosition()
            }

            animate(tvGuideLabel) {
                visible()
                originalPosition()
            }
        }.thenCouldYou(1000, interpolator = AccelerateInterpolator()) {
            Timber.d("animate to invisible")
            animate(ivHand) {
                invisible()
                bottomOfHisParent()
            }

            animate(tvGuideLabel) {
                invisible()
                bottomOfHisParent()
            }
        }.thenCouldYou {
            animate(ivHand) {
                originalPosition()
            }

            animate(tvGuideLabel) {
                originalPosition()
            }
        }.withEndAction {
            tvGuideLabel?.setText(R.string.skip)
        }.start()
    }

    fun runWordAppearAnimation() {
        val animator = ValueAnimator.ofFloat(0F, 1F)

        if (cardYPath == null) {
            cardYPath = calculatePath()
        }

        val yPath = cardYPath ?: 300

        animator.addUpdateListener { animation ->
            val animated = animation.animatedValue as Float
            cv_word?.scaleX = animated
            cv_word?.scaleY = animated
            cv_word?.translationY = yPath * (1F - animated)
        }

        animator.duration = 300

        animator.start()
    }

    //Calculate path for a word card from top of a hat to center of the screen
    private fun calculatePath(): Int? {
        val hatTop = ivHat.top
        val cardTop = cv_word.top

        if (hatTop == 0 || cardTop == 0) return null

        val additionalMargin: Float = context?.let { Functions.dpToPx(it, 32F) } ?: 0F

        return Math.abs(hatTop - cardTop) + additionalMargin.toInt()
    }
}
