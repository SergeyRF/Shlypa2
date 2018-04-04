package com.example.sergey.shlypa2.ui.fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.ui.RoundActivity
import com.example.sergey.shlypa2.viewModel.RoundViewModel
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 */
class GameFragment : Fragment() {

    lateinit var tvWord : TextView
    lateinit var viewModel: RoundViewModel
    lateinit var tvTime : TextView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(activity).get(RoundViewModel::class.java)

        // Inflate the layout for this fragment
        val root = inflater!!.inflate(R.layout.fragment_game, container, false)

        tvWord = root.findViewById(R.id.tvWord)
        tvTime = root.findViewById(R.id.tvTime)

        val btOk : Button = root.findViewById(R.id.btOk)
        val btReturn : Button = root.findViewById(R.id.btReturn)

        btOk.setOnClickListener{viewModel.answerWord(true)}
        btReturn.setOnClickListener{viewModel.answerWord(false)}

        viewModel.wordLiveData.observe(this, Observer{word ->
            if (word != null) tvWord.text = word.word})

        viewModel.finishTurnCall.observe(this, Observer { if (it != null && it) finishTurn()})

        viewModel.timerLiveData.observe(this, Observer { time -> tvTime.text = "$time" })

        return root
    }

    fun finishTurn() {
        Timber.d("finish turn")
        (activity as RoundActivity).startTurnFinishFragment()
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
