package com.nanaya.r_mj.data

import android.content.Context
import com.nanaya.r_mj.data.local.DB
import com.nanaya.r_mj.data.local.LocalMjSchoolRepo
import com.nanaya.r_mj.data.remote.RemoteMjSchoolRepo

interface IAppContainer {
    abstract val schoolRepo:MahjongSchoolRepo
}

class AppContainerImpl(private val app: Context) :IAppContainer{
    override val schoolRepo by lazy { MahjongSchoolRepo(LocalMjSchoolRepo(DB(app)),RemoteMjSchoolRepo()) }
}