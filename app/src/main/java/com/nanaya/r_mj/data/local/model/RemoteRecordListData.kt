package com.nanaya.r_mj.data.local.model

data class RemoteRecordListData(
    val current: Int = 0, // 1
    val pages: Int = 0, // 790
    val records: List<Record> = listOf(),
    val size: Int = 0, // 12
    val total: Int = 0 // 9479
) {
    data class Record(
        val acid: Int = 0, // 140
        val createTime: String = "", // 2024-09-01T23:58:24
        val defense: Any? = null, // null
        val idx: Int = 0, // 1044020
        var logtime: String = "", // 2024-09-01T22:52:55
        val name1: String = "", // 青宇
        val name2: String = "", // 小泽_gsws
        val name3: String = "", // 一番振听
        val name4: String = "", // 张朔
        val phone1: Int = 0, // 34766
        val phone2: Int = 0, // 55056
        val phone3: Int = 0, // 29371
        val phone4: Int = 0, // 36384
        val point1: Int = 0, // 408
        val point2: Int = 0, // 349
        val point3: Int = 0, // 173
        val point4: Int = 0, // 70
        val score1: Int = 0, // 458
        val score2: Int = 0, // 199
        val score3: Int = 0, // -177
        val score4: Int = 0 // -480
    )
}