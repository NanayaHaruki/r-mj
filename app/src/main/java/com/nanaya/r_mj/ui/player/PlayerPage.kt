package com.nanaya.r_mj.ui.player

import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.blankj.utilcode.util.LogUtils
import com.nanaya.r_mj.ui.common.LoadMoreState
import com.nanaya.r_mj.ui.home.HomeAppbar
import com.nanaya.r_mj.ui.share.RadarChart
import com.nanaya.r_mj.ui.share.RadarData
import com.nanaya.r_mj.ui.share.SearchBar
import com.nanaya.r_mj.ui.theme.Pie1
import com.nanaya.r_mj.ui.theme.Pie2
import com.nanaya.r_mj.ui.theme.Pie3
import com.nanaya.r_mj.ui.theme.Pie4
import com.nanaya.r_mj.ui.theme.Primary
import com.nanaya.r_mj.ui.theme.White87
import com.nanaya.r_mj.ui.theme.White94
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.LineProperties
import ir.ehsannarmani.compose_charts.models.Pie
import ir.ehsannarmani.compose_charts.models.ZeroLineProperties
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.random.Random

@Composable
fun PlayerPage(
    viewModel: PlayerViewModel = hiltViewModel(), backNav: () -> Unit

) {
    val snackState = remember {
        SnackbarHostState()
    }
    LaunchedEffect(key1 = Unit) {
        Log.d("player", "snack collect start")
        viewModel.snackMessageFlow.collect {
            Log.d("player", "snack collect $it")
            if (it.isNotEmpty()) {
                snackState.showSnackbar(it)
            }
        }
    }
    Scaffold(
        topBar = {
            HomeAppbar(
                navClick = backNav
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackState) }
    ) { paddingValues ->
        val uiState by viewModel.state.collectAsStateWithLifecycle()
        PlayerMainPage(
            paddingValues = paddingValues, uiState = uiState, onSearch = viewModel::search,
            onPieClick = viewModel::clickPie,
            snackState = snackState
        )

    }
}

@Composable
fun PlayerMainPage(
    paddingValues: PaddingValues,
    uiState: PlayerUiState,
    onSearch: (String) -> Unit,
    onPieClick: (Pie) -> Unit,
    snackState: SnackbarHostState,
) {
    val scope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier.padding(
            start = paddingValues.calculateStartPadding(LayoutDirection.Ltr) + 8.dp,
            end = paddingValues.calculateEndPadding(LayoutDirection.Ltr) + 8.dp,
            top = paddingValues.calculateTopPadding() + 8.dp,
            bottom = paddingValues.calculateBottomPadding() + 8.dp,
        )
    ) {
        item {
            var searchValue by remember {
                mutableStateOf(TextFieldValue(""))
            }
            SearchBar(
                modifier = Modifier.padding(8.dp),
                searchQuery = searchValue,
                onSearchQueryChanged = { searchValue = it },
                onSearch = onSearch
            )
        }
        item {
            // 基本信息
            PlayerBaseInfo(uiState)
        }
        item {
            // 6个圈
            Circle6(uiState = uiState)
        }
        item {
            // 雷达图
            RadarChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(horizontal = 22.dp),
                data = listOf(
                    RadarData("火力\n(一位均点)", uiState.fire / 5.0),
                    RadarData("防守\n(不飞率)", uiState.defence / 5.0),
                    RadarData("稳定\n(连对率)", uiState.stabilize / 5.0),
                    RadarData("运势\n(十战均顺)", uiState.luck / 5.0),
                    RadarData("技术\n(Rate)", uiState.tech / 5.0),
                    RadarData("进攻\n(一位率)", uiState.attack / 5.0),
                ),
                circleColors = arrayOf(White94, White87, White94, White87, White94),
                radarColor = Color.Blue
            )
        }

        item {

            // 饼图
            Box {

                PieChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .rotate(270f)
                        .height((300 * 0.8).dp),
                    onPieClick = { pie ->
                        scope.launch {
                            val idx = uiState.pieData.indexOf(pie) + 1
                            val (cnt, position, point) = when (idx) {
                                1 -> Triple(uiState.sort1, "一", uiState.avgPoint1)
                                2 -> Triple(uiState.sort1, "二", uiState.avgPoint2)
                                3 -> Triple(uiState.sort1, "三", uiState.avgPoint3)
                                else -> Triple(uiState.sort1, "四", uiState.avgPoint4)
                            }
                            snackState.showSnackbar(
                                "%.2f%%, %d次%s位, 均点%d".format(
                                    pie.data * 100,
                                    cnt, position, point
                                )
                            )
                        }

                        onPieClick(pie)
                    },
                    selectedScale = 1.2f,
                    scaleAnimEnterSpec = spring<Float>(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    scaleAnimExitSpec = tween(300),
                    selectedPaddingDegree = 4f,
                    data = uiState.pieData,

                    style = Pie.Style.Stroke()
                )
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (i in 0..3) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(uiState.pieData[i].color)
                            )
                            Text("    ${i + 1}位")
                        }
                    }
                }
            }

        }

        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .background(Primary, RoundedCornerShape(8.dp))
                        .padding(4.dp), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "最近顺位(旧->新)",
                        color = Color.White,
                    )
                }

                Text(text = "顺位 ${uiState.recentSort}")

            }
        }
        if(uiState.recentPoint.isNotEmpty()) {
            item(key = uiState.recentPoint) {
                PlayerPositionLineChart(uiState.recentPoint)
            }
        }
    }
}

@Composable
private fun PlayerPositionLineChart(_positions: List<Double>) {

    val positions by remember {
        mutableStateOf(_positions)
    }
    LogUtils.d(System.identityHashCode(positions).toString()+","+System.identityHashCode(_positions))
    val mx = ceil(positions.max() / 10000) * 10000
    val mn = floor(positions.min() / 10000) * 10000
    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 22.dp),
        data = listOf(
            Line(
                label = "点数",
                values = positions.ifEmpty { listOf(0.0) },
                color = SolidColor(Primary),
                dotProperties = DotProperties(enabled = true, color = SolidColor(Primary))
            )
        ),
        animationMode = AnimationMode.Together(delayBuilder = { it * 500L }),
        dividerProperties = DividerProperties(enabled = true),
        zeroLineProperties = ZeroLineProperties(false),
        gridProperties = GridProperties(
            xAxisProperties = GridProperties.AxisProperties(enabled = true, lineCount = (mx - mn).toInt() / 10000),
            yAxisProperties = GridProperties.AxisProperties(enabled = false)
        ),
        maxValue = mx,
        minValue = mn,


        )
}

@Composable
private fun PlayerBaseInfo(uiState: PlayerUiState) {
    Row(
        modifier = Modifier.padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier.size(80.dp),
            model = uiState.avatar,
            contentDescription = null,
        )
        Column(modifier = Modifier.padding(8.dp)) {
            Row {
                Box(
                    modifier = Modifier
                        .background(Primary, RoundedCornerShape(8.dp))
                        .padding(4.dp)
                ) {
                    Text(uiState.rank, color = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .background(Primary, RoundedCornerShape(8.dp))
                        .padding(4.dp)
                ) {
                    Text("Rate#${uiState.rate}", color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .background(Primary, RoundedCornerShape(8.dp))
                    .padding(4.dp)
            ) {
                Text(uiState.rateName, color = Color.White)
            }
            Text(uiState.name)
            Row {
                Text("全国排行：${uiState.allRank}", modifier = Modifier.width(100.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text("雀庄排行：${uiState.rateRank}")
            }
            Row {
                Text("总对局数：${uiState.total}", modifier = Modifier.width(100.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text("飞人吃一：${uiState.flyEatOne}")
            }
        }
    }
}

@Composable
fun Circle6(
    uiState: PlayerUiState
) {
    val fontSize1 = 22.sp
    val fontSize2 = 16.sp
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val txt1 = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Black, fontSize = fontSize1)) {
                    append("${uiState.maxPoint}")
                }
                append(System.lineSeparator())
                withStyle(style = SpanStyle(color = Color.Black, fontSize = fontSize2)) {
                    append("最高点数")
                }
            }
            CircleContent(txt1)

            val txt2 = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Black, fontSize = fontSize1)) {
                    append("${uiState.avgPoint}")
                }
                append(System.lineSeparator())
                withStyle(style = SpanStyle(color = Color.Black, fontSize = fontSize2)) {
                    append("平均点数")
                }
            }
            CircleContent(txt2)

            val txt3 = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Black, fontSize = fontSize1)) {
                    append("${(uiState.beatPercent * 100).toInt()}%")
                }
                append(System.lineSeparator())
                withStyle(style = SpanStyle(color = Color.Black, fontSize = fontSize2)) {
                    append("打败雀士")
                }
            }
            CircleContent(txt3)

        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val txt1 = buildAnnotatedString {
                if (uiState.upRuleRound == null) {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black, fontSize = fontSize2
                        )
                    ) {
                        append("完成")
                    }
                } else {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black, fontSize = fontSize2
                        )
                    ) {
                        append("≥${uiState.upRuleRound}(")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = if (uiState.total >= uiState.upRuleRound) Color.Green
                            else Color.Red, fontSize = fontSize2
                        )
                    ) {
                        append(uiState.total.toString())
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black, fontSize = fontSize2
                        )
                    ) {
                        append(")")
                    }
                }
                append(System.lineSeparator())
                withStyle(style = SpanStyle(color = Color.Black, fontSize = fontSize2)) {
                    append("升段局数")
                }
            }
            CircleContent(txt1)

            val txt2 = buildAnnotatedString {
                if (uiState.upRuleAvg == null) {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black, fontSize = fontSize2
                        )
                    ) {
                        append("完成")
                    }
                } else {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black, fontSize = fontSize2
                        )
                    ) {
                        append("≤${uiState.upRuleAvg}(")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = if (uiState.upAvgPosition <= uiState.upRuleAvg) Color.Green
                            else Color.Red, fontSize = fontSize2
                        )
                    ) {
                        append("%.2f".format(uiState.upAvgPosition))
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black, fontSize = fontSize2
                        )
                    ) {
                        append(")")
                    }
                }
                append(System.lineSeparator())
                withStyle(style = SpanStyle(color = Color.Black, fontSize = fontSize2)) {
                    append("升段均顺")
                }
            }
            CircleContent(txt2)

            val txt3 = buildAnnotatedString {
                if (uiState.upRuleSumPosition == null) {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black, fontSize = fontSize2
                        )
                    ) {
                        append("完成")
                    }
                } else {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black, fontSize = fontSize2
                        )
                    ) {
                        append("≤${uiState.upRuleSumPosition}(")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = if (uiState.upSumPosition <= uiState.upRuleSumPosition) Color.Green
                            else Color.Red,
                            fontSize = fontSize2
                        )
                    ) {
                        append(uiState.upSumPosition.toString())
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black, fontSize = fontSize2
                        )
                    ) {
                        append(")")
                    }
                }
                append(System.lineSeparator())
                withStyle(style = SpanStyle(color = Color.Black, fontSize = fontSize2)) {
                    append("顺位之和")
                }
            }
            CircleContent(txt3)
        }
    }
}

@Composable
fun CircleContent(content: AnnotatedString) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .border(3.dp, Primary, CircleShape)
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .border(0.5.dp, Primary, CircleShape)
                .align(Alignment.Center)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(content, textAlign = TextAlign.Center)
            }
        }
    }
}

@Preview
@Composable
private fun CirclePreview() {
    CircleContent(AnnotatedString(""))
}


@Preview(heightDp = 150, widthDp = 360, showBackground = true)
@Composable
private fun PlayerPagePreview() {
    val state = PlayerUiState(
        avatar = "https://q.qlogo.cn/headimg_dl?dst_uin=null&spec=640&img_type=jpg",
        rank = "八段@2023-08-12",
        rankTime = "",
        rate = 1667,
        rateName = "鸭川雀庄(北京人民大学店)",
        name = "小巫巫饭",
        allRank = 728,
        rateRank = 2,
        total = 1379,
        flyEatOne = 76,
        maxPoint = 82700,
        avgPoint = 26300,
        beatPercent = 0.9856890112050324,
        upRuleRound = 50,
        upRuleAvg = 1.9,
        upRuleSumPosition = 95,
        upAvgPosition = 2.72,
        upSumPosition = 136,
        fire = 3.9392,
        defence = 4.028499999999999,
        stabilize = 5.0,
        luck = 5.0,
        tech = 5.0,
        attack = 5.0,
        ratio1 = 0.2704858593183466,
        sort1 = 373,
        avgPoint1 = 44696,
        ratio2 = 0.2675852066715011,
        sort2 = 369,
        avgPoint2 = 29933,
        ratio3 = 0.23422770123277736,
        sort3 = 323,
        avgPoint3 = 19335,
        ratio4 = 0.22770123277737492,
        sort4 = 314,
        avgPoint4 = 7419,
        recentSort = "22131 32322 33323 22333 31433",
        recentPoint = listOf(
            357.0,
            282.0,
            377.0,
            155.0,
            388.0,
            300.0,
            336.0,
            150.0,
            283.0,
            337.0,
            193.0,
            220.0,
            155.0,
            336.0,
            150.0,
            283.0,
            337.0,
            193.0,
            220.0,
            155.0,
            112.0,
            298.0,
            163.0,
            225.0,
            184.0
        ),
        sameTableRecordListData = PlayerUiState.SameTableRecordListData(
            isRefresh = false,
            loadMoreState = LoadMoreState.Ready,
            data = emptyList()
        ),
        playerRecordListData = PlayerUiState.PlayerRecordListData(
            isRefresh = false,
            loadMoreState = LoadMoreState.Ready,
            data = emptyList()
        ),
        pieData = listOf(
            Pie(
                "%.2f%%\n%d回一位\n均点%d".format(
                    0.2704858593183466, 373, 44696
                ),
                0.2704858593183466,
                color = Pie1,
                selectedColor = Pie1
            ),
            Pie(
                "%.2f%%\n%d回二位\n均点%d".format(
                    0.23422770123277736, 369, 29933
                ),
                0.23422770123277736,
                color = Pie2,
                selectedColor = Pie2
            ),
            Pie(
                "%.2f%%\n%d回三位\n均点%d".format(
                    0.23422770123277736, 323, 19335,
                ),
                0.23422770123277736,
                color = Pie3,
                selectedColor = Pie3
            ),
            Pie(
                "%.2f%%\n%d回四位\n均点%d".format(
                    0.22770123277737492, 314, 7419,
                ),
                0.22770123277737492,
                color = Pie4,
                selectedColor = Pie4
            ),
        )
    )
    PlayerMainPage(
        paddingValues = PaddingValues(8.dp),
        uiState = state,
        onSearch = {},
        onPieClick = {},
        snackState = SnackbarHostState()
    )
}

@Preview(showBackground = true)
@Composable
private fun PlayerLineChartPreview() {
    PlayerPositionLineChart(_positions = listOf(8400,23400,66700,12100,2400,-4300,3300,-8900).map { it.toDouble() })
}



