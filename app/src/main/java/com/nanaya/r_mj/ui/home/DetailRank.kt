package com.nanaya.r_mj.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nanaya.r_mj.data.local.model.BaseListState
import com.nanaya.r_mj.data.local.model.RemoteRankListData
import com.nanaya.r_mj.data.local.model.RemoteRecordListData
import com.nanaya.r_mj.ui.common.SwipeRefreshAndLoadMoreList
import com.nanaya.r_mj.ui.share.ISelectorNode
import com.nanaya.r_mj.ui.share.Spinner
import com.nanaya.r_mj.ui.theme.Link
import com.nanaya.r_mj.ui.theme.White94
import com.nanaya.r_mj.ui.theme.White97
import kotlin.reflect.KProperty

@Composable
fun MjSchoolDetailRank(
    state: BaseListState<RemoteRankListData.Rank>,
    refresh: () -> Unit,
    loadMore: () -> Unit,
    filter: (Int) -> Unit,
    searchPlayer: (String) -> Unit
) {
    Column {
        val filterList = remember {
            mutableStateListOf(
                object : ISelectorNode<String> {
                    override fun text(): String = "本雀庄排行"

                    override fun value(): String = "本雀庄排行"
                }, object : ISelectorNode<String> {
                    override fun text(): String = "总排行"

                    override fun value(): String = "总排行"
                }
            )
        }
        var filterIndex by remember {
            mutableIntStateOf(0)
        }

        Spinner(
            modifier = Modifier.fillMaxWidth(),
            selectedIndex = filterIndex,
            items = filterList
        ) { index ->
            filterIndex=index
            filter(index)
        }
        RankItem(item = RemoteRankListData.Rank(
            name = "昵称",
            rateName = "归属雀庄",
            rankName = "段位",
            rate = "Rate",
            avgPoint = "均点",
            upAvgPosition = "均顺",
            totalRate = "总局数"
        ), idx = 1, {})
        SwipeRefreshAndLoadMoreList(
            modifier = Modifier,
            data = state.data,
            itemLayout = { idx, item -> RankItem(item, idx) { searchPlayer(it) } },
            isRefreshing = state.isRefreshing,
            loadMoreState = state.loadMoreState,
            onRefresh = refresh,
            onLoadMore = loadMore
        )
    }
}


@Composable
fun RankItem(
    item: RemoteRankListData.Rank,
    idx: Int,
    searchPlayer: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(if (idx % 2 == 1) White94 else White97)
            .clickable { searchPlayer(item.name) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.name,
            maxLines = 1,
            color = Link,
            modifier = Modifier.width(60.dp),
            textAlign = TextAlign.Center
        )
        VerticalDivider()
        Text(
            text = item.rateName,
            maxLines = 1,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        VerticalDivider()
        Text(text = item.rankName, modifier = Modifier.width(40.dp), textAlign = TextAlign.Center)
        VerticalDivider()
        Text(
            item.avgPoint,
            modifier = Modifier.width(40.dp),
            textAlign = TextAlign.Center
        )
        VerticalDivider()
        Text(
            if (item.upAvgPosition.length > 4) item.upAvgPosition.substring(
                0,
                4
            ) else item.upAvgPosition,
            modifier = Modifier.width(40.dp),
            textAlign = TextAlign.Center
        )
        VerticalDivider()
        Text(
            item.totalRate,
            modifier = Modifier.width(50.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RankItemP() {
    val item = RemoteRankListData.Rank(
        name = "aabbcc",
        rankName = "新人",
        rateName = "鸭川雀庄(北京人民大学店)",
        avgPoint = "123",
        upAvgPosition = "2.23",
        totalRate = "201"
    )
    Column {
        RankItem(item = item, 0) {

        }
        RankItem(item = item, 1) {

        }
    }
}