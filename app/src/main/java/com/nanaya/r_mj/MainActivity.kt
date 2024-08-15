package com.nanaya.r_mj

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nanaya.r_mj.ui.RmjApp
import com.nanaya.r_mj.ui.theme.RmjTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val appContext  = applicationContext as RmjApplication
        setContent {
            RmjTheme {
                RmjApp(appContext.appContainer)
            }
        }
    }
}
