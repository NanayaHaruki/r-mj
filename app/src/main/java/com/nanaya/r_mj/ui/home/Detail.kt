package com.nanaya.r_mj.ui.home

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.nanaya.r_mj.data.di.BASE_URL
import com.nanaya.r_mj.data.local.model.MjSchoolDetail
import com.nanaya.r_mj.data.local.model.MjSchoolDetailEntry
import com.nanaya.r_mj.data.local.model.MjSchoolImg
import com.nanaya.r_mj.ui.share.Loading
import com.nanaya.r_mj.ui.theme.CurrentIndicatorColor
import com.nanaya.r_mj.ui.theme.NormalIndicatorColor
import kotlinx.coroutines.delay

@Composable
fun MjSchoolDetailPage(
    vm: MjSchoolDetailViewModel = hiltViewModel(),
    navClick: () -> Unit,
) {
    val detail by vm.detail.collectAsStateWithLifecycle(null)

    Scaffold(
        topBar = {
            HomeAppbar(navClick = navClick)
        }
    ) { paddingValues: PaddingValues ->
        if (detail == null) {
            Loading(modifier = Modifier.padding(paddingValues))
        } else {
            var selectTabIndex by remember { mutableIntStateOf(0) }
            val lazyState = rememberLazyListState()
            LazyColumn(
                modifier = Modifier.padding(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr) + 8.dp,
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr) + 8.dp,
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                ),
                state = lazyState
            ) {
                item {
                    Text(text = detail?.name ?: "-", fontSize = 20.sp)
                }
                item {
                    Tags(txt = detail?.tag ?: "")
                }

                item {
                    val tabNames = remember {
                        listOf("介绍", "记录", "段位", "月榜")
                    }
                    Tabs(tabNameList = tabNames, selectTabIndex = selectTabIndex) { index ->
                        selectTabIndex = index
                    }
                }
                item {
                    when (selectTabIndex) {
                        0 -> MjSchoolDetailIntroduce(detail = detail!!)
                    }
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
        contentColor = CurrentIndicatorColor
    ) {
        tabNameList.forEachIndexed { index, tabName ->
            Tab(selected = selectTabIndex == index, onClick = { onTabClick(index) }) {
                Text(tabName, fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun Banner(
    urls: List<String>
) {
    Log.d("detail", "banner:$urls")
    val pageState = rememberPagerState { urls.size }
    LaunchedEffect(key1 = urls.firstOrNull()) {
        while (true) {
            delay(2000)
            pageState.animateScrollToPage(if (pageState.currentPage == urls.lastIndex) 0 else pageState.currentPage + 1)
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(state = pageState, modifier = Modifier
            .fillMaxWidth()
            .height(250.dp), key = { idx -> urls[idx] }
        ) { idx ->
            AsyncImage(
                model = urls[idx],
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Inside
            )

        }
        Row(modifier = Modifier.align(Alignment.BottomEnd)) {
            for (i in urls.indices) {
                Canvas(
                    modifier = Modifier
                        .padding(2.dp)
                        .size(5.dp)
                ) {
                    drawCircle(
                        color = if (i == pageState.currentPage) CurrentIndicatorColor else NormalIndicatorColor
                    )
                }
            }
        }
    }
}

@Composable
fun MjSchoolDetailIntroduce(
    detail: MjSchoolDetail
) {
    Column {
        Banner(
            urls = detail.picList
        )
        IntroduceBaseInfo(txt1 = "联系人", txt2 = detail.contact ?: "-")
        IntroduceBaseInfo(txt1 = "电话", txt2 = detail.mobile ?: "-")
        IntroduceBaseInfo(txt1 = "QQ", txt2 = detail.adminQQ ?: "")
        IntroduceBaseInfo(txt1 = "QQ群", txt2 = detail.qqGroup ?: "")
        IntroduceBaseInfo(txt1 = "微信", txt2 = detail.wechat ?: "")
        IntroduceBaseInfo(txt1 = "地址", txt2 = detail.address ?: "")
        Text(detail.rule ?: "")
    }
}

@Composable
private fun IntroduceBaseInfo(txt1: String, txt2: String) {
    Row {
        Text(txt1, modifier = Modifier.width(100.dp))
        Text(txt2)
    }
}

@Composable
fun MjSchoolDetailRecord(
    detail: MjSchoolDetail
) {

}

@Composable
fun MjSchoolDetailRank(
    detail: MjSchoolDetail
) {

}

@Composable
fun MjSchoolDetailLeaderboard(
    detail: MjSchoolDetail
) {

}