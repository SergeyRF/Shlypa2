package com.example.sergey.shlypa2.screens.game


import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.sergey.shlypa2.OnSwipeTouchListener
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.extensions.dpToPx
import com.example.sergey.shlypa2.extensions.hide
import com.example.sergey.shlypa2.extensions.onTransitionCompletedOnce
import com.example.sergey.shlypa2.extensions.show
import kotlinx.android.synthetic.main.fragment_game.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import kotlin.math.abs


class GameFragment : Fragment() {


    private val viewModel: RoundViewModel by sharedViewModel()

    private var cardYPath: Int? = null

    private val onSwipeTouchListener = object : OnSwipeTouchListener() {
        override fun onActionUp() {
            frameWord.translationY = 0F
            frameWord.translationX = 0F
        }

        override fun onSwipeTop(): Boolean {
            viewModel.answerWord(true)
            frameWord.hide()
            return true
        }

        override fun onSwipeBottom(): Boolean {
            viewModel.answerWord(false)
            frameWord.hide()
            return true
        }
    }

    //todo can cause problems better to keep it in a viewmodel
    var timerStop: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
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
            frameWord.translationY = frameWord.translationY - y
        }

        viewModel.timerLiveData.observe(this, Observer { time -> time?.let { onTimer(it) } })

        viewModel.answeredCountLiveData.observe(this, Observer { answered ->
            answered?.let { onAnsweredCount(it) }
        })

        containerGame.setOnClickListener { }
        containerGame.setOnTouchListener(onSwipeTouchListener)


        timerLinear.setOnClickListener {
            if (timerStop) {
                resumeTimer()
            } else {
                pauseTimer()
            }
        }

        tvResumeGame.hide()
        tvResumeGame.setOnClickListener {
            resumeTimer()
        }

        btTrue.setOnClickListener {
            viewModel.answerWord(true)
        }

        rootGame.setTransition(R.id.start, R.id.end)
        rootGame.transitionToEnd()
        rootGame.onTransitionCompletedOnce {
            tvGuideLabel.setText(R.string.skip)
            rootGame.setTransition(R.id.start, R.id.endSkip)
            rootGame.transitionToEnd()
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
        tvResumeGame.show()
        containerGame.setOnTouchListener(null)
        viewModel.pauseTimer()
        tvWord.hide()
        ibStopTime.setImageResource(R.drawable.ic_play_circle_outline_black_24dp)
        timerStop = true
    }

    private fun resumeTimer() {
        viewModel.startTimer()
        tvResumeGame.hide()
        tvWord.show()
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

    private fun onNextWord(word: Word) {
        tvWord.text = word.word

        Handler().postDelayed({
            if (frameWord != null) frameWord.show()
        }, 100)

        runWordAppearAnimation()
    }

    private fun onAnsweredCount(answered: Pair<Int, Int>) {
        tvAnsweredCount.text = answered.first.toString()
    }

    private fun runWordAppearAnimation() {
        val animator = ValueAnimator.ofFloat(0F, 1F)

        if (cardYPath == null) {
            cardYPath = calculatePath()
        }

        val yPath = cardYPath ?: 300

        animator.addUpdateListener { animation ->
            val animated = animation.animatedValue as Float
            with(frameWord) {
                scaleX = animated
                scaleY = animated
                translationY = yPath * (1F - animated)
            }
        }

        animator.duration = 300

        animator.start()
    }

    //Calculate path for a word card from top of a hat to center of the screen
    private fun calculatePath(): Int? {
        val hatTop = ivHat.top.takeIf { it != 0 } ?: return null
        val cardTop = frameWord.top.takeIf { it != 0 } ?: return null

        val additionalMargin: Float = 32.dpToPx.toFloat()

        return abs(hatTop - cardTop) + additionalMargin.toInt()
    }
}
