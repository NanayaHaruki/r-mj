package com.nanaya.r_mj.data.local.model


data class MjSchoolListData(
    val current: Int = 0, // 1
    val pages: Int = 0, // 1
    val records: List<MjSchoolDetail> = listOf(),
    val size: Int = 0, // 12
    val total: Int = 0 // 12
)
