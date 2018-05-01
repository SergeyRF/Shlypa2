package com.example.sergey.shlypa2

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.support.v7.preference.PreferenceManager
import timber.log.Timber


/**
 * Created by sergey on 5/1/18.
 */
class AppRater{

    fun app_launched(mContext: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(mContext)
        if (prefs.getBoolean(Constants.DONT_SHOW_RATE_DIALOG, false)) {
            return
        }
        Timber.d("game start FFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")
        val editor = prefs.edit()

        // Increment launch counter
        val launch_count = prefs.getLong(Constants.LAUNCH_COUNT, 0) + 1
        editor.putLong(Constants.LAUNCH_COUNT, launch_count)

        // Get date of first launch
        var date_firstLaunch = prefs.getLong(Constants.DATE_FIRST_LAUNCH, 0)
        if (date_firstLaunch == 0L) {
            date_firstLaunch = System.currentTimeMillis()
            editor.putLong(Constants.DATE_FIRST_LAUNCH, date_firstLaunch)
        }

        val rateLastRemind = prefs.getLong(Constants.LAST_RATE_SHOW_TIME, 0)

        // Wait at least n days before opening
        if (launch_count >= Constants.LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch + Constants.DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000) {
                if (System.currentTimeMillis() >= rateLastRemind + Constants.DAYS_UNTIL_REMIND * 24 * 60 * 60 * 1000) {
                    showRateDialog(mContext)
                }
            }
        }

        editor.apply()
    }

    fun showRateDialog(context:Context){
        val builder = AlertDialog.Builder(context)
        val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()

        builder.setTitle(R.string.rate)
        builder.setMessage(R.string.rate_massage)

        builder.setNeutralButton(R.string.rate_last) { dialog, which ->
            editor.putLong(Constants.LAST_RATE_SHOW_TIME, System.currentTimeMillis()).apply()
            dialog.dismiss()
        }

        builder.setNegativeButton(R.string.rate_never){dialog, which ->
            editor.putBoolean(Constants.DONT_SHOW_RATE_DIALOG, true).apply()
            dialog.dismiss()
        }

        builder.setPositiveButton(R.string.rate_ok){dialog, which ->
            editor.putBoolean(Constants.DONT_SHOW_RATE_DIALOG, true)
           // rateApp(context)
            dialog.dismiss()
        }

        builder.setOnCancelListener {
            editor.putLong(Constants.LAST_RATE_SHOW_TIME, System.currentTimeMillis()).apply()
        }


        builder.show()

    }

    fun rateApp(context: Context) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("")))
        } catch (ex: ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("")))
        }

    }
}