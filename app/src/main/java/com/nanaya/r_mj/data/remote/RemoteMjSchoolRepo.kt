package com.nanaya.r_mj.data.remote

import android.util.Log
import com.nanaya.r_mj.data.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import java.io.IOException

class RemoteMjSchoolRepo {
     suspend fun getAllSchools(): Result<List<MahjongSchoolItem>> = withContext(Dispatchers.IO){
        try {
            val schools: List<MahjongSchoolItem> = ApiService.instance.getSchools()
            Log.d("remote","schools.size=${schools.size}")
            return@withContext Result.success(schools)
        }catch (e:Exception){
            return@withContext Result.failure(e)
        }
    }

     suspend fun getSchoolDetail(cid: String): Result<MahjongSchoolDetail> = withContext(Dispatchers.IO) {
        try {
            val schoolDetail = ApiService.instance.getSchoolDetail("base/$cid")
            return@withContext Result.success(schoolDetail)
        }catch (e:Exception){
            return@withContext Result.failure(IOException())
        }
    }

}