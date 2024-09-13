package com.nanaya.r_mj.data.network

import com.nanaya.r_mj.data.ApiService
import com.nanaya.r_mj.data.Dispatcher
import com.nanaya.r_mj.data.RmjDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class PlayerFetcher@Inject constructor(
    private val apiService: ApiService,
    @Dispatcher(RmjDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun fetchInfo(name:String)  = withContext(ioDispatcher){
        runCatching {
            val res = apiService.fetchPlayerInfo(name)
            if(res.isSuccess()){
                Result.success(res.data)
            }else{
                Result.failure(IOException(res.message))
            }
        }.getOrElse {
            it.printStackTrace()
            Result.failure(it)
        }
    }

    suspend fun fetchTech(customerId: Int) = withContext(ioDispatcher){
        runCatching {
            val res = apiService.fetchPlayerTech(customerId)
            if(res.isSuccess()){
                Result.success(res.data)
            }else{

                Result.failure(IOException(res.message))
            }
        }.getOrElse {
            it.printStackTrace()
            Result.failure(it)
        }
    }


    suspend fun fetchSameTableList(pageNo:Int,pageSize:Int,customerId:Int) = withContext(ioDispatcher){
        runCatching {
            val res = apiService.fetchSameTableRecords(pageNo, pageSize, customerId)
            if(res.isSuccess()){
                Result.success(res.data)
            }else{
                Result.failure(IOException(res.message))
            }
        }.getOrElse {
            it.printStackTrace()
            Result.failure(it)
        }
    }

    suspend fun fetchPlayerHistoryList(pageNo:Int,pageSize:Int,customerId:Int) = withContext(ioDispatcher){
        runCatching {
            val res = apiService.fetchPlayerHistoryList(pageNo, pageSize, customerId)
            if(res.isSuccess()){
                Result.success(res.data)
            }else{
                Result.failure(IOException(res.message))
            }
        }.getOrElse {
            it.printStackTrace()
            Result.failure(it)
        }
    }

    suspend fun fetchRecentRecords(id: Int) = withContext(ioDispatcher){
        runCatching {
            val res = apiService.fetchPersonalRecordList(id)
            if(res.isSuccess()){
                Result.success(res.data)
            }else{
                Result.failure(IOException(res.message))
            }
        }.getOrElse {
            it.printStackTrace()
            Result.failure(it)
        }
    }
}