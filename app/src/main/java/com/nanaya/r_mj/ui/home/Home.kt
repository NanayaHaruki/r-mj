package com.nanaya.r_mj.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.blankj.utilcode.util.LogUtils
import com.nanaya.r_mj.data.local.LocalMahjongSchool
import com.nanaya.r_mj.ui.common.MarkDown
import com.nanaya.r_mj.ui.theme.Area_East
import com.nanaya.r_mj.ui.theme.Area_West
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.withContext

@Composable
fun HomeContent(
    innerPadding: PaddingValues,
    areaLabels: List<String>,
    vm: HomeViewModel,
    uiState: UIState
) {
    val listState = rememberLazyListState()
    Box(modifier = Modifier.padding(innerPadding)) {
        Column {
            if (uiState.detail == null) {
                Areas(labels = areaLabels) { idx ->
                    vm.selectArea(areaLabels[idx])
                }
            }

            if (uiState.detail == null) {

                RmjSchoolList(
                    schools = uiState.schools,
                    listState=listState,
                    itemClick = { vm.navToDetail(it) })
            } else {
                RmjSchoolDetail(uiState.detail)
            }
        }
        if (uiState.isLoading) {
            Loading()
        }
    }

}


@Composable
fun Areas(
    labels: List<String>,
    onSelected: (Int) -> Unit
) {
    var selectIdx by remember { mutableIntStateOf(0) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Yellow)
    ) {
        labels.forEachIndexed { index, s ->

            Text(
                s, modifier = Modifier
                    .weight(1f)
                    .background(
                        if (selectIdx == index) Area_West else Area_East
                    )
                    .clickable {
                        selectIdx = index
                        onSelected(index)
                    }
                    .padding(4.dp),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                color = Color.White
            )

        }

    }
}

@Composable
fun Loading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RmjTopBar(
    title: String,
    showNavBtn: Boolean,
    backAction: () -> Unit,
    searchAction:(String)->Unit
    ) {
    var searchContent by remember {
        mutableStateOf<String?>(null)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    CenterAlignedTopAppBar(
        title = {
            if(searchContent==null) {
                Text(text = title, maxLines = 1)
            }else{
                TextField(value = searchContent?:"", onValueChange = {searchContent=it} , singleLine = true,
//                    modifier = Modifier.height(25.dp)
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search,
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            searchAction(searchContent!!)
                            keyboardController?.hide()
                        }
                    )
                )
            }
        },
        navigationIcon = {
            if (showNavBtn) {
                IconButton(onClick = backAction) {
                    Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, "back")
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Area_East,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White,
            navigationIconContentColor = Color.White
        ),
        actions = {
            IconButton(onClick = {
                if(searchContent==null) {
                    searchContent = ""
                }else {
                    searchAction(searchContent!!)
                    if (searchContent.isNullOrEmpty()){
                        searchContent=null
                        keyboardController?.hide()
                    }


                }


            }
            ) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "search")
            }
        }

    )
}

@Composable
fun RmjSchoolList(
    schools: List<LocalMahjongSchool>,
    listState: LazyListState = rememberLazyListState(),
    itemClick: (LocalMahjongSchool) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = listState
    ) {
        items(items = schools, key = { i -> i.cid }) { localRmjSchool ->
            SchoolListItem(localRmjSchool) {
                itemClick(localRmjSchool)
            }
        }
    }
}

@Composable
fun SchoolListItem(
    it: LocalMahjongSchool,
    onItemClick: (LocalMahjongSchool) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(4.dp)
            .clickable { onItemClick(it) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(it.name, fontSize = 18.sp, color = it.color)
        Spacer(modifier = Modifier.width(8.dp))
        Text("${it.numerator}/${it.denominator}")
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier

                .background(
                    color = it.color,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            Text(text = it.area, color = Color.White)
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(it.city)

    }
    HorizontalDivider()
}

@Composable
fun RmjSchoolDetail(
    detail: LocalMahjongSchool
) {
    var composeLines by remember {
        mutableStateOf<List<@Composable () -> Unit>>(emptyList())
    }
    LaunchedEffect(key1 = detail.rule) {
        composeLines =
            withContext(Dispatchers.Default) {
                parseMarkDown(detail.rule)
            }
    }
    LazyColumn(
        modifier = Modifier.padding(8.dp)
    ) {
        item {
            Text(detail.status)
        }
        item {
            Row {

                Text(detail.area)
                Spacer(modifier = Modifier.width(16.dp))
                Text(detail.city)
            }

        }
        item {
            Text("QQ群:${detail.qqGroup}")
        }

        composeLines.forEach { composeLine ->
            item { composeLine() }
        }


    }

}


private fun parseMarkDown(rule: String): List<@Composable () -> Unit> {
    LogUtils.d("parse mark down $rule")
    return rule.lines().map { line ->
        @Composable {
            when {
                line.startsWith("# ") -> {
                    Text(text = line.substring(2), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }

                line.startsWith("## ") -> {
                    Text(text = line.substring(3), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }

                line.startsWith("### ") -> {
                    Text(text = line.substring(4), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                line.startsWith("![") && line.contains("](") -> {
                    // 处理图片
                    val altText = line.substringAfter("![").substringBefore("]")
                    val url = line.substringAfter("](").substringBefore(")")
                    AsyncImage(model = url, contentDescription = "")
                }

                line.startsWith("**") && line.endsWith("**") -> {
                    Text(text = line.substring(2, line.length - 2), fontWeight = FontWeight.Bold)
                }

                line.startsWith("*") && line.endsWith("*") -> {
                    Text(
                        text = line.substring(1, line.length - 1),
                        textDecoration = TextDecoration.LineThrough
                    )
                }

                else -> {
                    Text(text = line)
                }
            }
        }
    }
}

