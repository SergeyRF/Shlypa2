package com.example.sergey.shlypa2.ui.fragments


import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.sergey.shlypa2.OnSwipeTouchListener
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.utils.hide
import com.example.sergey.shlypa2.utils.show
import com.example.sergey.shlypa2.viewModel.RoundViewModel
import kotlinx.android.synthetic.main.fragment_game.*
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 */
class GameFragment : Fragment() {


    lateinit var viewModel: RoundViewModel
    lateinit var tvTime: TextView
    var timerStop:Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(activity!!).get(RoundViewModel::class.java)

        // Inflate the layout for this fragment
        val root = inflater!!.inflate(R.layout.fragment_game, container, false)

        tvTime = root.findViewById(R.id.tvTime)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.wordLiveData.observe(this, Observer { word: Word? ->
            if (word != null) onNextWord(word)
        })


        onSwipeTouchListener.scrollListener = { x, y ->
            cv_word.translationY = cv_word.translationY - y
        }

        viewModel.timerLiveData.observe(this, Observer { time -> time?.let { onTimer(it) }})

        viewModel.answeredCountLiveData.observe(this, Observer { answered ->
            answered?.let { onAnsweredCount(it) }
        })

        containerGame.setOnClickListener { }
        containerGame.setOnTouchListener(onSwipeTouchListener)



        tv_PlayGame.hide()
        timerLinear.setOnClickListener {
            timerStop = if (!timerStop) {
                tv_PlayGame.show()
                containerGame.setOnTouchListener(null)
                viewModel.pauseTimer()
                tv_word.hide()
                ibStopTime.setImageResource(R.drawable.ic_play_circle_outline_black_24dp)
                true
            } else{
                viewModel.startTimer()
                tv_PlayGame.hide()
                tv_word.show()
                containerGame.setOnTouchListener(onSwipeTouchListener)
                ibStopTime.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp)
                false
            }
        }

        tv_PlayGame.setOnClickListener {
            viewModel.startTimer()
            tv_PlayGame.hide()
            tv_word.show()
            containerGame.setOnTouchListener(onSwipeTouchListener)
            ibStopTime.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp)
            timerStop = false
        }

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

    fun onAnsweredCount(answered : Pair<Int, Int>) {
        tvAnsweredCount.text = answered.first.toString()
    }

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

    fun runWordAppearAnimation() {
        val animator = ValueAnimator.ofFloat(0F, 1F)
        val yPath = 300
        animator.addUpdateListener { animation ->
            val animated = animation.animatedValue as Float
            cv_word?.scaleX = animated
            cv_word?.scaleY = animated
            cv_word?.translationY = yPath * (1F - animated)
        }

        animator.duration = 300

        animator.start()
    }

    override fun onResume() {
        super.onResume()
        if(!timerStop) {
            viewModel.startTimer()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseTimer()
    }
}
