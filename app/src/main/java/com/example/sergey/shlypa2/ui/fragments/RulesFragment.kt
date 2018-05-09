package com.example.sergey.shlypa2.ui.fragments


import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sergey.shlypa2.BuildConfig
import com.example.sergey.shlypa2.R
import kotlinx.android.synthetic.main.fragment_rules.*
import java.io.BufferedReader
import java.io.InputStreamReader
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

    override fun onResume() {
        super.onResume()
        loadRules()
    }

    fun loadRules() {
        val locale: String = Locale.getDefault().language.toLowerCase()
        val fileName =
                when (locale) {
                    "ru" -> "rules.html"
                    else -> "rulesen.html"
                }

        val rulesStream = context!!.assets!!.open(fileName)
        val bufferedReader = BufferedReader(InputStreamReader(rulesStream))

        val stringBuilder = StringBuilder()
        var line: String? = bufferedReader.readLine()

        while (line != null) {
            stringBuilder.append(line)
            line = bufferedReader.readLine()
        }

        val html = stringBuilder.toString()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvRules.text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
        } else {
            tvRules.text = Html.fromHtml(html)
        }
    }

}// Required empty public constructor
