package com.alpermelkeli.livenotification.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.alpermelkeli.livenotification.notifications.navigation.NavigationNotification
import com.alpermelkeli.livenotification.notifications.navigation.enums.TrafficDensity
import com.alpermelkeli.livenotification.notifications.navigation.enums.TrafficState
import com.alpermelkeli.livenotification.notifications.transaction.TransactionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object LiveNotificationManager {
    private lateinit var notificationManager: NotificationManager
    private lateinit var appContext: Context
    const val CHANNEL_ID = "live_updates_channel_id"
    private const val CHANNEL_NAME = "live_updates_channel_name"
    private const val NOTIFICATION_ID = 1234

    fun initialize(context: Context, notificationManager: NotificationManager) {
        LiveNotificationManager.notificationManager = notificationManager
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_DEFAULT)
        appContext = context
        LiveNotificationManager.notificationManager.createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.BAKLAVA)
    fun start(notificationType: NotificationType) {
        val scope = CoroutineScope(Dispatchers.Main)
        when (notificationType) {
            NotificationType.TRANSACTION -> {
                for (state in TransactionState.entries) {
                    scope.launch {
                        delay(state.delay)
                        val notification = state.buildNotification(appContext).build()
                        notificationManager.notify(NOTIFICATION_ID, notification)
                    }
                }
            }
            NotificationType.NAVIGATION -> {

                val trafficStates = listOf(
                    TrafficState(TrafficDensity.HIGH, 0, 25),
                    TrafficState(TrafficDensity.HIGH, 25, 60),
                    TrafficState(TrafficDensity.MEDIUM, 60, 80),
                    TrafficState(TrafficDensity.LOW, 80, 100)
                )
                var progress = 0

                scope.launch {
                    for (i in 1..20){
                        delay(1000)
                        progress+=5
                        val notification = NavigationNotification(trafficStates, progress).buildNotification(appContext).build()
                        notificationManager.notify(NOTIFICATION_ID, notification)
                    }
                }
            }
        }
    }
}