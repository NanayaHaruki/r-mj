package com.nanaya.r_mj.ui.share

import android.graphics.drawable.shapes.RoundRectShape
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


interface ISelectorNode<out T> {
    fun text(): String
    fun value(): T
}

@Composable
fun Spinner(
    modifier: Modifier,
    selectedIndex: Int,
    items: List<ISelectorNode<String>>?,
    onItemSelect: (Int) -> Unit,
) {
    if (items.isNullOrEmpty()) {
        Text("no data")
    } else {

        var expanded by remember { mutableStateOf(false) }
        var boxWidth by remember {
            mutableIntStateOf(0)
        }
        Box(
            modifier = modifier
                .onGloballyPositioned { boxWidth = it.size.width }
                .border(1.dp, Color.DarkGray, RoundedCornerShape(8.dp))
                .clickable { expanded = true }
                .padding(8.dp)
        ) {
            Text(
                text = items.getOrNull(selectedIndex)?.text().let {
                    if (it.isNullOrEmpty()) {
                        Log.d("pop", "$selectedIndex $items")
                        "-"
                    } else it
                },
                modifier = Modifier.align(Alignment.Center),
                fontSize = 18.sp,
                maxLines = 1
            )
            androidx.compose.material3.DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(with(LocalDensity.current) { boxWidth.toDp() })
            ) {
                items.forEachIndexed { index, item ->
                    DropdownMenuItem(
                        text = {
                            Box(

                            ) {
                                Text(text = item.text(), fontSize = 18.sp)
                            }
                        },
                        onClick = {
                            expanded = false
                            onItemSelect(index)
                        }
                    )
                }
            }
        }
    }
}