package com.nanaya.r_mj.ui.home

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanaya.r_mj.data.Dispatcher
import com.nanaya.r_mj.data.RmjDispatcher
import com.nanaya.r_mj.data.local.model.MjSchoolDetail
import com.nanaya.r_mj.data.local.model.RemoteMonthRank
import com.nanaya.r_mj.data.repository.MonthRankRepository
import com.nanaya.r_mj.util.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

data class MonthRankUiState(
    val selectSchoolList: List<MjSchoolDetail>,
    val allSchoolList: List<MjSchoolDetail>,
    val fastDateRangeIndex: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val count: Int,
    val monthRankList: List<RemoteMonthRank>,
)

@HiltViewModel
class MonthRankViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: MonthRankRepository,
    @Dispatcher(RmjDispatcher.Default) private  val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {


    private val selectSchoolListFlow =
        MutableStateFlow<List<MjSchoolDetail>>(mutableListOf())
    private val allSchoolListFlow = MutableStateFlow<List<MjSchoolDetail>>(emptyList())

    private val fastDateRangeIndexFlow = MutableStateFlow(4)
    private val dateRangeFlow = MutableStateFlow(LocalDate.now().run {
        withDayOfMonth(1) to withDayOfMonth(lengthOfMonth())
    })
    private val countFlow = MutableStateFlow(1)
    private val monthRankListFlow = MutableStateFlow<List<RemoteMonthRank>>(emptyList())
    private val _state = MutableStateFlow(
        MonthRankUiState(
            selectSchoolListFlow.value,
            allSchoolListFlow.value,
            fastDateRangeIndexFlow.value,
            dateRangeFlow.value.first,
            dateRangeFlow.value.second,
            countFlow.value,
            monthRankListFlow.value,
        )
    )
    val state: StateFlow<MonthRankUiState>
        get() = _state

    init {
        viewModelScope.launch {
            combine(
                selectSchoolListFlow,
                allSchoolListFlow,
                fastDateRangeIndexFlow,
                dateRangeFlow,
                countFlow,
                monthRankListFlow
            ) { selectSchoolList, allSchoolList, fastDateRangeIndex, dataRange, count, list ->
                Log.d("monthRank", "combine ${selectSchoolList.size}")
                MonthRankUiState(
                    selectSchoolList,
                    allSchoolList,
                    fastDateRangeIndex,
                    dataRange.first,
                    dataRange.second,
                    count,
                    list
                )
            }.catch {
                it.printStackTrace()
            }
                .collect {
                    _state.value = it
                }
        }
        viewModelScope.launch {
            repo.fetchAllMahjongSchools().fold(
                onSuccess = { allSchoolListFlow.value = it.records },
                onFailure = { it.printStackTrace() }
            )

        }
    }

    fun selectFastDateRange(i: Int) {
        val now = LocalDate.now()
        fastDateRangeIndexFlow.value = i
        when (i) {
            1 -> dateRangeFlow.value = now to now
            2 -> dateRangeFlow.value = now.run { minusDays(2L) to this }
            3 -> dateRangeFlow.value = now.run {
                with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)) to with(
                    TemporalAdjusters.nextOrSame(
                        DayOfWeek.SUNDAY
                    )
                )
            }

            4 -> dateRangeFlow.value = LocalDate.now().run {
                withDayOfMonth(1) to withDayOfMonth(lengthOfMonth())
            }

            5 -> dateRangeFlow.value = now.let { date ->
                val month = date.month
                val year = date.year

                val (firstMonthOfQuarter, lastMonthOfQuarter) = when (month) {
                    Month.JANUARY, Month.FEBRUARY, Month.MARCH -> Pair(Month.JANUARY, Month.MARCH)
                    Month.APRIL, Month.MAY, Month.JUNE -> Pair(Month.APRIL, Month.JUNE)
                    Month.JULY, Month.AUGUST, Month.SEPTEMBER -> Pair(Month.JULY, Month.SEPTEMBER)
                    else -> Pair(
                        Month.OCTOBER,
                        Month.DECEMBER
                    )
                }

                val firstDayOfQuarter = LocalDate.of(year, firstMonthOfQuarter, 1)
                val lastDayOfQuarter = LocalDate.of(
                    year,
                    lastMonthOfQuarter,
                    lastMonthOfQuarter.length(date.isLeapYear)
                )
                firstDayOfQuarter to lastDayOfQuarter
            }

            6 -> dateRangeFlow.value = now.run {
                withDayOfYear(1) to withDayOfYear(lengthOfYear())
            }
        }
    }

    fun selectDateRange(start: LocalDate, end: LocalDate) {
        dateRangeFlow.value = start to end
    }


    fun search() {
        val (startTime, endTime) = dateRangeFlow.value.run {
            val (first, second) = this
            "%d-%02d-%02d 00:00:00".format(
                first.year,
                first.monthValue,
                first.dayOfMonth
            ) to "%d-%02d-%02d 23:59:59".format(
                second.year,
                second.monthValue,
                second.dayOfMonth
            )
        }
        viewModelScope.launch(defaultDispatcher) {
            val res = repo.fetchMonthRank(
                selectSchoolListFlow.value.map { it.id },
                startTime,
                endTime,
                countFlow.value
            )
            if (res.isSuccess) {
                val data = res.getOrNull()?: emptyList()
                data.sortedByDescending { it.score }
                monthRankListFlow.emit(data)
            } else {
                res.exceptionOrNull()?.printStackTrace()
            }
        }

    }

    fun setTotalCnt(cnt: Int) {
        countFlow.value = cnt
    }

    fun cancelSelectSchool(index: Int) {
        val selectSchoolList = selectSchoolListFlow.value.toMutableList()
        if (index >= selectSchoolList.size) {
            Log.e("monthRankVm", "删除越界")
        } else {
            selectSchoolList.removeAt(index)
            selectSchoolListFlow.value = selectSchoolList.toList()

        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("monthRank", "vm cleared")
    }

    fun selectSchool(mjSchoolDetail: MjSchoolDetail) {
        viewModelScope.launch {
            val selectSchoolList = selectSchoolListFlow.value.toMutableList()
            selectSchoolList.add(mjSchoolDetail)
            selectSchoolListFlow.value = selectSchoolList.toList()
        }
    }
}