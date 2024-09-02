package com.nanaya.r_mj.ui.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanaya.r_mj.data.local.model.MjSchoolDetail
import com.nanaya.r_mj.data.local.model.MjSchoolDetailEntry
import com.nanaya.r_mj.data.repository.MahjongSchoolRepository
import com.nanaya.r_mj.ui.RmjScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MjSchoolDetailViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val repo: MahjongSchoolRepository
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val id :Int= savedStateHandle[RmjScreen.ARG_DETAIL_ID]?:0
    val detail: Flow<MjSchoolDetail?> =
        savedStateHandle.getStateFlow(RmjScreen.ARG_DETAIL_ID, 0)
            .map { repo.refresh(it) }


}