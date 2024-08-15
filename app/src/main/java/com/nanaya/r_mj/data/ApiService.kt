package com.nanaya.r_mj.data

import com.nanaya.r_mj.data.remote.MahjongSchoolDetail
import com.nanaya.r_mj.data.remote.MahjongSchoolItem
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("r/rate.php")
    suspend fun getSchools(@Query("q") name:String="main"):List<MahjongSchoolItem>

    @GET("r/rate.php")
    /** https://cdn.r-mj.com/r/rate.php?q=base/4 */
    suspend fun getSchoolDetail(@Query("q") content:String):MahjongSchoolDetail
    companion object{
        private const val baseUrl = "https://cdn.r-mj.com/"
        private val retrofitInstance by lazy {
            val client = OkHttpClient.Builder()
                .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
            Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
        val instance = retrofitInstance.create(ApiService::class.java)
    }
}