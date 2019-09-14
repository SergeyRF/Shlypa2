package com.example.sergey.shlypa2.screens.settings


import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sergey.shlypa2.Constants
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.ads.ConsentManager
import com.example.sergey.shlypa2.extensions.hide
import com.example.sergey.shlypa2.extensions.selectTheme
import com.example.sergey.shlypa2.extensions.show
import com.example.sergey.shlypa2.screens.premium.BuyPremiumActivity
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.utils.PreferenceHelper
import com.example.sergey.shlypa2.utils.PreferenceHelper.get
import com.example.sergey.shlypa2.utils.PreferenceHelper.set
import com.example.sergey.shlypa2.utils.since
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.android.ext.android.inject


/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {

    companion object {
        private const val REQUEST_PREMIUM = 1022
    }

    private val consentManager by inject<ConsentManager>()

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

        ssPrefSound.setOnCheckedListener{isChecked ->
            preferences[Constants.SOUND_ON_PREF] = isChecked
        }

        since(Build.VERSION_CODES.LOLLIPOP) {
            //Theme setting
            val themeIds = arrayOf(R.style.AppThemeBlue, R.style.AppThemeCyan, R.style.AppThemeGreen,
                    R.style.AppTheme)

            val themeNames = arrayOf(R.string.theme_blue, R.string.theme_cyan,
                    R.string.theme_green, R.string.theme_teal)
                    .map { context!!.getString(it) }

            val adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1,
                    themeNames)

            spinnerThemes.adapter = adapter

            val currentThemeId = Functions.getSelectedThemeId(context!!)
            spinnerThemes.setSelection(themeIds.indexOf(currentThemeId))

            spinnerThemes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    requireActivity().selectTheme(themeIds[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            groupThemes.show()
        }


        //Content
        viewContentBack.setOnClickListener {
            activity?.let { (it as SettingsActivity).showContentLicenses() }
        }

        //Opensourse
        viewOpensourseBack.setOnClickListener {
            activity?.let { (it as SettingsActivity).showOpensourceLicenses() }
        }

        viewPrivacyBack.setOnClickListener {
            openWebPage("https://hatproject-2535f.firebaseapp.com/hat_en.html")
        }

        viewDeletePlayers.setOnClickListener {
            activity?.let { (it as SettingsActivity).showDeletePlayers() }
        }

        if(consentManager.consentRequired()) {
            tvConsent.show()
            tvConsent.setOnClickListener {
                consentManager.forceShowConsent(requireContext()) {
                    startActivityForResult(Intent(requireContext(), BuyPremiumActivity::class.java), REQUEST_PREMIUM)
                }
            }
        } else {
            tvConsent.hide()
        }
    }

    fun openWebPage(url: String) {
        try {
            val webpage = Uri.parse(url)
            val myIntent = Intent(Intent.ACTION_VIEW, webpage)
            startActivity(myIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No application can handle this request. Please install a web browser.", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
           REQUEST_PREMIUM -> {
               consentManager.setPremium(resultCode == Activity.RESULT_OK)
           }
        }
    }
}// Required empty public constructor
