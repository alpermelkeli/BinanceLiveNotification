package com.alpermelkeli.livenotification

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.alpermelkeli.livenotification.notifications.LiveNotificationManager
import com.alpermelkeli.livenotification.notifications.NotificationType
import kotlinx.coroutines.launch

@SuppressLint("InlinedApi")
@Composable
fun TransactionNotificationSample() {
    val context = LocalContext.current

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    LiveNotificationManager.initialize(context.applicationContext, notificationManager)

    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true // We are working on android 16.
            }
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
    }

    Scaffold(
        containerColor = Color.Black,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },

    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Live Notifications With Android 16!",
                color = Color.White
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(0.8f).height(60.dp),
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = Color.White
                    ),
                    shape = ShapeDefaults.Medium,
                    onClick = {
                        if (hasNotificationPermission) {
                            onTransactionStarted()
                            scope.launch {
                                snackbarHostState.showSnackbar("Transaction notification sent!")
                            }
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Please allow notification permission to send transaction notifications.")
                            }
                            permissionLauncher.launch(
                                Manifest.permission.POST_NOTIFICATIONS
                            )
                        }
                    }
                ) {
                    Text("Start Transaction Notification",
                        color = Color.Black)
                }

                Spacer(Modifier.height(20.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(0.8f).height(60.dp),
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = Color.White
                    ),
                    shape = ShapeDefaults.Medium,
                    onClick = {
                        if (hasNotificationPermission) {
                            onNavigationStarted()
                            scope.launch {
                                snackbarHostState.showSnackbar("Navigation notification started!")
                            }
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Please allow notification permission to send transaction notifications.")
                            }
                            permissionLauncher.launch(
                                Manifest.permission.POST_NOTIFICATIONS
                            )
                        }
                    }
                ) {
                    Text("Start Navigation Notification",
                        color = Color.Black)
                }
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.BAKLAVA)
fun onTransactionStarted() {
    LiveNotificationManager.start(notificationType = NotificationType.TRANSACTION)
}

@RequiresApi(Build.VERSION_CODES.BAKLAVA)
fun onNavigationStarted(){
    LiveNotificationManager.start(notificationType = NotificationType.NAVIGATION)
}