package com.nanaya.r_mj.data

import com.nanaya.r_mj.data.local.model.Area
import com.nanaya.r_mj.data.local.model.BaseDTO
import com.nanaya.r_mj.data.local.model.MjSchoolDetail
import com.nanaya.r_mj.data.local.model.MjSchoolListData
import com.nanaya.r_mj.data.local.model.MjSchoolImg
import com.nanaya.r_mj.data.local.model.RemoteMonthRank
import com.nanaya.r_mj.data.local.model.RemotePersonalRecentRecords
import com.nanaya.r_mj.data.local.model.RemotePlayerHistoryListData
import com.nanaya.r_mj.data.local.model.RemotePlayerInfo
import com.nanaya.r_mj.data.local.model.RemotePlayerTech
import com.nanaya.r_mj.data.local.model.RemoteRankListData
import com.nanaya.r_mj.data.local.model.RemoteRecordListData
import com.nanaya.r_mj.data.local.model.RemoteSameTableListData
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {
    /** 雀庄列表 */
    @POST("gszapi/customer/rate/list")
    suspend fun fetchMahjongSchoolList(
        @QueryMap params: Map<String, String>
    ): BaseDTO<MjSchoolListData>

    /** 雀庄详情 */
    @POST("gszapi/customer/rate/detail")
    /** https://cdn.r-mj.com/r/rate.php?q=base/4 */
    suspend fun fetchMahjongSchoolDetail(@Body body: RequestBody): BaseDTO<MjSchoolDetail>

    /** 雀庄图片 */
    @POST("gszapi/customer/imginfo")
    suspend fun fetchImage(@Body body: RequestBody): BaseDTO<List<MjSchoolImg>>

    /** 大区关联数据 */
    @GET("gszapi/customer/findArea")
    suspend fun fetchArea(): BaseDTO<List<Area>>

    @POST("gszapi/customer/findRanking")
    suspend fun fetchRanking(
        @QueryMap params:Map<String,String>
    ):BaseDTO<RemoteRankListData>

    /** 对局记录 */
    @POST("gszapi/customer/raterecord/page")
    suspend fun fetchRecord(
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Body body: RequestBody
    ): BaseDTO<RemoteRecordListData>

    /** 查询榜单 */
    @POST("gszapi/producer/monthRank/list")
    suspend fun fetchMonthRank(@Body body: RequestBody):BaseDTO<List<RemoteMonthRank>>

    /** 玩家信息 */
    @POST("gszapi/customer/getCustomerByName")
    suspend fun fetchPlayerInfo(@Query("name") name:String):BaseDTO<RemotePlayerInfo>

    /** 玩家技术信息，雷达图 */
    @POST("gszapi/score/tech")
    suspend fun fetchPlayerTech(@Query("customerId") id:Int):BaseDTO<RemotePlayerTech>

    /** 同桌信息 */
    @POST("gszapi/score/hate")
    suspend fun fetchSameTableRecords(
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Query("customerId") customerId:Int
    ):BaseDTO<RemoteSameTableListData>

    /** 对战记录，分数名字 */
    @POST("gszapi/customer/rate/page")
    suspend fun fetchPlayerHistoryList(
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Query("customerId") customerId:Int
    ):BaseDTO<RemotePlayerHistoryListData>

    /** 25战 */
    @POST("gszapi/customer/getCustomerRateList")
    suspend fun fetchPersonalRecordList(
        @Query("customerId") id:Int):BaseDTO<List<RemotePersonalRecentRecords>>


}