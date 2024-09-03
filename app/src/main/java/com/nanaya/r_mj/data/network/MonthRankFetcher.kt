package com.nanaya.r_mj.data.network

import com.nanaya.r_mj.data.ApiService
import com.nanaya.r_mj.data.Dispatcher
import com.nanaya.r_mj.data.RmjDispatcher
import getRequestBody
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject

class MonthRankFetcher @Inject constructor(
    private val apiService: ApiService,
    @Dispatcher(RmjDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun fetchMonthRank(
        pidList: List<Int>,
        startTime: String,
        endTime: String,
        count: Int,
        type: Int = 0
    ) =
        withContext(ioDispatcher) {
            runCatching {
                val jso = JSONObject()
                val pidListJson = JSONArray(pidList)
                val params = JSONObject().apply {
                    put("pidList", pidListJson)
                    put("startTime", startTime)
                    put("endTime", endTime)
                    put("count", count)
                    put("type", type)
                }
                jso.put("params", params)

                val dto = apiService.fetchMonthRank(getRequestBody(jso))
                if (dto.isSuccess()) {
                    Result.success(dto.data)
                } else {
                    Result.failure(IOException(dto.message))
                }
            }.getOrElse {
                Result.failure(it)
            }
        }
}