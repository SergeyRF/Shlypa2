package com.example.sergey.shlypa2.screens.settings

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.artitk.licensefragment.model.License
import com.artitk.licensefragment.model.LicenseType
import com.artitk.licensefragment.support.v4.RecyclerViewLicenseFragment
import com.example.sergey.shlypa2.extensions.setThemeApi21
import java.util.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setThemeApi21()
        super.onCreate(savedInstanceState)
        initToolbar()

        if (supportFragmentManager.findFragmentById(android.R.id.content) == null) {
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

    fun showDeletePlayers(){
        supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, DeletePlayerFragment())
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

    fun createLicenseFragment(): androidx.fragment.app.Fragment {
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

    private fun initToolbar() {
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
