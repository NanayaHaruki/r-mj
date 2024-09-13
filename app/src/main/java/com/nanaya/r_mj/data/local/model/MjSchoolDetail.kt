package com.nanaya.r_mj.data.local.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.nanaya.r_mj.ui.RmjScreen

@Entity(
    tableName = "mj_school",
    indices = [
        Index("name"),
        Index("areaName"),
        Index("province"),
        Index("city")
    ]
)
data class MjSchoolDetail(
    @PrimaryKey
    val id: Int = 0, // 226
    var address: String? = null, // 塔韵路199号绿憬商务广场1幢1310室
    var adminQQ: String? = null, // null
    var apply: Int = 0, // 2
    var areaName: String? = null, // 华东
    var city: String? = null, // 苏州市
    var compString: String? = null, // null
    var contact: String? = null, // null
    var contact1: String? = null, // null
    var contact2: String? = null, // null
    var denominator: String? = null, // null
    var district: String? = null, // 吴中区
    var edit: String? = null, // null
    var examineTime: String? = null, // null
    var healthy: Int = 0, // 0

    var info1: String? = null, // null
    var info2: String? = null, // null
    var lat: String? = null, // 31.214263
    var license: String? = null, // null
    var lng: String? = null, // 120.595094
    var mobile: String? = null, // null
    var mobile1: String? = null, // null
    var mobile2: String? = null, // null
    var name: String? = null, // 苏州麻雀街雀馆
    var newStore: Int = 0, // 1
    var numerator: String? = null, // null
    var popularize: Int = 0, // 0
    var popularizeTime: String? = null, // null
    var position1: String? = null, // null
    var position2: String? = null, // null
    var province: String? = null, // 江苏省
    var qqGroup: String? = null, // 917161241
    var rule: String? = null, // 机器配置：1️⃣大洋化学REXX3八口机一台2️⃣大洋化学JPC白色四口机两台（30）雀庄内零食饮料主食（馄饨/卤肉面）免费畅吃畅饮。
    var score: String? = null, // null
    var shortName: String? = null, // 麻雀街
    var status: String? = null, // null
    var tag: String? = null, // 有包间,八口机,支持散排,可吸烟
    var topping: Int = 0, // 0
    var toppingTime: String? = null, // null
    var type: Int = 0, // 5
    var updateTime: String? = null, // null
    var wechat: String? = null, // null
    var pic_license:String?=null,
    var pic_logo:String?=null,
    var pic1:String?=null,
    var pic2:String?=null,
    var pic3:String?=null,
    var pic4:String?=null,

    var refreshTime:String =""

){
    @Ignore
    var checkState:Boolean?=null
    @Ignore
    var picList:List<String> = emptyList()

    override fun equals(other: Any?): Boolean {
        if(this===other) return true
        if(other==null || javaClass!=other.javaClass) return false
        return id == (other as MjSchoolDetail).id
    }
    override fun hashCode(): Int {
        return id
    }
}