package com.dxl.ttstudy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.dxl.ttstudy.databinding.ActivityMainBinding
import com.dxl.ttstudy.ui.hanzi.HanziActivity
import com.dxl.ttstudy.ui.pinyin.PinyinActivity

class MainActivity : AppCompatActivity() {

    private lateinit var vb:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)

        vb.btnHanzi.setOnClickListener {
            HanziActivity.start(this, 0)
        }

        vb.btnPinyin.setOnClickListener {
            HanziActivity.start(this, 1)
        }

        vb.btnPinyinList.setOnClickListener {
            PinyinActivity.start(this)
        }
    }


}