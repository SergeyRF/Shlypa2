package com.example.sergey.shlypa2.ui.fragments


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.utils.PrecaheLayoutManager
import com.example.sergey.shlypa2.utils.gone
import com.example.sergey.shlypa2.utils.show
import com.example.sergey.shlypa2.viewModel.RoundViewModel


/**
 * A simple [Fragment] subclass.
 */
class TurnResultFragment : androidx.fragment.app.Fragment() {

    lateinit var viewModel: RoundViewModel
    lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(activity!!).get(RoundViewModel::class.java)
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_turn_result, container, false)

        recyclerView = root.findViewById(R.id.rvWordsTurnResult)

        val textNoWords: TextView = root.findViewById(R.id.tvNoAnswers)
        viewModel.roundLiveData.observe(this, Observer { round ->
            round?.let {
                if (it.wordsAnsweredByPlayer.isNotEmpty()) {
                    setRecycler(it.wordsAnsweredByPlayer)
                } else {
                    textNoWords.show()
                    recyclerView.gone()
                }
            }
        })

        //todo implement words right - wrong selection

        val finishBt: Button = root.findViewById(R.id.btFinishTurn)

        finishBt.setOnClickListener { viewModel.nextTurn() }

        return root
    }

    private fun setRecycler(words: List<Word>) {
        val adapter = RvAdapter()
        adapter.altMode = true
        adapter.setData(words)

        recyclerView.layoutManager = PrecaheLayoutManager(context)
        recyclerView.adapter = adapter
    }

}
