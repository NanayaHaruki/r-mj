package com.nanaya.r_mj

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.nanaya.r_mj.data.AppContainerImpl
import com.nanaya.r_mj.data.IAppContainer

class RmjApplication:Application() {
    lateinit var appContainer:IAppContainer
    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        appContainer = AppContainerImpl(this)
    }
}