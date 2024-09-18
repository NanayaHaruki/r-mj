package com.nanaya.r_mj

import android.app.Application
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.Utils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RmjApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        LogUtils.d("""
            width  =${ScreenUtils.getScreenWidth()}
            height =${ScreenUtils.getScreenHeight()}
            density=${ScreenUtils.getScreenDensity()}
            dpi    =${ScreenUtils.getScreenDensityDpi()}
            xdpi   =${ScreenUtils.getScreenXDpi()}
            ydpi   =${ScreenUtils.getScreenYDpi()}
        """.trimIndent()
        )
    }
}