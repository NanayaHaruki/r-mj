package com.nanaya.r_mj.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanaya.r_mj.data.local.model.BaseListState
import com.nanaya.r_mj.data.local.model.MjSchoolDetail
import com.nanaya.r_mj.data.local.model.RemoteRankListData
import com.nanaya.r_mj.data.local.model.RemoteRecordListData
import com.nanaya.r_mj.data.repository.MahjongSchoolRepository
import com.nanaya.r_mj.ui.RmjScreen
import com.nanaya.r_mj.ui.common.LoadMoreState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class MjSchoolDetailViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val repo: MahjongSchoolRepository
) : ViewModel() {


    @OptIn(ExperimentalCoroutinesApi::class)
    val id: Int = savedStateHandle[RmjScreen.ARG_DETAIL_ID] ?: 0

    // 介绍
    val detail: Flow<MjSchoolDetail?> =
        savedStateHandle.getStateFlow(RmjScreen.ARG_DETAIL_ID, 0)
            .map { repo.refresh(it) }
    val hint = MutableStateFlow("")

    // 记录
    val recordRefreshing = MutableStateFlow(false)
    val recordLoadMoreState = MutableStateFlow(LoadMoreState.Ready)
    val recordListData = MutableStateFlow<MutableList<RemoteRecordListData.Record>>(mutableListOf())
    val recordState = MutableStateFlow(BaseListState(false, LoadMoreState.Ready, mutableListOf<RemoteRecordListData.Record>()))
    var recordPage = 0
    val pageSize = 20


    fun refreshRecord(){
        viewModelScope.launch {
            recordRefreshing.emit(true)
            val res = repo.fetchRecord(1,pageSize,id)
            if(res.isSuccess){
                recordPage=1
                val (_,newData) = res.getOrNull()!!
                recordListData.emit(newData.toMutableList())
            }else{
                hint.emit(res.exceptionOrNull()?.message?:"")
            }
            recordRefreshing.emit(false)
        }
    }

    fun loadMoreRecord(){
        viewModelScope.launch {
            recordLoadMoreState.emit(LoadMoreState.Loading)
            val res = repo.fetchRecord(recordPage+1,pageSize,id)
            if(res.isSuccess){
                recordPage++
                val (state,newData) = res.getOrNull()!!
                recordListData.emit(recordListData.value.apply { addAll(newData) })
                recordLoadMoreState.emit(state)
            }else{
                hint.emit(res.exceptionOrNull()?.message?:"")
                recordLoadMoreState.emit(LoadMoreState.Error)
            }

        }
    }
    // 段位
    val rankRefreshing = MutableStateFlow(false)
    val rankLoadMoreState = MutableStateFlow(LoadMoreState.Ready)
    val rankListData = MutableStateFlow<MutableList<RemoteRankListData.Rank>>(mutableListOf())
    val rankState = MutableStateFlow(BaseListState(false, LoadMoreState.Ready, mutableListOf<RemoteRankListData.Rank>()))
    var rankPage = 0
    val rankFilterId = MutableStateFlow<Int?>(id)

    fun filterRank(index:Int){
        rankFilterId.value = (if(index==0) id else null)
    }
    fun refreshRank(){
        viewModelScope.launch {
            rankRefreshing.emit(true)
            val res = repo.fetchRank(1,pageSize,rankFilterId.value)
            if(res.isSuccess){
                rankPage=1
                val (_,newData) = res.getOrNull()!!
                rankListData.emit(newData.toMutableList())
            }else{
                hint.emit(res.exceptionOrNull()?.message?:"")
            }
            rankRefreshing.emit(false)
        }
    }

    fun loadMoreRank(){
        viewModelScope.launch {
            rankLoadMoreState.emit(LoadMoreState.Loading)
            val res = repo.fetchRank(recordPage+1,pageSize,id)
            if(res.isSuccess){
                rankPage++
                val (state,newData) = res.getOrNull()!!
                rankListData.emit(rankListData.value.apply { addAll(newData) })
                rankLoadMoreState.emit(state)
            }else{
                hint.emit(res.exceptionOrNull()?.message?:"")
                rankLoadMoreState.emit(LoadMoreState.Error)
            }

        }
    }

    init {
        viewModelScope.launch {
            combine(
                recordRefreshing,
                recordLoadMoreState,
                recordListData
            ) { recordRefreshing, recordLoadMoreState, recordListData ->
                BaseListState(recordRefreshing, recordLoadMoreState, recordListData)
            }.catch {
                hint.value = it.message ?: ""
            }.collect {
                recordState.value = it
            }
        }

        viewModelScope.launch {
            combine(
                rankRefreshing,
                rankLoadMoreState,
                rankListData
            ) { recordRefreshing, recordLoadMoreState, rankListData ->
                BaseListState<RemoteRankListData.Rank>(recordRefreshing, recordLoadMoreState, rankListData)
            }.catch {
                hint.value = it.message ?: ""
            }.collect {
                rankState.value = it
            }
        }

        viewModelScope.launch {
            rankFilterId.collect{
                refreshRank()
            }
        }
    }
}