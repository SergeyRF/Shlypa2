package com.example.sergey.shlypa2

import android.content.Context
import android.net.Uri
import java.io.File

object ImagesHelper {

    const val AVATAR_RELATIVE_PATH = "/avatars/players"
    private const val CUSTOM_AVATAR_PREFIX = "xt45xz"

    private fun isCustomAvatar(avatar: String) = avatar.startsWith(CUSTOM_AVATAR_PREFIX)

    fun smallImagePathPlayer(image: String, context: Context): String {
        return if (isCustomAvatar(image)) imageCustomNameToUrl("$AVATAR_RELATIVE_PATH/$image", context)
        else imageNameToUrl("player_avatars/small/$image")
    }

    fun largeImagePathPlayer(image: String, context: Context): String {
        return if (isCustomAvatar(image)) imageCustomNameToUrl("$AVATAR_RELATIVE_PATH/$image", context)
        else imageNameToUrl("player_avatars/large/$image")
    }

    fun imageNameToUrl(name: String): String {
        return "file:///android_asset/$name"
    }

    fun getRoundImage(image: String): String {
        return imageNameToUrl("round_avatars/$image")
    }

    private fun imageCustomNameToUrl(name: String, context: Context): String {
        val filesDir = context.filesDir
        return Uri.fromFile(filesDir).toString() + name//"file:///android_asset/$name"
    }

    fun saveImage(uri: Uri, context: Context): String {
        val avatarFile = File(uri.path!!)
        val avatarsDir = File(context.filesDir, AVATAR_RELATIVE_PATH)
        val newAvatarFile = File(avatarsDir, "$CUSTOM_AVATAR_PREFIX${uri.lastPathSegment}")

        val newFile = avatarFile.copyTo(newAvatarFile, false)
        avatarFile.delete()
        return newFile.name
    }
}