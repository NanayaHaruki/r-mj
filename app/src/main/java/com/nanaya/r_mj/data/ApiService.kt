package com.nanaya.r_mj.data

import com.nanaya.r_mj.data.local.model.Area
import com.nanaya.r_mj.data.local.model.BaseDTO
import com.nanaya.r_mj.data.local.model.MjSchoolDetail
import com.nanaya.r_mj.data.local.model.MjSchoolListData
import com.nanaya.r_mj.data.local.model.MjSchoolImg
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface ApiService {
    @POST("gszapi/customer/rate/list")
    suspend fun fetchMahjongSchoolList(
        @QueryMap params:Map<String,String>
    ):BaseDTO<MjSchoolListData>

    @POST("gszapi/customer/rate/detail")
    /** https://cdn.r-mj.com/r/rate.php?q=base/4 */
    suspend fun fetchMahjongSchoolDetail(@Body body:RequestBody): BaseDTO<MjSchoolDetail>

    @POST("gszapi/customer/imginfo")
    suspend fun fetchImage(@Body body:RequestBody):BaseDTO<List<MjSchoolImg>>

    @GET("gszapi/customer/findArea")
    suspend fun fetchArea():BaseDTO<List<Area>>
}