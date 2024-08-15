package com.nanaya.r_mj.data.remote

data class MahjongSchoolItem(
    val area_name: String = "",
    val cid: String = "",
    val ctype: String = "",
    val denominator: String = "",
    val name: String = "",
    val numerator: String = "",
    val status: String = ""
) {

    val area: String
        get() = when (ctype) {
            "1" -> "华北"
            "2" -> "东北"
            "3" -> "华东"
            "4" -> "中南"
            "5" -> "西部"
            else -> "其他"
        }

    val statusDesc: String
        get() = when (status) {
            "2" -> "全天营业"
            "4" -> "永久闭店"
            else -> ""
        }
}