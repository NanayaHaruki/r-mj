package com.nanaya.r_mj.data.local.model

import com.nanaya.r_mj.ui.share.ISelectorNode


data class Area(
    val text:String,
    val value:String,
    var children:List<Area>?=null
):ISelectorNode<String>{
    override fun text(): String = text
    override fun value():String = value
}