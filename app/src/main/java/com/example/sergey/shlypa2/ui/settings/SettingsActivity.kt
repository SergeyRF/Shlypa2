package com.example.sergey.shlypa2.ui.settings

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.artitk.licensefragment.model.License
import com.artitk.licensefragment.model.LicenseType
import com.artitk.licensefragment.support.v4.RecyclerViewLicenseFragment
import com.example.sergey.shlypa2.utils.Functions
import java.util.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Functions.setTheme(this)
        super.onCreate(savedInstanceState)

        if(supportFragmentManager.findFragmentById(android.R.id.content) == null) {
            val fragment = SettingsFragment()
            supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, fragment)
                    .commit()
        }
    }

    fun showContentLicenses() {
        val fragment = ContentLicensesFragment()
        supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, fragment)
                .addToBackStack(null)
                .commit()
    }

    fun showOpensourceLicenses() {
        val fragment = createLicenseFragment()
        supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, fragment)
                .addToBackStack(null)
                .commit()
    }

    fun createLicenseFragment() : Fragment {
        val fragment = RecyclerViewLicenseFragment.newInstance()
        val licenses = ArrayList<License>()
        licenses.add(License(this, "Support Library", LicenseType.APACHE_LICENSE_20, "2016", "Android Open Source Project"))
        licenses.add(License(this, "Firebase", LicenseType.APACHE_LICENSE_20, "2016", "Google Inc"))
        licenses.add(License(this, "Gson", LicenseType.APACHE_LICENSE_20, "2008", "Google Inc"))
        licenses.add(License(this, "Google PlayServices", LicenseType.APACHE_LICENSE_20, "2015", "Google Inc"))
        licenses.add(License(this, "Dagger 2", LicenseType.APACHE_LICENSE_20, "2012", "The Dagger Authors"))
        licenses.add(License(this, "Support v4", LicenseType.APACHE_LICENSE_20, "2014", "Android Open Source Project"))
        licenses.add(License(this, "CircleImageView", LicenseType.APACHE_LICENSE_20, "2014 - 2017", "Henning Dodenhof"))
        licenses.add(License(this, "Architecture Components", LicenseType.APACHE_LICENSE_20, "2015", "Android Open Sourse Project"))
        licenses.add(License(this, "Timber", LicenseType.APACHE_LICENSE_20, "2013", "Jake Wharton"))
        licenses.add(License(this, "Picasso", LicenseType.APACHE_LICENSE_20, "2013", "Square, Inc."))
        licenses.add(License(this, "Clans/FloatingActionButton", LicenseType.APACHE_LICENSE_20, "2015", "Dmytro Tarianyk"))
        licenses.add(License(this, "KotlinPleaseAnimate", LicenseType.APACHE_LICENSE_20, "2018", "florent37, Inc"))
        fragment.addCustomLicense(licenses)

        return fragment
    }
}
