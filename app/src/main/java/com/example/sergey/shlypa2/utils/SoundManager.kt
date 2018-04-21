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

    enum class Sound(var id : Int) {
        CORRECT(R.raw.correct),
        WRONG(R.raw.wrong);

        var soundId = -1
    }

    fun preload(vararg sounds : Sound) {
        for(sound in sounds) {
            sound.soundId = soundPool.load(context, sound.id, 1)
        }
    }

    fun play(sound : Sound) {
        if(sound.soundId == -1) {
            preload(sound)
            soundPool.setOnLoadCompleteListener{pool, sampleId, status ->
                if(status == 0) pool.play(sampleId, leftVolume, rightVolume, 1, 0, 1F)
            }
        } else {
            soundPool.play(sound.soundId, leftVolume, rightVolume, 1, 0, 1f )
        }
    }

    fun release() {
        soundPool.release()
        for(s in Sound.values()) {
            s.soundId = -1
        }
    }
}