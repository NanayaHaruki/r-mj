package com.nanaya.r_mj.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.Tags
import com.blankj.utilcode.util.LogUtils
import com.nanaya.r_mj.R
import com.nanaya.r_mj.data.local.model.Area
import com.nanaya.r_mj.data.local.model.MjSchoolDetail
import com.nanaya.r_mj.ui.RmjScreen
import com.nanaya.r_mj.ui.common.LoadMoreState
import com.nanaya.r_mj.ui.common.SwipeRefreshAndLoadMoreList
import com.nanaya.r_mj.ui.share.Spinner
import com.nanaya.r_mj.ui.theme.Area_East
import com.nanaya.r_mj.ui.theme.Area_East_North
import com.nanaya.r_mj.ui.theme.Area_Foreign
import com.nanaya.r_mj.ui.theme.Area_Middle_South
import com.nanaya.r_mj.ui.theme.Area_North
import com.nanaya.r_mj.ui.theme.Area_West_North
import com.nanaya.r_mj.ui.theme.Area_West_South
import com.nanaya.r_mj.ui.theme.White94
import com.nanaya.r_mj.ui.theme.White87
import com.nanaya.r_mj.ui.theme.topbarBg

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToDetail: (Int) -> Unit,
    navMonthRank: () -> Unit,
    backToMonthRank: (Int,String) -> Unit
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    MjSchoolListPage(
        uiState = uiState as HomeUiState.MjList,
        navMonthRank = navMonthRank,
        onRefresh = { viewModel.updateList() },
        onLoadMore = { viewModel.loadMore() },
        onItemClick = { idx,detail ->
            if ((uiState as HomeUiState.MjList).mode == RmjScreen.LIST_MODE_NORMAL)
                navigateToDetail(detail.id)
            else{
                backToMonthRank(idx,detail.name?:"-")
            }
        },
        onAreaChanged = viewModel::updateListByArea,
        onSearch = viewModel::updateListByName,
    )
}

@Composable
fun MjSchoolListPage(
    uiState: HomeUiState.MjList,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onItemClick: (Int,MjSchoolDetail) -> Unit,
    onAreaChanged: (String?, String?, String?) -> Unit,
    onSearch: (String) -> Unit,
    navMonthRank: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var searchContent by remember {
        mutableStateOf(TextFieldValue(""))
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            HomeAppbar {
                when (uiState.mode) {
                    RmjScreen.LIST_MODE_NORMAL -> TextButton(onClick = { navMonthRank() }) {
                        Text("月榜",color = Color.White)
                    }

//                    RmjScreen.LIST_MODE_SELECT -> TextButton(
//                        onClick = backToMonthRank
//                    ) {
//                        Text("完成")
//                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }

    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            com.nanaya.r_mj.ui.share.SearchBar(
                modifier = Modifier.padding(8.dp),
                label = {Text("输入雀庄名称")},
                searchQuery = searchContent,
                onSearchQueryChanged = {
                    searchContent = it
                },
                onSearch = { onSearch(it) }
            )
            if (!uiState.areaSelectorData.isNullOrEmpty()) {
                AreaSelector(data = uiState.areaSelectorData, onAreaChanged = onAreaChanged)
            }
            HomeScreenList(
                modifier = Modifier,
                mjList = uiState.mjSchoolList,
                isRefreshing = uiState.isRefreshing,
                loadMoreState = uiState.loadMoreState,
                onRefresh = onRefresh,
                onLoadMore = onLoadMore,
                onItemClick = onItemClick,
            )
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppbar(
    navClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Image(
                painter = painterResource(id = R.mipmap.logo),
                contentDescription = "logo",
                modifier = Modifier.width(200.dp),
                contentScale = ContentScale.Inside
            )
        },
        navigationIcon = {
            if (navClick != null) {
                IconButton(onClick = navClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(containerColor = topbarBg)
    )


}


@Composable
fun HomeScreenList(
    modifier: Modifier,
    mjList: List<MjSchoolDetail>,
    isRefreshing: Boolean,
    loadMoreState: LoadMoreState,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onItemClick: (Int,MjSchoolDetail) -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        val lazyColumnState = rememberLazyListState()
        if(isRefreshing){
            lazyColumnState.requestScrollToItem(0)
        }
        SwipeRefreshAndLoadMoreList(
            modifier = Modifier.fillMaxSize(),
            data = mjList,
            itemLayout = { idx, detail -> MjSchoolListItem(idx,detail,onItemClick)},
            isRefreshing = isRefreshing,
            loadMoreState = loadMoreState,
            onRefresh = onRefresh,
            onLoadMore = onLoadMore,
            lazyColumnState = lazyColumnState
        )
        IconButton(
            modifier = Modifier
                .size(48.dp)
                .padding(end = 8.dp, bottom = 16.dp)
                .align(Alignment.BottomEnd)
                .background(Color(0x55000000)),
            onClick = { lazyColumnState.requestScrollToItem(0) }
        ) {
            Icon(Icons.Filled.KeyboardArrowUp, null)
        }
    }

}

@Composable
fun MjSchoolListItem(
    idx:Int,
    detail: MjSchoolDetail,
    onClick: (Int,MjSchoolDetail) -> Unit
) {
    ElevatedCard(
        onClick = { onClick(idx,detail) }, modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = White94
        )
    ) {
        // 头像 名字  地址
        val avatarUrl = detail.pic_logo
        Row {
            AsyncImage(
                model = avatarUrl,
                contentDescription = "logo",
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.DarkGray)
                    .align(Alignment.CenterVertically),
            )
            Column(modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)) {
                Text(text = detail.name ?: "", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = "地址:${detail.address}", maxLines = 2)
                Text(text = "QQ群:${detail.qqGroup}")
            }
            if(detail.checkState!=null) {
                Checkbox(checked = detail.checkState!!,onCheckedChange = null, modifier = Modifier.align(Alignment.CenterVertically))
            }

        }
        // tag
        Tags(txt = detail.tag ?: "")
        // 地区
        Row(
            modifier = Modifier
                .fillMaxWidth(),

            ) {
            Box(
                modifier = Modifier
                    .size(100.dp, 50.dp)
                    .background(
                        when (detail.areaName) {
                            "东北" -> Area_East_North
                            "中南" -> Area_Middle_South
                            "华东" -> Area_East
                            "华北" -> Area_North
                            "西北" -> Area_West_North
                            "西南" -> Area_West_South
                            else -> Area_Foreign
                        }
                    )
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = detail.areaName ?: "",
                    fontSize = 18.sp
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .background(White87)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "${detail.province}-${detail.city}-${detail.district}",
                    fontSize = 18.sp
                )
            }

        }
    }
}

@Composable
fun Tags(txt: String) {
    Row(modifier = Modifier.padding(4.dp)) {
        txt.split(',').forEach { tag ->
            if (tag.trim().isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .border(
                            1.dp,
                            color = Area_Middle_South,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(4.dp)
                ) {
                    Text(text = tag)
                }
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}


@Composable
fun AreaSelector(
    data: List<Area>,
    onAreaChanged: (String?, String?, String?) -> Unit
) {
    var areaIndex by remember {
        mutableIntStateOf(0)
    }
    var provinceIndex by remember {
        mutableIntStateOf(0)
    }
    var cityIndex by remember {
        mutableIntStateOf(0)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Spinner(
            modifier = Modifier.width(80.dp),
            areaIndex,
            data
        ) { idx ->
            areaIndex = idx
            provinceIndex = 0
            cityIndex = 0
            onAreaChanged(
                data[areaIndex].value,
                data[areaIndex].children?.getOrNull(provinceIndex)?.value,
                data[areaIndex].children?.getOrNull(provinceIndex)?.children?.getOrNull(cityIndex)?.value
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Spinner(
            modifier = Modifier.weight(1f),
            provinceIndex,
            data[areaIndex].children
        ) { idx ->
            provinceIndex = idx
            cityIndex = 0
            onAreaChanged(
                data[areaIndex].value,
                data[areaIndex].children?.getOrNull(provinceIndex)?.value,
                data[areaIndex].children?.getOrNull(provinceIndex)?.children?.getOrNull(cityIndex)?.value
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Spinner(
            modifier = Modifier.weight(1f),
            cityIndex,
            data[areaIndex].children?.get(provinceIndex)?.children
        ) { idx ->
            cityIndex = idx
            onAreaChanged(
                data[areaIndex].value,
                data[areaIndex].children?.getOrNull(provinceIndex)?.value,
                data[areaIndex].children?.getOrNull(provinceIndex)?.children?.getOrNull(cityIndex)?.value
            )
        }
    }
}