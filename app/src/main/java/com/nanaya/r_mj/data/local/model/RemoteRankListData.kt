package com.nanaya.r_mj.data.local.model

data class RemoteRankListData(
    val current: Int = 0, // 1
    val pages: Int = 0, // 53
    val records: List<Rank> = listOf(),
    val size: Int = 0, // 12
    val total: Int = 0 // 631
) {
    data class Rank(
        val name: String = "", // 三缺二三
        val rateName: String = "", // 国士无双智竞馆
        val rankName: String = "", // 九段
        val rate: String = "", // 2037
        val avgPoint: String = "", // 290
        val upAvgPosition: String = "", // 2.14
        val totalRate: String = "", // 465

        val avgSort: Any? = null, // null
        val count: Any? = null, // null
        val lsScore: Any? = null, // null
        val point: Any? = null, // null
        val position1: Int = 0, // 168
        val position2: Int = 0, // 122
        val position3: Int = 0, // 102
        val position4: Int = 0, // 73
        val rank: Int = 0, // 15
        val score: Any? = null, // null
        val sort: Any? = null, // null
        val sortCount: Any? = null, // null
        val totalScore: Double = 0.0, // 4922.9
        val upAvgSort: Any? = null, // null
        val upCount: Any? = null, // null
        val upRate: Int = 0 // 122
    )
}