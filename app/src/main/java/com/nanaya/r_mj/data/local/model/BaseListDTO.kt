package com.nanaya.r_mj.data.local.model

import com.nanaya.r_mj.data.local.model.RemotePlayerHistoryListData.Record

class BaseListDTO<out T>(
    val current: Int = 0, // 1
    val pages: Int = 0, // 2
    val records: List<T> = listOf(),
    val size: Int = 0, // 10
    val total: Int = 0 // 18
)