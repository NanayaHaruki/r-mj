package com.nanaya.r_mj.ui.common

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
    itemLayout: @Composable (T, (T) -> Unit) -> Unit,
    isRefreshing: Boolean,
    loadMoreState: LoadMoreState,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onItemClick: (T) -> Unit,
    lazyColumnState: LazyListState = rememberLazyListState(),
) {

    PullToRefreshBox(isRefreshing = isRefreshing, onRefresh = onRefresh, modifier = modifier) {
        LazyColumn(state = lazyColumnState, horizontalAlignment = Alignment.CenterHorizontally) {
            items(data.size) { index ->
                itemLayout(data[index], onItemClick)
            }
            if (!isRefreshing) {
                item {
                    when (loadMoreState) {
                        LoadMoreState.Ready -> {
                            onLoadMore()
                        }

                        LoadMoreState.Loading -> CircularProgressIndicator()
                        LoadMoreState.Error -> Text(
                            text = "加载失败，点击重试",
                            Modifier.clickable { onLoadMore() })

                        LoadMoreState.NoData -> Text("没有了～")
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
        itemLayout = { item, action -> Text(item) },
        isRefreshing = isRefresh,
        LoadMoreState.NoData,
        onRefresh = {
            isRefresh = true
            list.add("new")
            isRefresh = false
        },
        onLoadMore = { },
        onItemClick = {}
    )
}