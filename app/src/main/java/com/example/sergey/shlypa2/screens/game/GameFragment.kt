package com.example.sergey.shlypa2.screens.game


import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sergey.shlypa2.OnSwipeTouchListener
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.extensions.*
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onSwipeTouchListener.scrollListener = { x, y ->
            frameWord.translationY = frameWord.translationY - y
        }

        viewModel.wordLiveData.observeSafe(this) { onNextWord(it) }
        viewModel.timerLiveData.observeSafe(this) { onTimer(it) }
        viewModel.answeredCountLiveData.observeSafe(this) { onAnsweredCount(it) }
        viewModel.timerStateLiveData.observeSafe(this) {
            if (it) onTimerResumed() else onTimerPaused()
        }

        containerGame.setOnClickListener { }
        containerGame.setOnTouchListener(onSwipeTouchListener)

        timerLinear.setOnClickListener { viewModel.toggleTimer() }

        tvResumeGame.setOnClickListener {
            viewModel.toggleTimer()
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
        viewModel.onGameResumed()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onGamePaused()
    }

    private fun onTimerPaused() {
        tvResumeGame.show()
        containerGame.setOnTouchListener(null)
        tvWord.hide()
        ibStopTime.setImageResource(R.drawable.ic_play_circle_outline_black_24dp)
    }

    private fun onTimerResumed() {
        tvResumeGame.hide()
        tvWord.show()
        containerGame.setOnTouchListener(onSwipeTouchListener)
        ibStopTime.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp)
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

            frameWord?.scaleX = animated
            frameWord?.scaleY = animated
            frameWord?.translationY = yPath * (1F - animated)
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
