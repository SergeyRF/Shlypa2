package com.example.sergey.shlypa2.ui.fragments


import android.animation.ValueAnimator
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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(activity!!).get(RoundViewModel::class.java)

        // Inflate the layout for this fragment
        val root = inflater!!.inflate(R.layout.fragment_game, container, false)

        tvTime = root.findViewById(R.id.tvTime)

        val btOk: Button = root.findViewById(R.id.btOk)
        val btReturn: Button = root.findViewById(R.id.btReturn)

        btOk.setOnClickListener { viewModel.answerWord(true) }
        btReturn.setOnClickListener { viewModel.answerWord(false) }

        viewModel.timerLiveData.observe(this, Observer { time -> tvTime.text = "$time" })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.wordLiveData.observe(this, Observer { word: Word? ->
            if (word != null) onNextWord(word)
        })


        onSwipeTouchListener.scrollListener = { x, y ->
            Timber.d("x dist $x y dist $y")
            cv_word.translationY = cv_word.translationY - y
        }

        containerGame.setOnClickListener { }
        containerGame.setOnTouchListener(onSwipeTouchListener)


    }

    fun onNextWord(word: Word) {
        Timber.d("on Next word")
        tv_word.text = word.word

        Handler().postDelayed({
            if (cv_word != null) cv_word.show()
        }, 100)

        runWordAppearAnimation()
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
        animator.addUpdateListener { animation ->
            val animated = animation.animatedValue as Float
            cv_word?.scaleX = animated
            cv_word?.scaleY = animated
            Timber.d("animated fraction $animated")
        }

        animator.duration = 300

        animator.start()
    }

    override fun onResume() {
        super.onResume()
        viewModel.startTimer()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseTimer()
    }
}
