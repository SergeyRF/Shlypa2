package com.example.sergey.shlypa2.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.sergey.shlypa2.Constants
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.utils.PreferenceHelper.get
import com.example.sergey.shlypa2.utils.PreferenceHelper.set
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by alex on 4/17/18.
 */

fun since(version: Int, block: () -> Unit) {
    if (Build.VERSION.SDK_INT >= version) {
        block.invoke()
    }
}

object Functions {

    fun timeToLocalDateWithTime(time: Long, context: Context): String {
        val date = Date(time)
        val dateFormat: DateFormat = android.text.format.DateFormat.getDateFormat(context)
        val dateString = dateFormat.format(date)

        val timeFormat = android.text.format.DateFormat.getTimeFormat(context)
        val timeString = timeFormat.format(time)

        return "$dateString $timeString"
    }

    fun getGameId(context: Context): Int {
        var id = PreferenceHelper.defaultPrefs(context)["Game id", 0] ?: 0
        PreferenceHelper.defaultPrefs(context)["Game id"] = ++id

        Timber.d("Game id is $id")
        return id
    }

    fun hideKeyboard(context: Context, focusableView: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromInputMethod(focusableView.windowToken, 0)
    }

    fun showKeyboard(context: Context, focusableView: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(focusableView, InputMethodManager.SHOW_IMPLICIT)
    }

    fun imageNameToUrl(name: String): String {
        return "file:///android_asset/$name"
    }

    fun imageCustomNameToUrl(name: String): String {
        return "file:///android_asset/$name"
    }

    fun readJsonFromAssets(context: Context, filePath: String): String {
        var result = "[]"

        try {
            val inputStream = context.assets.open(filePath)
            val size = inputStream.available()
            val buffer = ByteArray(size)

            inputStream.read(buffer)
            inputStream.close()
            result = String(buffer)
        } catch (ex: IOException) {
            Timber.e(ex)
        }

        return result
    }

    fun getSelectedThemeId(context: Context): Int {
        val preferences = PreferenceHelper.defaultPrefs(context)
        return preferences[Constants.THEME_PREF] ?: com.example.sergey.shlypa2.R.style.AppTheme
    }

    fun getScreenWidth(context: Context): Int {
        val display = context.resources.displayMetrics
        return display.widthPixels
    }

    fun dpToPx(context: Context, dp: Float): Float {
        val r = context.getResources()
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics())
    }

    fun getThemedBgColor(context: Context): Int {
        val preferences = PreferenceHelper.defaultPrefs(context)
        val themeRes: Int = preferences[Constants.THEME_PREF]
                ?: com.example.sergey.shlypa2.R.style.AppTheme
        val colorRes = when (themeRes) {
            R.style.AppThemeBlue -> R.color.BlueAvatarBg
            R.style.AppThemeCyan -> R.color.CyanAvatarBg
            R.style.AppThemeGreen -> R.color.GreenAvatarBg
            R.style.AppThemeIndigo -> R.color.IndigoAvatarBg
            R.style.AppThemeYellow -> R.color.YellowAvatarBg
            R.style.AppThemePurple -> R.color.PurpleAvatarBg
            else -> R.color.BlueAvatarBg
        }

        return ContextCompat.getColor(context, colorRes)
    }

    private val fileStamp = SimpleDateFormat("yyyyMMdd_HHmmss")
    private const val JPEG_FILE_SUFFIX = ".jpg"
    private const val JPEG_FILE_PREFIX = "IMG_"
    private const val ALBUM_NAME = "HAT"

    fun getAlbumDirectory(): File? {
        var file: File? = null
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    ALBUM_NAME)
            if (!file.mkdirs()) {
                if (!file.exists()) {
                    return null
                }
            }
        }
        return file
    }

    @Throws(IOException::class)
    fun createImageFile(): File {
        val timeStamp = fileStamp.format(Date())
        val imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        val albumF = getAlbumDirectory()
        return File.createTempFile(
                imageFileName, /* prefix */
                JPEG_FILE_SUFFIX, /* suffix */
                albumF      /* directory */
        );
    }

    fun createImageUri(context: Context): Uri?{
        val timeStamp = fileStamp.format(Date())
        val imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, imageFileName+JPEG_FILE_SUFFIX)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/PerracoLabs")
        }

        return resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }
}