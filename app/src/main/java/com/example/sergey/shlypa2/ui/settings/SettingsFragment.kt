package com.example.sergey.shlypa2.ui.settings


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import com.example.sergey.shlypa2.Constants
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.utils.PreferenceHelper
import com.example.sergey.shlypa2.utils.PreferenceHelper.get
import com.example.sergey.shlypa2.utils.PreferenceHelper.set
import kotlinx.android.synthetic.main.fragment_settings.*


/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Sound setting
        val preferences = PreferenceHelper.defaultPrefs(context!!)
        val soundEnabled: Boolean = preferences[Constants.SOUND_ON_PREF] ?: true

        ssPrefSound.setChecked(soundEnabled)

        ssPrefSound.setOnCheckedListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
             preferences[Constants.SOUND_ON_PREF] = isChecked
        })

        //Theme setting
        val themeIds = arrayOf(R.style.AppThemeBlue, R.style.AppThemeCyan, R.style.AppThemeGreen,
                R.style.AppTheme)

        val themeNames = arrayOf(R.string.theme_blue, R.string.theme_cyan,
                R.string.theme_green, R.string.theme_teal)
                .map { context!!.getString(it) }

        val adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1,
               themeNames )

        spinnerThemes.adapter = adapter

        val currentThemeId = Functions.getSelectedThemeId(context!!)
        spinnerThemes.setSelection(themeIds.indexOf(currentThemeId))

        spinnerThemes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Functions.selectTheme(themeIds[position], activity!!)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


        //Content
        viewContentBack.setOnClickListener {
            activity?.let { (it as SettingsActivity).showContentLicenses() }
        }

        //Opensourse
        viewOpensourseBack.setOnClickListener {
            activity?.let { (it as SettingsActivity).showOpensourceLicenses() }
        }
    }
}// Required empty public constructor