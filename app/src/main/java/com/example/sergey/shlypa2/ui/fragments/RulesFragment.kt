package com.example.sergey.shlypa2.ui.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sergey.shlypa2.R
import kotlinx.android.synthetic.main.fragment_rules.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class RulesFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rules, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val locale : String = Locale.getDefault().language.toLowerCase()
        when(locale){
            "ru"->web_rules.loadUrl("file:///android_asset/rules.html")
            else->web_rules.loadUrl("file:///android_asset/rulesen.html")
        }


    }

}// Required empty public constructor
