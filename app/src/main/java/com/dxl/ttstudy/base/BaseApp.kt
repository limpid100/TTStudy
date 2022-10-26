package com.dxl.ttstudy.base

import android.app.Application
import android.content.Context

class BaseApp: Application() {

    companion object {
        lateinit var instance: BaseApp
        val appContext: Context by lazy { instance.applicationContext }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}