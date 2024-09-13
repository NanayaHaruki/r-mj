package com.nanaya.r_mj.data.local.model

data class RemotePlayerHistoryListData(
    val current: Int = 0, // 1
    val pages: Int = 0, // 2
    val records: List<Record> = listOf(),
    val size: Int = 0, // 10
    val total: Int = 0 // 18
) {
    data class Record(
        val logtime: String = "", // 2024-08-21T21:32:34
        val name1: String = "", // 番茄_13
        val name2: String = "", // 阿鹤
        val name3: String = "", // 石上三黏
        val name4: String = "", // 晓林
        val point1: Int = 0, // 301
        val point2: Int = 0, // 287
        val point3: Int = 0, // 238
        val point4: Int = 0, // 174
        val rateName: String = "", // 竹林雀庄·立直麻将研究部（巴南万达店）
        val score1: Int = 0, // 351
        val score2: Int = 0, // 137
        val score3: Int = 0, // -112
        val score4: Int = 0 // -376
    )
}