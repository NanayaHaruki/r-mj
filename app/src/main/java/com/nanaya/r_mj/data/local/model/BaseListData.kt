package com.nanaya.r_mj.data.local.model

import com.nanaya.r_mj.ui.common.LoadMoreState

open class BaseListState<T>(
    val isRefreshing: Boolean,
    val loadMoreState: LoadMoreState,
    val data: MutableList<T>
)