package com.nanaya.r_mj.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nanaya.r_mj.data.local.model.BaseListState
import com.nanaya.r_mj.data.local.model.RemoteRecordListData
import com.nanaya.r_mj.ui.common.SwipeRefreshAndLoadMoreList
import com.nanaya.r_mj.ui.theme.CurrentIndicatorColor
import com.nanaya.r_mj.ui.theme.Link
import com.nanaya.r_mj.ui.theme.White80
import com.nanaya.r_mj.ui.theme.White87
import com.nanaya.r_mj.ui.theme.White94


@Composable
fun MjSchoolDetailRecord(
    state: BaseListState<RemoteRecordListData.Record>,
    refresh: () -> Unit,
    loadMore: () -> Unit,
    searchPlayer: (String) -> Unit
) {
    SwipeRefreshAndLoadMoreList(
        modifier = Modifier,
        data = state.data,
        itemLayout = {_, item -> DetailRecordListItem(item) { searchPlayer(it) } },
        isRefreshing = state.isRefreshing,
        loadMoreState = state.loadMoreState,
        onRefresh = refresh,
        onLoadMore = loadMore
    )
}


@Composable
fun DetailRecordListItem(
    item: RemoteRecordListData.Record,
    onClick: (String) -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column {

            Text(text = item.logtime, modifier = Modifier.align(Alignment.CenterHorizontally))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .padding(8.dp)
                    .clickable { onClick(item.name1) }) {
                    Text(
                        item.name1,
                        color = Link,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                    Text(
                        text = (item.point1 * 100).toString(),
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
                Box(modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clickable { onClick(item.name2) }) {
                    Text(item.name2, color = Link, modifier = Modifier.align(Alignment.CenterStart))
                    Text(
                        text = (item.point2 * 100).toString(),
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .padding(8.dp)
                    .clickable { onClick(item.name3) }) {
                    Text(item.name3, color = Link, modifier = Modifier.align(Alignment.CenterStart))
                    Text(
                        text = (item.point3 * 100).toString(),
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
                Box(modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .padding(8.dp)
                    .clickable { onClick(item.name4) }) {
                    Text(item.name4, color = Link, modifier = Modifier.align(Alignment.CenterStart))
                    Text(
                        text = (item.point4 * 100).toString(),
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun RecordListItemPreview() {
    val item = RemoteRecordListData.Record(
        name1 = "一位",
        name2 = "二位",
        name3 = "三位",
        name4 = "四位",
        point1 = 345,
        point2 = 123,
        point3 = 99,
        point4 = 2,
        logtime = "2024-01-01 12:22:33"
    )
    DetailRecordListItem(item = item, {})
}