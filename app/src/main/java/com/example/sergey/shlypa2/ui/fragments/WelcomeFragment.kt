package com.example.sergey.shlypa2.ui.fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.ui.FirstActivity
import com.example.sergey.shlypa2.ui.PlayersActivity
import kotlinx.android.synthetic.main.fragment_welcome.*


/**
 * A simple [Fragment] subclass.
 */
class WelcomeFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onStart() {
        super.onStart()
        btNewGame.setOnClickListener {
            startActivity(Intent(context, PlayersActivity::class.java))
        }

        btLoadGame.setOnClickListener{
            (activity as FirstActivity).startGameLoadFragment()
        }
    }
}
