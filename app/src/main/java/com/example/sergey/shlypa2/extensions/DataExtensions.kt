package com.example.sergey.shlypa2.extensions

import android.content.Context
import com.example.sergey.shlypa2.ImagesHelper
import com.example.sergey.shlypa2.beans.Player

fun Player.getSmallImage(context: Context) = ImagesHelper.smallImagePathPlayer(avatar, context)
fun Player.getLargeImage(context: Context) = ImagesHelper.largeImagePathPlayer(avatar, context)