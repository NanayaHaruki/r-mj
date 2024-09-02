package com.nanaya.r_mj.data.network

import com.nanaya.r_mj.data.ApiService
import com.nanaya.r_mj.data.Dispatcher
import com.nanaya.r_mj.data.RmjDispatcher
import com.nanaya.r_mj.data.local.model.Area
import com.nanaya.r_mj.data.local.model.BaseDTO
import com.nanaya.r_mj.data.local.model.MjSchoolDetail
import com.nanaya.r_mj.data.local.model.MjSchoolListData
import com.nanaya.r_mj.data.local.model.MjSchoolImg
import getRequestBody
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject

class MjSchoolFetcher @Inject constructor(
    private val apiService: ApiService,
    @Dispatcher(RmjDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun fetchList(
        pageNo: Int,
        pageSize: Int,
        name: String? = null,
        areaName: String? = null,
        province: String? = null,
        city: String? = null
    ): Result<MjSchoolListData> = withContext(ioDispatcher) {
        runCatching {
            val queryMap = mutableMapOf<String, String>(
                "pageNo" to pageNo.toString(),
                "pageSize" to pageSize.toString()
            )

            name?.let { queryMap["name"] = it }
            areaName?.let { queryMap["areaName"] = it }
            province?.let { queryMap["province"] = it }
            city?.let { queryMap["city"] = it }

            val dto = apiService.fetchMahjongSchoolList(queryMap)
            if(dto.isSuccess()){
                Result.success(dto.data)
            }else{
                Result.failure(IOException(dto.message))
            }
        } .getOrElse {  e->
            Result.failure(e)
        }
    }

    suspend fun fetchDetail(id:Int):Result<MjSchoolDetail> = withContext(ioDispatcher){
        runCatching {
            val dto = apiService.fetchMahjongSchoolDetail(createRequestBody(id))
            if(dto.isSuccess()){
                Result.success(dto.data)
            }else{
                Result.failure(IOException(dto.message))
            }
        }.getOrElse { e->
            Result.failure(IOException(e))
        }
    }
    private fun createRequestBody(id: Int) = JSONObject().let {
        it.put("params", JSONObject().apply { put("id", id) })
        getRequestBody(it)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun fetchImage(details: List<MjSchoolDetail>): List<List<MjSchoolImg>> =
        withContext(ioDispatcher) {
            val deferredResults = details.map { detail ->
                async {
                    runCatching {
                        val dto = apiService.fetchImage(createRequestBody(detail.id))
                        if (dto.isSuccess()) {
                            dto.data.forEach {
                                it.schoolId = detail.id
                            }
                            dto.data
                        } else {
                            emptyList()
                        }
                    }.getOrElse { emptyList() }

                }
            }
            return@withContext deferredResults.awaitAll()
        }

    suspend fun fetchArea():List<Area> = withContext(ioDispatcher){
        runCatching {
            val res = apiService.fetchArea()
            if(res.isSuccess()){
                res.data
            }else{
                emptyList()
            }
        }.getOrElse {
            emptyList()
        }
    }

}