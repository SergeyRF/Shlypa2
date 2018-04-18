package com.example.sergey.shlypa2.ui.fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.sergey.shlypa2.OnSwipeTouchListener

import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.ui.RoundActivity
import com.example.sergey.shlypa2.utils.hide
import com.example.sergey.shlypa2.viewModel.RoundViewModel
import kotlinx.android.synthetic.main.fragment_game.*
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 */
class GameFragment : Fragment() {


    lateinit var viewModel: RoundViewModel
    lateinit var tvTime : TextView


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(activity).get(RoundViewModel::class.java)

        // Inflate the layout for this fragment
        val root = inflater!!.inflate(R.layout.fragment_game, container, false)

        tvTime = root.findViewById(R.id.tvTime)



        val btOk : Button = root.findViewById(R.id.btOk)
        val btReturn : Button = root.findViewById(R.id.btReturn)

        btOk.hide()
        btReturn.hide()

        btOk.setOnClickListener{viewModel.answerWord(true)}
        btReturn.setOnClickListener{viewModel.answerWord(false)}

        viewModel.timerLiveData.observe(this, Observer { time -> tvTime.text = "$time" })

        return root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cv_word.setOnClickListener { }

        viewModel.wordLiveData.observe(this, Observer{word : Word? ->
            if (word != null) tv_word.text = word.word})

        cv_word.setOnTouchListener(object : OnSwipeTouchListener() {
            override fun onSwipeLeft(): Boolean {
                viewModel.answerWord(false)
                return true
            }

            override fun onSwipeRight(): Boolean {
                viewModel.answerWord(true)
                return true
            }
        })
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
