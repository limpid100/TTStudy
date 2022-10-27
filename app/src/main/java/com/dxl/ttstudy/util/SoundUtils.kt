package com.dxl.ttstudy.util

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool

object SoundUtils {

    private val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(1)
        .setAudioAttributes(
            AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build()
        )
        .build()

    fun Context.playSound(resId:Int) {
        soundPool.load(this, resId, 1)
        soundPool.setOnLoadCompleteListener { soundPool, sampleId, status ->
            if (status == 0) {
                soundPool.play(sampleId, 1f, 1f, 1, 0, 1f)
            }
        }
    }
}