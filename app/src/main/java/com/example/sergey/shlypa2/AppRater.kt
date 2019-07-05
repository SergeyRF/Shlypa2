package com.example.sergey.shlypa2

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceManager
import android.widget.Button
import android.widget.ImageView
import com.example.sergey.shlypa2.utils.PreferenceHelper
import com.example.sergey.shlypa2.utils.anal.AnalSender
import org.koin.core.KoinComponent
import org.koin.core.inject


class AppRater: KoinComponent {

    companion object {
        const val APP_NAME = "com.attiladroid.shlypa"
        private const val RATED = "rated"
        private const val SHOW_LATER_SELECTED = "show_later_selected"
        private const val LAST_SHOWN_TIME = "last_shown_time"
        private const val DAYS_UNTIL_SHOW = 3
    }

    private val anal by inject<AnalSender>()
    private var lightedStars = 0

    fun rateAppIfRequired(mContext: Context) {
        val preferenceHelper = PreferenceHelper.defaultPrefs(mContext)
        if(preferenceHelper.getBoolean(RATED, false)) return
        if(preferenceHelper.getBoolean(SHOW_LATER_SELECTED, false)) {
            val lastShown = preferenceHelper.getLong(LAST_SHOWN_TIME, Long.MAX_VALUE)
            if(System.currentTimeMillis() < lastShown +  DAYS_UNTIL_SHOW * 24 * 60 * 60 * 1000) return
        }

        showRateDialogMy(mContext)
    }

    private fun showRateDialogMy(context: Context) {
        anal.rateDialogOpened()

        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_rater)



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
            lightedStars = i
            for (n in 0..(listStars.size - 1)) {
                listStars[n].setImageResource(R.drawable.ic_star)
            }
            for (n in (i)..(listStars.size - 1)) {
                listStars[n].setImageResource(R.drawable.ic_star_grey)
            }
        }

        val prefs = PreferenceHelper.defaultPrefs(context)

        bt_positive.setOnClickListener {
            anal.rateStarred(lightedStars)
            prefs.edit().putBoolean(RATED, true).apply()
            rateApp(context)
            dialog.dismiss()
        }

        bt_negative.setOnClickListener {
            anal.rateDialogNeverClicked()
            prefs.edit().putBoolean(RATED, true).apply()
            dialog.dismiss()
        }

        bt_neutral.setOnClickListener {
            prefs.edit().putLong(LAST_SHOWN_TIME, System.currentTimeMillis()).apply()
            prefs.edit().putBoolean(SHOW_LATER_SELECTED, true).apply()
            dialog.dismiss()
        }

        dialog.setOnCancelListener {
            anal.rateDialogCanceled()
            prefs.edit().putLong(LAST_SHOWN_TIME, System.currentTimeMillis()).apply()
            prefs.edit().putBoolean(SHOW_LATER_SELECTED, true).apply()
        }

        star_1.setOnClickListener { lightStars(1) }
        star_2.setOnClickListener { lightStars(2) }
        star_3.setOnClickListener { lightStars(3) }
        star_4.setOnClickListener {
            lightStars(4)
            rateApp(context)
            dialog.dismiss()
        }
        star_5.setOnClickListener {
            lightStars(5)
            rateApp(context)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun rateApp(context: Context) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$APP_NAME")))
        } catch (ex: ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=$APP_NAME")))
        }
    }
}