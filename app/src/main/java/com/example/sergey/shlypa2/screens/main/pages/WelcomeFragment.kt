package com.example.sergey.shlypa2.screens.main.pages


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.screens.main.WelcomeViewModel
import kotlinx.android.synthetic.main.fragment_welcome.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


/**
 * A simple [Fragment] subclass.
 */
class WelcomeFragment : Fragment() {

    private val viewModel by sharedViewModel<WelcomeViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
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
