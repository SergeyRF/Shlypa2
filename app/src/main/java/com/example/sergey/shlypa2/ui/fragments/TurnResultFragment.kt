package com.example.sergey.shlypa2.ui.fragments


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.ui.RoundActivity
import com.example.sergey.shlypa2.utils.gone
import com.example.sergey.shlypa2.utils.show
import com.example.sergey.shlypa2.viewModel.RoundViewModel


/**
 * A simple [Fragment] subclass.
 */
class TurnResultFragment : Fragment() {

    lateinit var viewModel: RoundViewModel
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(activity).get(RoundViewModel::class.java)
        // Inflate the layout for this fragment
        val root = inflater!!.inflate(R.layout.fragment_turn_result, container, false)

        recyclerView = root.findViewById(R.id.rvWordsTurnResult)

        val wordsList = viewModel.getTurnResults()

        if(wordsList.isNotEmpty()) {
            setRecycler(wordsList)
        } else {
            val textNoWords : TextView = root.findViewById(R.id.tvNoAnswers)

            textNoWords.show()
            recyclerView.gone()
        }

        //todo implement words right - wrong selection

        val finishBt : Button = root.findViewById(R.id.btFinishTurn)

        finishBt.setOnClickListener{ viewModel.nextTurn() }

        return root
    }

    private fun setRecycler(words : List<Word>) {
        val adapter = RvAdapter()
        adapter.altMode = true
        adapter.setData(words)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

}
