package com.nanaya.r_mj.data.repository

import com.nanaya.r_mj.data.local.model.RemoteMonthRank
import com.nanaya.r_mj.data.network.MjSchoolFetcher
import com.nanaya.r_mj.data.network.MonthRankFetcher
import javax.inject.Inject

class MonthRankRepository @Inject constructor(
    private val monthRankFetcher: MonthRankFetcher,
    private val schoolFetcher:MjSchoolFetcher
) {
    suspend fun fetchMonthRank(
        pidList: List<Int>,
        startTime: String,
        endTime: String,
        count: Int
    ) = monthRankFetcher.fetchMonthRank(pidList, startTime, endTime, count)

    suspend fun fetchAllMahjongSchools() = schoolFetcher.fetchList(1,10000)
}