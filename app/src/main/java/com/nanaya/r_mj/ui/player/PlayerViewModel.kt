package com.nanaya.r_mj.ui.player

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanaya.r_mj.data.local.model.RemotePersonalRecentRecords
import com.nanaya.r_mj.data.local.model.RemotePlayerHistoryListData
import com.nanaya.r_mj.data.local.model.RemoteSameTableListData
import com.nanaya.r_mj.data.repository.PlayerRepository
import com.nanaya.r_mj.ui.RmjScreen
import com.nanaya.r_mj.ui.common.LoadMoreState
import com.nanaya.r_mj.ui.theme.Pie1
import com.nanaya.r_mj.ui.theme.Pie2
import com.nanaya.r_mj.ui.theme.Pie3
import com.nanaya.r_mj.ui.theme.Pie4
import com.nanaya.r_mj.ui.theme.White94
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.ehsannarmani.compose_charts.models.Pie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlayerUiState(
    val avatar: String = "",
    val rank: String = "",
    val rankTime: String = "",
    val rate: Int = 0,
    val rateName: String = "",
    val name: String = "",
    val allRank: Int = 0,
    val rateRank: Int = 0,
    val total: Int = 0,
    val flyEatOne: Int = 0,
    val maxPoint: Int = 0,
    val avgPoint: Int = 0,
    val beatPercent: Double = 0.0,


    val upRuleRound: Int? = null,
    val upRuleAvg: Double? = null,
    val upRuleSumPosition: Int? = null,

    val upAvgPosition: Double = 0.0,
    val upSumPosition: Int = 0,

    /** 雷达图 */
    val fire: Double = 0.0,
    val defence: Double = 0.0,
    val stabilize: Double = 0.0,
    val luck: Double = 0.0,
    val tech: Double = 0.0,
    val attack: Double = 0.0,

    /** 饼图 */
    val ratio1: Double = 0.0,
    val sort1: Int = 0,
    val avgPoint1: Int = 0,
    val ratio2: Double = 0.0,
    val sort2: Int = 0,
    val avgPoint2: Int = 0,
    val ratio3: Double = 0.0,
    val sort3: Int = 0,
    val avgPoint3: Int = 0,
    val ratio4: Double = 0.0,
    val sort4: Int = 0,
    val avgPoint4: Int = 0,
    val pieData:List<Pie> = listOf(Pie("",0.0, White94)),

    val recentSort:String="",
    val recentPoint:List<Double> = emptyList(),
    val sameTableRecordListData: SameTableRecordListData = SameTableRecordListData(),
    val playerRecordListData: PlayerRecordListData = PlayerRecordListData()
) {
    data class SameTableRecordListData(
        val isRefresh: Boolean = false,
        val loadMoreState: LoadMoreState = LoadMoreState.Ready,
        val data: List<RemoteSameTableListData.Record> = emptyList()
    )

    data class PlayerRecordListData(
        val isRefresh: Boolean = false,
        val loadMoreState: LoadMoreState = LoadMoreState.Ready,
        val data: List<RemotePlayerHistoryListData.Record> = emptyList()
    )

}

@HiltViewModel
class PlayerViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val repo: PlayerRepository
) : ViewModel() {
    val name = savedStateHandle.get<String>(RmjScreen.ARG_PLAYER_NAME) ?: ""
    private val _state = MutableStateFlow(PlayerUiState())
    val state: StateFlow<PlayerUiState>
        get() = _state

    private val pageSize = 20
    private var sameTablePage = 1
    private var playerRecordPage = 1

    private val notFindPlayer = "未查询到有效信息"
    val snackMessageFlow = MutableStateFlow("")
    init {
        viewModelScope.launch {
            val newUiState= repo.obtainPlayerUiState(name)
            if(newUiState==null){
                snackMessageFlow.emit(notFindPlayer)
                return@launch
            }
            val uiState = newUiState.copy(
                pieData = listOf(
                    Pie(
                        "%.2f%%\n%d回一位\n均点%d".format(
                            newUiState.ratio1, newUiState.sort1, newUiState.avgPoint1
                        ),
                        newUiState.ratio1,
                        color = Pie1,
                        selectedColor = Pie1
                    ),
                    Pie(
                        "%.2f%%\n%d回二位\n均点%d".format(
                            newUiState.ratio2, newUiState.sort2, newUiState.avgPoint2
                        ),
                        newUiState.ratio2,
                        color = Pie2,
                        selectedColor = Pie2
                    ),
                    Pie(
                        "%.2f%%\n%d回三位\n均点%d".format(
                            newUiState.ratio3, newUiState.sort3, newUiState.avgPoint3
                        ),
                        newUiState.ratio3,
                        color = Pie3,
                        selectedColor = Pie3
                    ),
                    Pie(
                        "%.2f%%\n%d回四位\n均点%d".format(
                            newUiState.ratio4, newUiState.sort4, newUiState.avgPoint4
                        ),
                        newUiState.ratio4,
                        color = Pie4,
                        selectedColor = Pie4
                    ),
                )
            )
            Log.d("player",uiState.toString())
            _state.emit(uiState)
        }

    }

    fun search(name: String) {
        viewModelScope.launch {
            val state = repo.obtainPlayerUiState(name)
            if(state==null){
snackMessageFlow.emit(notFindPlayer)
            }else{
                _state.value = state
            }
        }
    }

    fun clickPie(p:Pie){
        val clickIdx = _state.value.pieData.indexOf(p)
        val newPieData = _state.value.pieData.mapIndexed { index, pie -> pie.copy(selected = index==clickIdx) }
        _state.update { it.copy(pieData = newPieData) }
    }

}