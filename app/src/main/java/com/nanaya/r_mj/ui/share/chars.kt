package com.nanaya.r_mj.ui.share

import androidx.annotation.ColorInt
import androidx.annotation.ColorLong
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextLayoutInput
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nanaya.r_mj.ui.theme.White87
import com.nanaya.r_mj.ui.theme.White97
import org.jetbrains.annotations.Range
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


data class RadarData(
    val label: String,
    val value: Double,
)

@Composable
fun RadarChart(
    modifier: Modifier = Modifier,
    data: List<RadarData>,
    circleColors: Array<Color>,
    radarColor: Color,
) {
    val numberOfAxes = data.size
    val angleIncrement = (2 * PI / numberOfAxes).toFloat()
    val labelMeasurers = Array(numberOfAxes) { rememberTextMeasurer() }

    var isShow by remember {
        mutableStateOf(false)
    }
//    val progress by animateFloatAsState(
//        targetValue = if (isShow) 1f else 0f,
//        animationSpec = tween(2000),
//        label = "radarAnim"
//    )
    val  progress=1f

    Modifier.onGloballyPositioned {
        isShow = it.boundsInRoot().top >= 0 && it.boundsInRoot().right >= 0
    }
    Canvas(modifier = modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val sideLen = size.minDimension
        val radius = sideLen / 2 * 0.6f
        drawCircle(Color.Black,radius, style=Stroke(width = 1.dp.toPx()))
        // 画几个圈,先画大圆
        for (i in circleColors.lastIndex downTo 0) {
            val color = circleColors[i]
            drawCircle(color, radius / circleColors.size * (i + 1))
        }
        val labelSpace =30.dp.toPx()

        // 绘制雷达图的轴,
        for (i in 0 until numberOfAxes) {
            val angle = (angleIncrement * i - PI / 2).toFloat()
            val x = centerX + radius * cos(angle)
            val y = centerY + radius * sin(angle)
            drawLine(Color.Gray, Offset(centerX, centerY), Offset(x, y))

            drawText(labelMeasurers[i], data[i].label,
                topLeft = when(i){
                    0->Offset(x-16.dp.toPx(), y-40.dp.toPx())
                    1-> Offset(x+12.dp.toPx(),y-20.dp.toPx())
                    2-> Offset(x,y)
                    3-> Offset(x-16.dp.toPx(),y)
                    4-> Offset(x-30.dp.toPx(),y)
                    else-> Offset(x-60.dp.toPx(),y-20.dp.toPx())
                })
        }

        // 绘制多边形
        val path = androidx.compose.ui.graphics.Path()
        for (index in 0 until numberOfAxes) {
            val radarData = data[index]
            val angle = angleIncrement * index - PI / 2
            val x = (centerX + (radarData.value) * radius * cos(angle)).toFloat() * progress
            val y = (centerY + (radarData.value) * radius * sin(angle)).toFloat() * progress

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        path.close()

        drawPath(path, radarColor.copy(alpha = 0.5f))

        // 绘制边界
        drawPath(path, radarColor, style = Stroke(2.dp.toPx()))
    }
}

@Preview(showBackground = true)
@Composable
private fun RadarChartPreview() {
    RadarChart(
        modifier = Modifier.fillMaxWidth(),
        data = listOf(
            RadarData("火力\n(一位均点)", 0.7),
            RadarData("防守\n(不飞率)",  0.3),
            RadarData("稳定\n(连对率)",  0.8),
            RadarData("运势\n(十战均顺)", 0.5),
            RadarData("技术\n(Rate)", 0.7),
            RadarData("进攻\n(一位率)", 0.7)
        ), arrayOf(White97, White87, White97, White87, White97), Color.Blue
    )

}
