package com.nanaya.r_mj.data.local.model

open class BaseDTO<T>(
    val code:Int,
    val message:String,
    val data:T
) {
    fun isSuccess() = code == 200
}