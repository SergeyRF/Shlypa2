package com.example.sergey.shlypa2.screens.settings


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

import com.example.sergey.shlypa2.R


/**
 * A simple [Fragment] subclass.
 */
class ContentLicensesFragment : PreferenceFragmentCompat() {


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.empty_preferences, rootKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val titlesArray = resources.getStringArray(R.array.content_license_title)
        val summaryArray = resources.getStringArray(R.array.content_license_summary)
        val linksArray = resources.getStringArray(R.array.content_license_link)

        for(i in 0 until titlesArray.size) {
            val preference = Preference(context)
            preference.title = titlesArray[i]
            preference.summary = summaryArray[i]
            preference.intent = Intent(Intent.ACTION_VIEW, Uri.parse(linksArray[i]))

            preferenceScreen.addPreference(preference)
        }
    }


}// Required empty public constructor
