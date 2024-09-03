package com.nanaya.r_mj.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanaya.r_mj.data.local.model.RemoteMonthRank
import com.nanaya.r_mj.data.repository.MonthRankRepository
import com.nanaya.r_mj.ui.share.ISelectorNode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

data class MonthRankUiState(
    val selectNames: List<String>,
    val fastDateRangeIndex:Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val count: Int,
    val datas: List<RemoteMonthRank>
)

@HiltViewModel
class MonthRankViewModel @Inject constructor(
    val repo: MonthRankRepository
) : ViewModel() {



    private val fastDateRangeIndexFlow = MutableStateFlow(4)
    private val dateRangeFlow = MutableStateFlow(LocalDate.now().run {
        withDayOfMonth(1) to withDayOfMonth(lengthOfMonth())
    })
    private val selectNamesFlow = MutableStateFlow(emptyList<String>())
    private val countFlow = MutableStateFlow(1)
    private val monthRankListFlow = MutableStateFlow<List<RemoteMonthRank>>(emptyList())
    private val _state = MutableStateFlow(
        MonthRankUiState(
            selectNamesFlow.value,
            fastDateRangeIndexFlow.value,
            dateRangeFlow.value.first,
            dateRangeFlow.value.second,
            countFlow.value,
            monthRankListFlow.value
        )
    )
    val state: StateFlow<MonthRankUiState>
        get() = _state

    init {
        viewModelScope.launch {
            combine(
                selectNamesFlow,fastDateRangeIndexFlow, dateRangeFlow, countFlow, monthRankListFlow
            ) { names,fastDateRangeIndex, dataRange, count, list ->
                Log.d("monthrank", "$names $dataRange $count $list")
                MonthRankUiState(
                    names, fastDateRangeIndex,dataRange.first, dataRange.second, count, list
                )
            }.catch {
                it.printStackTrace()
            }
                .collect {
                    _state.value = it
                }
        }
    }

    fun selectFastDateRange(i: Int) {
        val now = LocalDate.now()
        when (i) {
            1 -> dateRangeFlow.value = now to now
            2 -> dateRangeFlow.value = now.run { minusDays(3L) to this }
            3 -> dateRangeFlow.value = now.run {
                with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)) to with(
                    TemporalAdjusters.previousOrSame(
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

    fun selectDateRange(start:Long,end:Long){
        dateRangeFlow.value = LocalDate.ofEpochDay(start) to LocalDate.ofEpochDay(end)
    }


    fun search() {
        TODO("Not yet implemented")
    }

    fun setTotalCnt(cnt: Int) {
        countFlow.value=cnt
    }
}