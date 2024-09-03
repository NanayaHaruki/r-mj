package com.nanaya.r_mj.data.repository

import com.nanaya.r_mj.data.local.model.RemoteMonthRank
import com.nanaya.r_mj.data.network.MonthRankFetcher
import javax.inject.Inject

class MonthRankRepository @Inject constructor(
    val monthRankFetcher: MonthRankFetcher
) {
    suspend fun fetchMonthRank(
        pidList: List<Int>,
        startTime: String,
        endTime: String,
        count: Int
    ) = monthRankFetcher.fetchMonthRank(pidList, startTime, endTime, count)
}