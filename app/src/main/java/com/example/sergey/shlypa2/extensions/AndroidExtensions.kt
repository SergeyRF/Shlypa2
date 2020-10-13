package com.example.sergey.shlypa2.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.sergey.shlypa2.BuildConfig
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.utils.Functions.createImageUri
import timber.log.Timber

fun Context.runOnceEver(prefKey: String, block: () -> Unit) {
    val preference = PreferenceManager.getDefaultSharedPreferences(this)
    if (!preference.getBoolean(prefKey, false)) {
        block.invoke()
        preference.edit()
                .putBoolean(prefKey, true)
                .apply()
    }
}

fun AppCompatActivity.dismissDialogFragment(tag: String) {
    (supportFragmentManager
            .findFragmentByTag(tag) as? DialogFragment)
            ?.dismissAllowingStateLoss()
}

fun AppCompatActivity.isPermissionsGranted(vararg permissions: String): Boolean {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        true
    } else {
        permissions.forEach {
            if (checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED) return false
        }
        true
    }
}

fun Fragment.isPermissionsGranted(permission: String): Boolean {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        true
    } else {
        if (activity != null) {
            ActivityCompat.checkSelfPermission(activity as Context, permission) ==
                    PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
    }
}

fun AppCompatActivity.openGalleryIntent(
        selectRequestCode: Int,
        title: String = getString(R.string.choose_photo)) {
    val intent = Intent().apply {
        type = "image/*"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        action = Intent.ACTION_GET_CONTENT
    }
    startActivityForResult(Intent.createChooser(intent, title), selectRequestCode)
}

@SuppressLint("QueryPermissionsNeeded")
fun AppCompatActivity.photoFromCamera(requestCode: Int): Uri? {

    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    var imageUri: Uri? = null

        if (takePictureIntent.resolveActivity(packageManager) != null) {
            runCatching {
                val providerUri  =
                        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.Q) {
                            val file = Functions.createImageFile()
                            imageUri = Uri.fromFile(file)
                            FileProvider.getUriForFile(this,
                                    "${BuildConfig.APPLICATION_ID}.file_provider",
                                    file)
                        }else{
                            imageUri= createImageUri(this)
                            imageUri
                        }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerUri)
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                    takePictureIntent.clipData = ClipData.newRawUri("", providerUri)
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivityForResult(takePictureIntent, requestCode)
            }.onFailure {
                Timber.e(it)
            }
        }
    return imageUri
}

inline fun <reified T : Any> Activity.extra(key: String, default: T? = null) = lazy {
    val value = intent?.extras?.get(key)
    if (value is T) value else default
}

inline fun <reified T : Any> Activity.extraNotNull(key: String, default: T? = null) = lazy {
    val value = intent?.extras?.get(key)
    requireNotNull(if (value is T) value else default) { key }
}

inline fun <reified T : Any> Fragment.extra(key: String, default: T? = null) = lazy {
    val value = arguments?.get(key)
    if (value is T) value else default
}

inline fun <reified T : Any> Fragment.extraNotNull(key: String, default: T? = null) = lazy {
    val value = arguments?.get(key)
    requireNotNull(if (value is T) value else default) { key }
}