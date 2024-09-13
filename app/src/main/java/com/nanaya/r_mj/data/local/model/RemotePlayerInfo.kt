package com.nanaya.r_mj.data.local.model

data class RemotePlayerInfo(
    val allRankNum: Int = 0, // 16158
    val avgPoint: Int = 0, // 29500
    val beatNum: Double = 0.0, // 0.6789907618953015
    val createTime: Any? = null, // null
    val formerNames: Any? = null, // null
    val honor: List<Any> = listOf(),
    val id: Int = 0, // 31648
    val lastRate: Any? = null, // null
    val maxPoint: Int = 0, // 57700
    val name: String = "", // 番茄_13
    val phone: Any? = null, // null
    val pid: Any? = null, // null
    val position1: Any? = null, // null
    val position2: Any? = null, // null
    val position3: Any? = null, // null
    val position4: Any? = null, // null
    val qq: Any? = null, // null
    val rank: Any? = null, // null
    val rankName: Any? = null, // null
    val rankRule: RankRule = RankRule(),
    val rankTime: String = "", // 2024-08-21
    val rate: Int = 0, // 1600
    val rateName: String = "", // 竹林雀庄·立直麻将研究部（巴南万达店）
    val rateRankNum: Int = 0, // 8
    val score: Any? = null, // null
    val sort: Any? = null, // null
    val sumPosition: Int = 0, // 6
    val ticket: Any? = null, // null
    val totalRate: Int = 0, // 18
    val totalScore: Any? = null, // null
    val upAvgPosition: Double = 0.0, // 1.5
    val upRate: Int = 0, // 4
    val useStatus: Any? = null, // null
    val wechat: Any? = null, // null
    val wxid: Any? = null // null
) {
    data class RankRule(
        val avg: String? = null, // 2.7
        val id: Int = 0, // 3
        val rank: String = "", // 4级
        val round: Int? = null, // 10
        val value: Int? = null // 27
    )
}