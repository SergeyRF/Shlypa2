package com.example.sergey.shlypa2.screens.main.pages


import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sergey.shlypa2.R
import kotlinx.android.synthetic.main.fragment_rules.*
import java.util.*


class RulesFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
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

        val rulesStream = requireContext().assets!!.open(fileName)
        val rulesText = String(rulesStream.readBytes())
        rulesStream.close()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvRules.text = Html.fromHtml(rulesText, Html.FROM_HTML_MODE_COMPACT)
        } else {
            tvRules.text = Html.fromHtml(rulesText)
        }
    }

}
