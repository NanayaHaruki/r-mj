package com.nanaya.r_mj.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nanaya.r_mj.data.local.model.RemoteMonthRank
import com.nanaya.r_mj.ui.share.ISelectorNode
import com.nanaya.r_mj.ui.share.Spinner
import com.nanaya.r_mj.ui.theme.White94
import com.nanaya.r_mj.ui.theme.White97
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthRankPage(
    viewModel: MonthRankViewModel = hiltViewModel(),
    navMjSchoolList: () -> Unit,
    navPop: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()


    Scaffold(
        topBar = {
            HomeAppbar(
                navClick = navPop
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr) + 8.dp,
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr) + 8.dp,
                    top = paddingValues.calculateTopPadding()+8.dp,
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            var showFilter by remember {
                mutableStateOf(false)
            }
            if (!showFilter) {
                OutlinedButton(
                    onClick = { showFilter = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),

                ) {
                    Text("设置过滤条件")
                }
            }
            LazyColumn(modifier = Modifier.padding(top = 50.dp)) {
                items(uiState.datas) { item: RemoteMonthRank ->

                }
            }
            if (showFilter) {
                FilterMonthRank(
                    modifier = Modifier,
                    navMjSchoolList = navMjSchoolList,
                    selectNames = uiState.selectNames,
                    fastDateRangeIndex = uiState.fastDateRangeIndex,
                    startDate = uiState.startDate,
                    endDate = uiState.endDate,
                    onFastRangeSelected = viewModel::selectFastDateRange,
                    onDateRangeSelected = viewModel::selectDateRange,
                    count = uiState.count,
                    onCountChanged = viewModel::setTotalCnt,
                    onSearch = viewModel::search,
                    onCancel = { showFilter = false }
                )
            }

        }
    }

}

@Composable
fun MonthRankListItem(
    remoteMonthRank: RemoteMonthRank,
    onPlayerClick: (Int) -> Unit,
) {

}


@Composable
fun FilterMonthRank(
    modifier: Modifier,
    navMjSchoolList: () -> Unit,
    selectNames: List<String>,
    fastDateRangeIndex: Int,
    startDate: LocalDate,
    endDate: LocalDate,
    onFastRangeSelected: (Int) -> Unit,
    onDateRangeSelected: (Long, Long) -> Unit,
    count: Int,
    onCountChanged: (Int) -> Unit,
    onSearch: () -> Unit,
    onCancel: () -> Unit,

    ) {
    Column(modifier = modifier.background(White97)) {
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { navMjSchoolList() },
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "请选择雀庄")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
        ) {
            items(selectNames) { name ->
                Text(text = name)
            }
        }
        DatePicker(
            startDate = startDate,
            endDate = endDate,
            fastDataRangeIndex = fastDateRangeIndex,
            onFastRangeSelected = onFastRangeSelected,
            onDateRangeSelected = onDateRangeSelected
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("对局数>=")
            OutlinedTextField(
                value = count.toString(),
                onValueChange = {
                    it.toIntOrNull()?.let { num ->
                        onCountChanged(num)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                )
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.SpaceEvenly
        ) {
            OutlinedButton(onClick = onCancel) {
                Text("取消")
            }
            Button(
                onClick = onSearch,
            ) {
                Text("搜索")
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    fastDataRangeIndex: Int,
    startDate: LocalDate,
    endDate: LocalDate,
    onFastRangeSelected: (Int) -> Unit,
    onDateRangeSelected: (Long, Long) -> Unit
) {

    var showDateRangeDialog by remember {
        mutableStateOf(false)
    }
    val quickDateRangeList by remember {
        mutableStateOf(
            listOf("自定义", "日榜", "三日榜", "周榜", "月榜", "季榜", "年榜").map {
                object : ISelectorNode<String> {
                    override fun text(): String = it

                    override fun value(): String = it
                }
            })
    }
    Column {
        Spinner(
            modifier = Modifier.fillMaxWidth(),
            selectedIndex = fastDataRangeIndex,
            items = quickDateRangeList
        ) { index ->
            onFastRangeSelected(index)
        }
        OutlinedButton(
            onClick = {
                showDateRangeDialog = true
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                "%d-%02d-%02d 00:00:00 - %d-%02d-%02d 23:59:59".format(
                    startDate.year,
                    startDate.monthValue,
                    startDate.dayOfMonth,
                    endDate.year,
                    endDate.monthValue,
                    endDate.dayOfMonth
                )
            )
        }
        if (showDateRangeDialog) {
            val dateRangeState = rememberDateRangePickerState(
                initialSelectedStartDateMillis = startDate.toEpochDay(),
                initialSelectedEndDateMillis = endDate.toEpochDay(),
                initialDisplayMode = DisplayMode.Input
            )
            val confirmEnabled = remember {
                derivedStateOf { dateRangeState.selectedStartDateMillis != null && dateRangeState.selectedEndDateMillis != null }
            }
            DatePickerDialog(
                onDismissRequest = { showDateRangeDialog = false },
                confirmButton = {
                    Button(
                        onClick = {
                            onDateRangeSelected(
                                dateRangeState.selectedStartDateMillis!!,
                                dateRangeState.selectedEndDateMillis!!
                            )
                            showDateRangeDialog = false
                        },
                        enabled = confirmEnabled.value
                    ) {
                        Text("确定")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDateRangeDialog = false }) {
                        Text("取消")
                    }
                }

            ) {
                DateRangePicker(state = dateRangeState)
            }
        }
    }

}


