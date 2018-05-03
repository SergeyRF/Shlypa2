package com.example.sergey.shlypa2.ui.fragments


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.viewModel.WelcomeViewModel
import kotlinx.android.synthetic.main.fragment_welcome.*


/**
 * A simple [Fragment] subclass.
 */
class WelcomeFragment : Fragment() {

    lateinit var viewModel: WelcomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        viewModel = ViewModelProviders.of(activity!!).get(WelcomeViewModel::class.java)
        return inflater.inflate(R.layout.fragment_welcome, container, false)

    }

    override fun onStart() {
        super.onStart()
        btNewGame.setOnClickListener {
            viewModel.newGame()
        }

        btLoadGame.setOnClickListener {
            viewModel.savedGames()
        }

        btRules.setOnClickListener {
            viewModel.rules()
        }

    }

}
