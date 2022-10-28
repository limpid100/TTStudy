package com.dxl.ttstudy.util

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import androidx.activity.ComponentActivity
import androidx.lifecycle.*
import com.dxl.ttstudy.base.BaseApp
import com.dxl.ttstudy.ui.hanzi.Hanzi
import com.dxl.ttstudy.util.PinyinUtil.toPinyinVoiceFileName
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SoundUtils(private val activity: ComponentActivity) : LifecycleObserver,
    LifecycleEventObserver {

    init {
        activity.lifecycle.addObserver(this)
    }

    private var soundPool: SoundPool? = null

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        lllog("event = $event")
        if (event == Lifecycle.Event.ON_CREATE) {
            soundPool = SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(
                    AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build()
                )
                .build()

        } else if (event == Lifecycle.Event.ON_DESTROY) {
            soundPool?.release()
        }
    }


    fun playSound(resId: Int) {
        soundPool?.load(activity, resId, 1)
        soundPool?.setOnLoadCompleteListener { soundPool, sampleId, status ->
            lllog("加载成功，soundId = $sampleId")
            if (status == 0) {
                soundPool.play(sampleId, 1f, 1f, 1, 0, 1f)
            }
        }
    }

    fun read(hanzi: Hanzi?) {
        hanzi ?: return
        activity.lifecycleScope.launch {
            hanzi.pinyin.toPinyinVoiceFileName().split(" ").forEach { fileName ->
                kotlin.runCatching {
                    BaseApp.appContext.assets.openFd("pinyin/$fileName.mp3")
                }.onSuccess {
                    soundPool?.load(it, 1)
                    soundPool?.setOnLoadCompleteListener { soundPool, sampleId, status ->
                        lllog("加载成功，soundId = $sampleId")
                        if (status == 0) {
                            lllog("播放：$sampleId")
                            soundPool.play(sampleId, 1f, 1f, 1, 0, 1f)
                        }
                    }
                    delay(1000)
                }.onFailure {

                }
            }

        }

    }


}