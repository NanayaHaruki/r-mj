package com.nanaya.r_mj.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nanaya.r_mj.data.local.model.MjSchoolDetail
import com.nanaya.r_mj.ui.share.Loading
import com.nanaya.r_mj.ui.theme.Primary

@Composable
fun MjSchoolDetailPage(
    vm: MjSchoolDetailViewModel = hiltViewModel(),
    backNav: () -> Unit,
    searchPlayer:(String)->Unit,
) {
    val detail by vm.detail.collectAsStateWithLifecycle(null)
    val record by vm.recordState.collectAsStateWithLifecycle()
    val rank by vm.rankState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            HomeAppbar(navClick = backNav)
        }
    ) { paddingValues: PaddingValues ->
        if (detail == null) {
            Loading(modifier = Modifier.padding(paddingValues))
        } else {
            var selectTabIndex by remember { mutableIntStateOf(0) }
            val lazyState = rememberLazyListState()
            Column(
                modifier = Modifier.padding(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr) + 8.dp,
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr) + 8.dp,
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                ),
            ) {

                Text(text = detail?.name ?: "-", fontSize = 20.sp)

                Tags(txt = detail?.tag ?: "")


                val tabNames = remember {
                    listOf("介绍", "记录", "段位", "月榜")
                }
                Tabs(tabNameList = tabNames, selectTabIndex = selectTabIndex) { index ->
                    selectTabIndex = index
                }

                Spacer(modifier = Modifier.height(8.dp))
                when (selectTabIndex) {
                    0 -> MjSchoolDetailIntroduce(detail = detail!!)
                    1 -> MjSchoolDetailRecord(
                        state = record,
                        refresh = vm::refreshRecord,
                        loadMore = vm::loadMoreRecord,
                        searchPlayer = searchPlayer
                    )
                    2-> MjSchoolDetailRank(
                        state = rank,
                        refresh = vm::refreshRank,
                        loadMore = vm::loadMoreRank,
                        filter =vm::filterRank,
                        searchPlayer=searchPlayer
                    )
                }
            }
        }
    }
}




@Composable
fun Tabs(
    tabNameList: List<String>,
    selectTabIndex: Int,
    onTabClick: (Int) -> Unit
) {
    TabRow(
        selectedTabIndex = selectTabIndex,
        contentColor = Primary
    ) {
        tabNameList.forEachIndexed { index, tabName ->
            Tab(selected = selectTabIndex == index, onClick = { onTabClick(index) }) {
                Text(tabName, fontSize = 24.sp)
            }
        }
    }
}


@Composable
fun MjSchoolDetailLeaderboard(
    detail: MjSchoolDetail
) {

}

