package com.nanaya.r_mj.ui.share

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Loading(modifier: Modifier = Modifier) {
    Surface(modifier = modifier) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            CircularProgressIndicator(
                Modifier.align(Alignment.Center)
            )
        }
    }
}