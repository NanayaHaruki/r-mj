package com.nanaya.r_mj.ui.common

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nanaya.r_mj.ui.share.Loading
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

enum class LoadMoreState {
    Ready, Loading, Error, NoData
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeRefreshAndLoadMoreList(
    modifier: Modifier,
    data: List<T>,
    itemLayout: @Composable (Int,T) -> Unit,
    isRefreshing: Boolean,
    loadMoreState: LoadMoreState,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    lazyColumnState: LazyListState = rememberLazyListState(),
) {

    PullToRefreshBox(isRefreshing = isRefreshing, onRefresh = onRefresh, modifier = modifier) {
        LazyColumn(state = lazyColumnState, horizontalAlignment = Alignment.CenterHorizontally) {
            items(data.size) { index ->
                itemLayout(index,data[index])
            }
            if (!isRefreshing) {
                item {
                    when (loadMoreState) {
                        LoadMoreState.Ready -> {
                            onLoadMore()
                        }

                        LoadMoreState.Loading -> Loading()
                        LoadMoreState.Error -> Box(
                            Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .clickable { onLoadMore()  }
                        ) {
                            Text(
                                text = "加载失败，点击重试",
                                fontSize = 18.sp,
                                modifier = Modifier .align(Alignment.Center)
                            )

                        }

                        LoadMoreState.NoData -> Box(
                            Modifier
                                .fillMaxWidth()
                                .height(40.dp)) {
                            Text(
                                text = "没有了～",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }

                }
            }

        }
    }
}


@Composable
@Preview
fun SwipeRefreshAndLoadMoreListPreview() {
    val list by remember {
        mutableStateOf(mutableListOf<String>())
    }
    var isRefresh by remember {
        mutableStateOf(false)
    }
    SwipeRefreshAndLoadMoreList(
        modifier = Modifier.fillMaxSize(),
        data = list,
        itemLayout = {idx, item-> Text(item) },
        isRefreshing = isRefresh,
        LoadMoreState.NoData,
        onRefresh = {
            isRefresh = true
            list.add("new")
            isRefresh = false
        },
        onLoadMore = { },
    )
}