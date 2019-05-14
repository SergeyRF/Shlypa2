package com.example.sergey.shlypa2.screens.game


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Word
import com.example.sergey.shlypa2.extensions.gone
import com.example.sergey.shlypa2.extensions.observeSafe
import com.example.sergey.shlypa2.extensions.show
import com.example.sergey.shlypa2.screens.game.adapter.ItemWordCheck
import com.example.sergey.shlypa2.utils.PrecaheLayoutManager
import eu.davidea.flexibleadapter.FlexibleAdapter
import kotlinx.android.synthetic.main.fragment_turn_result.*


class TurnResultFragment : androidx.fragment.app.Fragment() {

    lateinit var viewModel: RoundViewModel
    private val wordsAdapter = FlexibleAdapter(emptyList())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(requireActivity()).get(RoundViewModel::class.java)
        return inflater.inflate(R.layout.fragment_turn_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        viewModel.roundLiveData.observeSafe(this) {
            if (it.wordsAnsweredByPlayer.isNotEmpty()) {
                onWords(it.wordsAnsweredByPlayer)
            } else {
                showNoAnswers()
            }
        }
    }

    private fun initViews() {
        rvWordsTurnResult.layoutManager = PrecaheLayoutManager(requireContext())
        rvWordsTurnResult.adapter = wordsAdapter
        btFinishTurn.setOnClickListener { onFinishClicked() }
    }

    private fun onFinishClicked() {
        wordsAdapter.currentItems
                .filter { it is ItemWordCheck && it.checked }
                .map { (it as ItemWordCheck).word.id }
                .apply { viewModel.nextTurn(this) }
    }

    private fun showNoAnswers() {
        rvWordsTurnResult.gone()
        tvNoAnswers.show()
    }

    private fun onWords(words: List<Word>) {
        rvWordsTurnResult.show()
        tvNoAnswers.gone()

        words.map { word ->
            ItemWordCheck(word, word.right)
        }.apply {
            wordsAdapter.updateDataSet(this)
        }
    }

}
