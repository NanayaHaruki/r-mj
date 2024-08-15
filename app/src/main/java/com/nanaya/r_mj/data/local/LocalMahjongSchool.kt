package com.nanaya.r_mj.data.local

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.nanaya.r_mj.data.remote.MahjongSchoolDetail
import com.nanaya.r_mj.data.remote.MahjongSchoolItem
import com.nanaya.r_mj.ui.theme.Area_East
import com.nanaya.r_mj.ui.theme.Area_East_North
import com.nanaya.r_mj.ui.theme.Area_Middle_South
import com.nanaya.r_mj.ui.theme.Area_North
import com.nanaya.r_mj.ui.theme.Area_West

@Entity
data class LocalMahjongSchool(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    var cid: String,
    var name: String,
    var city: String,
    var area: String,
    var status: String,
    var qqGroup: String,
    var numerator: String,
    var denominator: String,
    var setting: String,
    var rule: String
) {
    @Ignore
    val color = when (area) {
        "华北" -> Area_East_North
        "东北" -> Area_North
        "华东" -> Area_East
        "中南" -> Area_Middle_South
        "西部" -> Area_West
        else -> Area_West
    }



    companion object {
        fun convertFromMahjongSchoolItem(d: MahjongSchoolItem,localMahjongSchool: LocalMahjongSchool?=null)  = localMahjongSchool?.apply {
            cid = d.cid
            name = d.name
            city = d.area_name
            area = d.area
            status = d.statusDesc
            numerator = d.numerator
            denominator = d.denominator
        } ?: LocalMahjongSchool(
            id = 0,
            cid = d.cid,
            name = d.name,
            city = d.area_name,
            area = d.area,
            status = d.statusDesc,
            qqGroup = "",
            numerator = d.numerator,
            denominator = d.denominator,
            setting = "",
            rule = "",
        )

        fun convertFromMahjongSchoolDetail(
            d: MahjongSchoolDetail,
            localMahjongSchool: LocalMahjongSchool? = null
        ) = localMahjongSchool?.apply {
            cid = d.cid
            name = d.name
            city = d.area_name
            area = d.area
            status = d.statusDesc
            qqGroup = d.qqgroup
            numerator = d.numerator
            denominator = d.denominator
            setting = d.setting
            rule = d.rule
        } ?: LocalMahjongSchool(
            id = 0,
            cid = d.cid,
            name = d.name,
            city = d.area_name,
            area = d.area,
            status = d.statusDesc,
            qqGroup = d.qqgroup,
            numerator = d.numerator,
            denominator = d.denominator,
            setting = d.setting,
            rule = d.rule,
        )

    }

}
