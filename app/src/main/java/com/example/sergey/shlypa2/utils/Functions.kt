package com.example.sergey.shlypa2.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.text.DateFormat
import java.util.*
import com.example.sergey.shlypa2.utils.PreferenceHelper.set
import com.example.sergey.shlypa2.utils.PreferenceHelper.get
import org.json.JSONArray
import timber.log.Timber
import java.io.IOException

/**
 * Created by alex on 4/17/18.
 */
object Functions {

    fun timeToLocalDate(time: Long, context: Context): String {
        val date = Date(time)
        val dateFormat: DateFormat = android.text.format.DateFormat.getDateFormat(context)
        return dateFormat.format(date)
    }

    fun getGameId(context: Context) : Int {
        var id = PreferenceHelper.defaultPrefs(context)["Game id", 0] ?: 0
        PreferenceHelper.defaultPrefs(context)["Game id"] = ++id

        Timber.d("Game id is $id")
        return  id
    }

    fun hideKeyboard(context: Context, focusableView : View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromInputMethod(focusableView.windowToken, 0)
    }

    fun showKeyboard(context: Context, focusableView: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(focusableView, InputMethodManager.SHOW_IMPLICIT)
    }

    fun imageNameToUrl(name : String) : String {
        return "file:///android_asset/player_avatars/$name"
    }

    fun readJsonFromAssets(context: Context, filePath: String) : String {
        var result = "[]"

        try {
            val inputStream = context.assets.open(filePath)
            val size = inputStream.available()
            val buffer = ByteArray(size)

            inputStream.read(buffer)
            inputStream.close()
            result = String(buffer)
        } catch (ex : IOException) {
            Timber.e(ex)
        }

        return result
    }

}