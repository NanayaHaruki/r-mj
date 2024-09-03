package com.nanaya.r_mj.ui.home

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nanaya.r_mj.data.local.model.MjSchoolDetail
import com.nanaya.r_mj.ui.theme.CurrentIndicatorColor
import com.nanaya.r_mj.ui.theme.NormalIndicatorColor
import kotlinx.coroutines.delay

@Composable
fun Banner(
    urls: List<String>
) {
    Log.d("detail", "banner:$urls")
    val pageState = rememberPagerState { urls.size }
    LaunchedEffect(key1 = urls.firstOrNull()) {
        while (true) {
            delay(2000)
            pageState.animateScrollToPage(if (pageState.currentPage == urls.lastIndex) 0 else pageState.currentPage + 1)
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(state = pageState, modifier = Modifier
            .fillMaxWidth()
            .height(250.dp), key = { idx -> urls[idx] }
        ) { idx ->
            AsyncImage(
                model = urls[idx],
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Inside
            )

        }
        Row(modifier = Modifier.align(Alignment.BottomEnd)) {
            for (i in urls.indices) {
                Canvas(
                    modifier = Modifier
                        .padding(2.dp)
                        .size(5.dp)
                ) {
                    drawCircle(
                        color = if (i == pageState.currentPage) CurrentIndicatorColor else NormalIndicatorColor
                    )
                }
            }
        }
    }
}

@Composable
fun MjSchoolDetailIntroduce(
    detail: MjSchoolDetail
) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Banner(
            urls = detail.picList
        )
        IntroduceBaseInfo(txt1 = "联系人", txt2 = detail.contact ?: "-")
        IntroduceBaseInfo(txt1 = "电话", txt2 = detail.mobile ?: "-")
        IntroduceBaseInfo(txt1 = "QQ", txt2 = detail.adminQQ ?: "")
        IntroduceBaseInfo(txt1 = "QQ群", txt2 = detail.qqGroup ?: "")
        IntroduceBaseInfo(txt1 = "微信", txt2 = detail.wechat ?: "")
        IntroduceBaseInfo(txt1 = "地址", txt2 = detail.address ?: "")
        Text(detail.rule ?: "")
    }
}

@Composable
private fun IntroduceBaseInfo(txt1: String, txt2: String) {
    Row {
        Text(txt1, modifier = Modifier.width(100.dp))
        Text(txt2)
    }
}