package com.alpermelkeli.livenotification

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.alpermelkeli.livenotification.ui.theme.LiveNotificationTheme

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.BAKLAVA)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LiveNotificationTheme {
                TransactionNotificationSample()
            }
        }
    }
}