package com.nanaya.r_mj.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nanaya.r_mj.data.local.model.MjSchoolDetail
import com.nanaya.r_mj.data.local.model.RemoteMonthRank
import com.nanaya.r_mj.ui.share.ISelectorNode
import com.nanaya.r_mj.ui.share.Spinner
import com.nanaya.r_mj.ui.theme.White97
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthRankPage(
    viewModel: MonthRankViewModel = hiltViewModel(),
    navToPlayer: (String) -> Unit,
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
                    top = paddingValues.calculateTopPadding() + 8.dp,
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
                LazyColumn(modifier = Modifier.padding(top = 50.dp)) {
                    items(uiState.monthRankList) { item: RemoteMonthRank ->
                        MonthRankListItem(item = item) {
                            navToPlayer(it)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }else{
                FilterMonthRank(
                    modifier = Modifier,
                    selectSchoolList = uiState.selectSchoolList,
                    allSchoolList = uiState.allSchoolList,
                    fastDateRangeIndex = uiState.fastDateRangeIndex,
                    startDate = uiState.startDate,
                    endDate = uiState.endDate,
                    onFastRangeSelected = viewModel::selectFastDateRange,
                    onDateRangeSelected = viewModel::selectDateRange,
                    count = uiState.count,
                    onCountChanged = viewModel::setTotalCnt,
                    onSearch = {
                        viewModel.search()
                        showFilter = false
                    },
                    onCancel = { showFilter = false },
                    onSelectSchoolDelete = viewModel::cancelSelectSchool,
                    onSelectSchoolConfirm = viewModel::selectSchool
                )
            }

        }
    }

}

@Composable
fun MonthRankListItem(
    item: RemoteMonthRank,
    onPlayerClick: (String) -> Unit,
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlayerClick(item.name) },
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        // 1,2,4
        val sort = item.sort.split(',')
        // 11,22,33  表示1位11次，4位33次，3位0次
        val sortCount = item.sortCount.split(',')
        val positions = IntArray(4)
        runCatching {
            sort.zip(sortCount).forEach {
                val s = it.first.toInt()
                val c = it.second.toInt()
                positions[s-1] = c
            }
        }
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("昵称：${item.name}")
                Text("雀庄：${item.rateName}")

            }
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("15连：${item.lsScore}")
                Text("成绩：${item.score}")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "均顺：${
                        if (item.avgSort.length > 4) item.avgSort.substring(
                            0,
                            4
                        ) else item.avgSort
                    } "
                )
                Text("均点：${item.avgPoint * 100}")
                Text("局数：${item.count}")
            }
            if (sort.size == sortCount.size) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    positions.forEachIndexed { index, i ->
                        Text("${index + 1}位：$i")
                    }
                }
            }

        }
    }
}

@Preview
@Composable
private fun MonthRankListItemPreview() {
    val remoteMonthRank = RemoteMonthRank(

    )
    MonthRankListItem(item = remoteMonthRank) {

    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterMonthRank(
    modifier: Modifier,
    selectSchoolList: List<MjSchoolDetail>,
    allSchoolList: List<MjSchoolDetail>,
    fastDateRangeIndex: Int,
    startDate: LocalDate,
    endDate: LocalDate,
    onFastRangeSelected: (Int) -> Unit,
    onDateRangeSelected: (LocalDate, LocalDate) -> Unit,
    count: Int,
    onCountChanged: (Int) -> Unit,
    onSearch: () -> Unit,
    onCancel: () -> Unit,
    onSelectSchoolDelete: (Int) -> Unit,
    onSelectSchoolConfirm: (MjSchoolDetail) -> Unit,
) {
    var showAllSchools by remember {
        mutableStateOf(false)
    }
    if (showAllSchools) {
        LazyColumn {
            items(allSchoolList) { detail ->
                Text("${detail.name}", modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSelectSchoolConfirm(detail)
                        showAllSchools = false
                    })
                HorizontalDivider()

            }
        }
    } else {
        Column(modifier = modifier.background(White97)) {
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { showAllSchools = true },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "请选择雀庄")
            }

            FlowRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                selectSchoolList.forEachIndexed { index, mjSchoolDetail ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(mjSchoolDetail.name ?: "-")
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = { onSelectSchoolDelete(index) }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                        }
                    }
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    fastDataRangeIndex: Int,
    startDate: LocalDate,
    endDate: LocalDate,
    onFastRangeSelected: (Int) -> Unit,
    onDateRangeSelected: (LocalDate, LocalDate) -> Unit
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
                initialSelectedStartDateMillis = startDate.toEpochDay() * 86400000,
                initialSelectedEndDateMillis = endDate.toEpochDay() * 86400000,
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
                                LocalDate.ofEpochDay(dateRangeState.selectedStartDateMillis!! / 86400000),
                                LocalDate.ofEpochDay(dateRangeState.selectedEndDateMillis!! / 86400000),
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
                DateRangePicker(state = dateRangeState,
                    dateFormatter = remember {
                        DatePickerDefaults.dateFormatter(
                            selectedDateSkeleton = "yyyy.MM.dd",

                            )
                    }
                )
            }
        }
    }

}


