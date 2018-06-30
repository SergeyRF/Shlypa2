package com.example.sergey.shlypa2

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.support.v7.preference.PreferenceManager
import android.widget.Button
import android.widget.ImageView


/**
 * Created by sergey on 5/1/18.
 */
class AppRater {

    fun app_launched(mContext: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(mContext)
        if (prefs.getBoolean(Constants.DONT_SHOW_RATE_DIALOG, false)) {
            return
        }

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
                    showRateDialogMy(mContext)
                }
            }
        }

        editor.apply()
    }

    fun showRateDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()

        builder.setTitle(R.string.rate)
        builder.setMessage(R.string.rate_massage)

        builder.setNeutralButton(R.string.rate_last) { dialog, which ->
            editor.putLong(Constants.LAST_RATE_SHOW_TIME, System.currentTimeMillis()).apply()
            dialog.dismiss()
        }

        builder.setNegativeButton(R.string.rate_never) { dialog, which ->
            editor.putBoolean(Constants.DONT_SHOW_RATE_DIALOG, true).apply()
            dialog.dismiss()
        }

        builder.setPositiveButton(R.string.rate_ok) { dialog, which ->
            editor.putBoolean(Constants.DONT_SHOW_RATE_DIALOG, true)
            // rateApp(context)
            dialog.dismiss()
        }

        builder.setOnCancelListener {
            editor.putLong(Constants.LAST_RATE_SHOW_TIME, System.currentTimeMillis()).apply()
        }


        builder.show()

    }

    fun showRateDialogMy(context: Context) {

        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_rater)

        val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()

        val bt_positive = dialog.findViewById<Button>(R.id.bt_positive)
        val bt_neutral = dialog.findViewById<Button>(R.id.bt_neutral)
        val bt_negative = dialog.findViewById<Button>(R.id.bt_negative)

        val star_1 = dialog.findViewById<ImageView>(R.id.star_1)
        val star_2 = dialog.findViewById<ImageView>(R.id.star_2)
        val star_3 = dialog.findViewById<ImageView>(R.id.star_3)
        val star_4 = dialog.findViewById<ImageView>(R.id.star_4)
        val star_5 = dialog.findViewById<ImageView>(R.id.star_5)

        val listStars = mutableListOf<ImageView>()
        listStars.add(star_1)
        listStars.add(star_2)
        listStars.add(star_3)
        listStars.add(star_4)
        listStars.add(star_5)

        fun lightStars(i: Int) {

            for (n in 0..(listStars.size - 1)) {
                listStars[n].setImageResource(R.drawable.ic_star)
            }
            for (n in (i)..(listStars.size - 1)) {
                listStars[n].setImageResource(R.drawable.ic_star_grey)
            }
        }

        bt_positive.setOnClickListener {
            editor.putBoolean(Constants.DONT_SHOW_RATE_DIALOG, true)
            // rateApp(context)
            dialog.dismiss()
        }
        bt_negative.setOnClickListener {
            editor.putBoolean(Constants.DONT_SHOW_RATE_DIALOG, true).apply()
            dialog.dismiss()
        }

        bt_neutral.setOnClickListener {
            editor.putLong(Constants.LAST_RATE_SHOW_TIME, System.currentTimeMillis()).apply()
            dialog.dismiss()
        }
        dialog.setOnCancelListener {
            editor.putLong(Constants.LAST_RATE_SHOW_TIME, System.currentTimeMillis()).apply()
        }

        star_1.setOnClickListener { lightStars(1) }
        star_2.setOnClickListener { lightStars(2) }
        star_3.setOnClickListener { lightStars(3) }
        star_4.setOnClickListener { lightStars(4) }
        star_5.setOnClickListener { lightStars(5) }

        dialog.show()
    }

    fun rateApp(context: Context) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("")))
        } catch (ex: ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("")))
        }

    }
}