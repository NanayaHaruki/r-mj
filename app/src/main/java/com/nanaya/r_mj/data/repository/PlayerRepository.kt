package com.nanaya.r_mj.data.repository

import com.nanaya.r_mj.data.Dispatcher
import com.nanaya.r_mj.data.RmjDispatcher
import com.nanaya.r_mj.data.local.model.RemotePlayerHistoryListData
import com.nanaya.r_mj.data.local.model.RemoteSameTableListData
import com.nanaya.r_mj.data.network.PlayerFetcher
import com.nanaya.r_mj.ui.common.LoadMoreState
import com.nanaya.r_mj.ui.player.PlayerUiState
import com.nanaya.r_mj.ui.theme.Pie1
import com.nanaya.r_mj.ui.theme.Pie2
import com.nanaya.r_mj.ui.theme.Pie3
import com.nanaya.r_mj.ui.theme.Pie4
import ir.ehsannarmani.compose_charts.models.Pie
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class PlayerRepository @Inject constructor(
    private val fetcher: PlayerFetcher,
    @Dispatcher(RmjDispatcher.Default) private val defaultDispatcher: CoroutineDispatcher

) {
    suspend fun obtainSameTableList(
        pageNo: Int,
        pageSize: Int,
        customerId: Int
    ): Result<Pair<LoadMoreState, List<RemoteSameTableListData.Record>>> {
        val res = fetcher.fetchSameTableList(pageNo, pageSize, customerId)
        return if (res.isSuccess) {
            val data = res.getOrNull()!!
            if (data.current >= data.pages) {
                Result.success(LoadMoreState.NoMoreData to data.records)
            } else {
                Result.success(LoadMoreState.Ready to data.records)
            }
        } else {
            res.exceptionOrNull()?.printStackTrace()
            Result.failure(res.exceptionOrNull()!!)
        }
    }

    suspend fun obtainPlayerHistoryList(
        pageNo: Int,
        pageSize: Int,
        customerId: Int
    ): Result<Pair<LoadMoreState, List<RemotePlayerHistoryListData.Record>>> {
        val res = fetcher.fetchPlayerHistoryList(pageNo, pageSize, customerId)
        return if (res.isSuccess) {
            val data = res.getOrNull()!!
            if (data.current >= data.pages) {
                Result.success(LoadMoreState.NoMoreData to data.records)
            } else {
                Result.success(LoadMoreState.Ready to data.records)
            }
        } else {
            res.exceptionOrNull()?.printStackTrace()
            Result.failure(res.exceptionOrNull()!!)
        }
    }

    suspend fun obtainPlayerInfo(name: String) = fetcher.fetchInfo(name)
    suspend fun obtainPlayerTech(id: Int) = fetcher.fetchTech(id)
    suspend fun obtainRecentRecords(id: Int) = fetcher.fetchRecentRecords(id)

    /** 玩家信息，不包括 同桌和对战记录  这两个在UI显示的时候会触发load more加载数据 */
    suspend fun obtainPlayerUiState(name: String): PlayerUiState? =
        withContext(defaultDispatcher) {
            val info = obtainPlayerInfo(name).getOrNull() ?: return@withContext null
            val techDeferred = async { obtainPlayerTech(info.id) }
            val recentRecordsDeferred = async { obtainRecentRecords(info.id) }

            val tech = techDeferred.await().getOrNull() ?: return@withContext null
            val recentRecords =
                recentRecordsDeferred.await().getOrNull() ?.reversed()?: return@withContext null

            val sortList = recentRecords.map { it.sort }
            val pointList = recentRecords.map { it.point*100.0 }
            val sortBuilder = StringBuilder()
            for (i in sortList.indices){
                if(i>0 && i%5==0){
                    sortBuilder.append(' ')
                }
                sortBuilder.append(sortList[i])
            }
            val sortStr = sortBuilder.toString()
            PlayerUiState(
                avatar = "https://q.qlogo.cn/headimg_dl?dst_uin=${info.qq}&spec=640&img_type=jpg",
                rank = "${info.rankRule.rank}@${info.rankTime}",
                rate = info.rate,
                name = info.name,
                rateName = info.rateName,
                allRank = info.allRankNum,
                rateRank = info.rateRankNum,
                total = info.totalRate,
                maxPoint = info.maxPoint,
                avgPoint = info.avgPoint,
                beatPercent = info.beatNum,
                upRuleRound = info.rankRule.round,
                upRuleAvg = info.rankRule.avg?.toDoubleOrNull(),
                upRuleSumPosition = info.rankRule.value,
                upAvgPosition = info.upAvgPosition,
                upSumPosition = info.sumPosition,
                // 火力，1位点数，25000 为0，50000为5，5000 1档
                fire = min(5.0, (tech.fire - 25000) / 5000.0),
                // 防守，不飞率，有问题，不飞率应该是0.08-0.04，可实际数据都在20%左右
                defence = min(5.0, tech.defense / 0.2),
                // 稳定，连对率，意义不明
                stabilize = min(5.0, tech.stabilize / 0.1),
                // 运势，十战均顺,根据天凤2.4  9级2.65
                luck = max(5.0, min(0.0, (2.65 - tech.lucky) / 0.05)),

                // 技术，rate
                tech = max(5.0, min(0.0, 2.5 + (tech.tech - 1500) / 200)),
                // 进攻，1位率 ，根据天凤0.27  9级0.22
                attack = max(5.0, min(0.0, (tech.attack - 0.22) / 0.01)),
                ratio1 = tech.ratio1,
                sort1 = tech.sort1,
                avgPoint1 = tech.avg1,

                ratio2 = tech.ratio2,
                sort2 = tech.sort2,
                avgPoint2 = tech.avg2,
                ratio3 = tech.ratio3,
                sort3 = tech.sort3,
                avgPoint3 = tech.avg3,
                ratio4 = tech.ratio4,
                sort4 = tech.sort4,
                avgPoint4 = tech.avg4,
                flyEatOne = tech.flyEatOne,
                recentSort = sortStr,
                recentPoint = pointList,

            )
        }
}