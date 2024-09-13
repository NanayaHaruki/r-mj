package com.nanaya.r_mj.data.local.model


data class RemoteSameTableListData(
    val current: Int = 0, // 1
    val pages: Int = 0, // 5
    val records: List<Record> = listOf(),
    val size: Int = 0, // 10
    val total: Int = 0 // 50
) {
    data class Record(
        val customerId: Int = 0, // 13625
        val hatred: Int = 0, // 6
        val myAvg: Double = 0.0, // 2.6364
        val mySort1: Int = 0, // 5
        val mySort2: Int = 0, // 3
        val mySort3: Int = 0, // 9
        val mySort4: Int = 0, // 5
        val name: String = "", // 皮皮蛇
        val opAvg: Double = 0.0, // 2.3636
        val opSort1: Int = 0, // 5
        val opSort2: Int = 0, // 7
        val opSort3: Int = 0, // 7
        val opSort4: Int = 0, // 3
        val total: Int = 0, // 22
        val winRate: Double = 0.0, // 0.5
        val wincount: Int = 0 // 11
    )
}
