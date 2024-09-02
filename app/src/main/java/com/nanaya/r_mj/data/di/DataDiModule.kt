package com.nanaya.r_mj.data.di

import android.content.Context
import androidx.room.Room
import com.nanaya.r_mj.data.ApiService
import com.nanaya.r_mj.data.Dispatcher
import com.nanaya.r_mj.data.RmjDispatcher
import com.nanaya.r_mj.data.local.RmjDatabase
import com.nanaya.r_mj.data.local.dao.MjSchoolDao
import com.nanaya.r_mj.data.local.dao.MjSchoolImgDao
import com.nanaya.r_mj.data.local.dao.TransactionRunner
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val BASE_URL = "https://gsz.rmlinking.com/"

@Module
@InstallIn(SingletonComponent::class)
object DataDiModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): RmjDatabase = Room.databaseBuilder(context, RmjDatabase::class.java, "rmj.db")
        .fallbackToDestructiveMigration()
        .build()


    @Provides
    @Singleton
    fun provideMahjongSchoolDetailDao(
        rmjDatabase: RmjDatabase
    ): MjSchoolDao = rmjDatabase.mjSchoolDao()

    @Provides
    @Singleton
    fun provideMjSchoolImgDao(
        rmjDatabase: RmjDatabase
    ):MjSchoolImgDao = rmjDatabase.mjSchoolImgDao()

    @Provides
    @Singleton
    fun provideTransactionRunnerDao(
        rmjDatabase: RmjDatabase
    ):TransactionRunner = rmjDatabase.transactionRunnerDao()

    @Provides
    @Singleton
    @Dispatcher(RmjDispatcher.IO)
    fun provideIODispatcher() :CoroutineDispatcher= Dispatchers.IO

    @Provides
    @Singleton
    @Dispatcher(RmjDispatcher.Main)
    fun provideMainDispatcher() :CoroutineDispatcher= Dispatchers.Main

    @Provides
    @Singleton
    @Dispatcher(RmjDispatcher.Default)
    fun provideDefaultDispatcher() :CoroutineDispatcher= Dispatchers.Default


}