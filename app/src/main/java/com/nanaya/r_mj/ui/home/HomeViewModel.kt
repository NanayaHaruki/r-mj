package com.nanaya.r_mj.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import com.nanaya.r_mj.data.local.model.Area
import com.nanaya.r_mj.data.local.model.MjSchoolDetail
import com.nanaya.r_mj.data.local.model.MjSchoolDetailEntry
import com.nanaya.r_mj.data.repository.MahjongSchoolRepository
import com.nanaya.r_mj.ui.RmjScreen
import com.nanaya.r_mj.ui.common.LoadMoreState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeUiState {
    data class MjList(
        val isRefreshing: Boolean,
        val loadMoreState: LoadMoreState,
        val mjSchoolList: MutableList<MjSchoolDetail>,
        val areaSelectorData: List<Area>?
    ) : HomeUiState

    data class MjDetail(
        val entry: MjSchoolDetailEntry
    ) : HomeUiState
}

data class FilterArgs(
    var page: Int = 1,
    var name: String? = null,
    var areaName: String? = null,
    var province: String? = null,
    var city: String? = null
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MahjongSchoolRepository
) : ViewModel() {

    private val pageSize = 20
    private val filterArgs = FilterArgs()
    val _listFlow: MutableStateFlow<MutableList<MjSchoolDetail>> =MutableStateFlow(mutableListOf())

    val _areaSelectorData: MutableStateFlow<List<Area>?> = MutableStateFlow(null)

    private val _isRefreshing = MutableStateFlow(true)
    private val _loadMoreState = MutableStateFlow(LoadMoreState.Ready)
    private val _snackFlow = MutableStateFlow("")
    private val _state = MutableStateFlow<HomeUiState>(
        HomeUiState.MjList(
            true, LoadMoreState.Ready, mutableListOf(), null

        )
    )

    val state: StateFlow<HomeUiState>
        get() = _state

    val snack: StateFlow<String>
        get() = _snackFlow

    init {
        viewModelScope.launch {
            combine(
                _listFlow,
                _isRefreshing,
                _loadMoreState,
                _areaSelectorData
            ) { mjSchoolList, isRefreshing, _loadMoreState, areaSelectorData ->

                HomeUiState.MjList(
                    isRefreshing,
                    _loadMoreState,
                    mjSchoolList,
                    areaSelectorData
                )
            }.catch { throwable ->
                _snackFlow.value = throwable.message ?: ""
            }.collect {
                _state.value = it
            }
        }

        viewModelScope.launch {
            updateList()
        }
        viewModelScope.launch {
            _areaSelectorData.value = repository.fetchArea()
        }

    }


    fun updateList() {
        LogUtils.d("update list")
        filterArgs.page=1
        viewModelScope.launch {
            _isRefreshing.value=true
            val res = repository.refresh(filterArgs.page,pageSize,filterArgs.name,filterArgs.areaName,filterArgs.province,filterArgs.city)
            if(res.isSuccess){
                _listFlow.value = res.getOrNull()?.second?.toMutableList()?: mutableListOf()
            }else{
                _snackFlow.value = res.exceptionOrNull()?.message?:""
            }
            _isRefreshing.value = false
        }

    }

    fun updateListByName(name: String) {
        filterArgs.name=name
        updateList()
    }

    fun updateListByArea(
        a: String?,
        p: String?,
        c: String?
    ) {
        filterArgs.run {
            areaName=a
            province=p
            city=c
        }
        updateList()
    }

    fun loadMore() {
        LogUtils.d("loadmore")
        viewModelScope.launch {
            filterArgs.page++
            _loadMoreState.value = LoadMoreState.Loading
            val res = repository.refresh(filterArgs.page,pageSize,filterArgs.name,filterArgs.areaName,filterArgs.province,filterArgs.city)
            if(res.isSuccess){
                val data = _listFlow.value
                data.addAll(res.getOrNull()!!.second)
                _listFlow.emit(data)
                _loadMoreState.value = res.getOrNull()!!.first
            }else{
                filterArgs.page--
                _snackFlow.emit(res.exceptionOrNull()?.message?:"")
                _loadMoreState.value = LoadMoreState.Error
            }
        }
    }

}