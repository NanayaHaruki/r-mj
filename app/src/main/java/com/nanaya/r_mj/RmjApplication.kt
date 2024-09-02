package com.nanaya.r_mj

import android.app.Application
import com.blankj.utilcode.util.Utils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RmjApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
    }
}