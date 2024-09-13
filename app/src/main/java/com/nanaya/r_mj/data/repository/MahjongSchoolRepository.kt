package com.nanaya.r_mj.data.repository

import android.util.Log
import com.blankj.utilcode.util.TimeUtils
import com.nanaya.r_mj.data.Dispatcher
import com.nanaya.r_mj.data.RmjDispatcher
import com.nanaya.r_mj.data.di.BASE_URL
import com.nanaya.r_mj.data.local.dao.MjSchoolDao
import com.nanaya.r_mj.data.local.dao.MjSchoolImgDao
import com.nanaya.r_mj.data.local.dao.TransactionRunner
import com.nanaya.r_mj.data.local.model.Area
import com.nanaya.r_mj.data.local.model.MjSchoolDetail
import com.nanaya.r_mj.data.local.model.MjSchoolDetailEntry
import com.nanaya.r_mj.data.local.model.MjSchoolImg
import com.nanaya.r_mj.data.network.MjSchoolFetcher
import com.nanaya.r_mj.ui.common.LoadMoreState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MahjongSchoolRepository @Inject constructor(
    private val fetcher: MjSchoolFetcher,
    private val dao: MjSchoolDao,
    private val imgDao: MjSchoolImgDao,
    private val transactionRunner: TransactionRunner,
    @Dispatcher(RmjDispatcher.IO) private val ioDispatcher: CoroutineDispatcher

) {
    private val tag = "MahjongSchoolRepository"
    suspend fun refresh(
        pageNo: Int,
        pageSize: Int,
        name: String?,
        area: String?,
        province: String?,
        city: String?
    ): Result<Pair<LoadMoreState, List<MjSchoolDetail>>> = withContext(ioDispatcher) {
        val res = fetcher.fetchList(pageNo, pageSize, name, area, province, city)
        if (res.isSuccess) {
            val listData = res.getOrNull()!!
            val details = listData.records
            val imgList = fetcher.fetchImage(details)
            details.forEachIndexed { index, mjSchoolDetail ->
                mjSchoolDetail.pic_logo = imgList.getOrNull(index)
                    ?.firstOrNull { it.type == MjSchoolImg.AVATAR }
                    ?.img
                    ?.let { BASE_URL.dropLast(1) + it }
            }
            transactionRunner {
                dao.insert(details)
                imgDao.insert(imgList.flatten())
            }
            if (listData.pages <= listData.current) {
                Result.success(LoadMoreState.NoMoreData to details)
            } else {
                Result.success(LoadMoreState.Ready to details)
            }
        } else {
            res.exceptionOrNull()?.printStackTrace()
            Result.failure(res.exceptionOrNull()!!)
        }
    }

    suspend fun refresh(id: Int) = withContext(ioDispatcher) {
        val res = fetcher.fetchDetail(id)
        if (res.isSuccess) {
            val detail = res.getOrNull()!!
            val imgList = fetcher.fetchImage(listOf(detail))
            detail.picList = imgList.first().filter { it.type == MjSchoolImg.PICTURE }
                .map { BASE_URL.dropLast(1) + it.img }
            transactionRunner {
                detail.refreshTime = TimeUtils.getNowString()
                dao.insert(detail)
                if (imgList.isNotEmpty()) {
                    imgDao.insert(imgList.flatten())
                }
                Log.d("repo", "insert detail")
            }
            detail

        } else {
            null
        }
    }

    suspend fun fetchArea(): List<Area> = withContext(ioDispatcher) {
        val areaList = fetcher.fetchArea()
        val data = mutableListOf<Area>()
        data.add(Area("全部", "", null))
        data.addAll(areaList)
        for (area in data) {
            val a = Area("全部", "", null)
            val provinceList = area.children?.toMutableList() ?: mutableListOf()
            provinceList.add(0, a)
            area.children = provinceList
            for (province in provinceList) {
                val b = Area("全部", "", null)
                val cityList = province.children?.toMutableList() ?: mutableListOf()
                cityList.add(0, b)
                province.children = cityList
            }
        }
        data
    }


    suspend fun fetchRecord(pageNo: Int,pageSize: Int,id:Int) = withContext(ioDispatcher){
        fetcher.fetchRecord(pageNo, pageSize, id)
    }

    suspend fun fetchRank(pageNo: Int,pageSize: Int,pid:Int?) = withContext(ioDispatcher){
        fetcher.fetchRank(pageNo, pageSize, pid)
    }

    fun getList(
        pageNo: Int,
        pageSize: Int,
        name: String?,
        areaName: String?,
        province: String?,
        city: String?,
    ): Flow<List<MjSchoolDetailEntry>> =
        dao.getList(pageNo * pageSize, name, areaName, province, city)


    fun getDetail(id: Int): Flow<MjSchoolDetailEntry?> {
        return dao.getDetail(id)
    }

    suspend fun insertOrUpdate(mjSchoolDetail: MjSchoolDetail) =
        withContext(ioDispatcher) {
            dao.insert(mjSchoolDetail)
        }

    suspend fun insertOrUpdate(list: List<MjSchoolDetail>) = withContext(ioDispatcher) {
        dao.insert(list)
    }

    suspend fun delete(mjSchoolDetail: MjSchoolDetail) = withContext(ioDispatcher) {
        dao.delete(mjSchoolDetail)
    }

    suspend fun count(): Int = withContext(ioDispatcher) {
        dao.count()
    }

}