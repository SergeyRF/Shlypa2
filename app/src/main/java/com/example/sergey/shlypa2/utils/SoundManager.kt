package com.example.sergey.shlypa2.utils

import android.content.Context
import android.media.SoundPool
import android.os.Build
import com.example.sergey.shlypa2.R

/**
 * Created by alex on 4/22/18.
 */
class SoundManager(val context: Context,
                   var leftVolume : Float = 0.05f,
                   var rightVolume : Float = 0.05f) {

    private val soundPool : SoundPool by lazy {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SoundPool.Builder().setMaxStreams(2).build()
        } else {
            SoundPool(2, android.media.AudioManager.STREAM_MUSIC, 1);
        }
    }

    private val soundsMap: MutableMap<Int, Int> = mutableMapOf()

    fun play(resource : Int, lVol : Float = leftVolume, rVol : Float = rightVolume) {
        val soundId = soundsMap[resource]

        if(soundId == null) {
            soundsMap[resource] = soundPool.load(context, resource, 1)
            soundPool.setOnLoadCompleteListener{pool, sampleId, status ->
                if(status == 0) pool.play(sampleId, lVol, rVol, 1, 0, 1F)
            }
        } else {
            soundPool.play(soundId, lVol, rVol, 1, 0, 1f )
        }
    }

    fun release() {
        soundPool.release()
    }
}