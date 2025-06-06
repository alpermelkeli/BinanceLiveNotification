package com.alpermelkeli.livenotification.notifications.navigation

import android.app.Notification
import android.app.Notification.ProgressStyle
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.IconCompat
import com.alpermelkeli.livenotification.R
import com.alpermelkeli.livenotification.notifications.LiveNotificationManager
import com.alpermelkeli.livenotification.notifications.navigation.enums.TrafficState

class NavigationNotification(
    private val trafficStates: List<TrafficState>,
    private val currentProgress: Int
) {

    @RequiresApi(Build.VERSION_CODES.BAKLAVA)
    fun buildNotification(context: Context): Notification.Builder {
        return buildBaseNotification(context)
            .setContentTitle("Navigation in Progress")
            .setContentText("Current progress: ${currentProgress}%")
            .setStyle(buildNavigationProgressStyle(context))
    }

    @RequiresApi(Build.VERSION_CODES.BAKLAVA)
    private fun buildNavigationProgressStyle(context: Context): ProgressStyle {
        val progressStyle = ProgressStyle()
        val progressSegmentsList: MutableList<ProgressStyle.Segment> = mutableListOf()

        initializeProgressFromTrafficStates(progressSegmentsList)

        progressStyle
            .setProgressSegments(progressSegmentsList)
            .setProgressTrackerIcon(
                IconCompat.createWithResource(
                    context, R.drawable.ic_navigation
                ).toIcon(context)
            )
            .setProgress(currentProgress)

        return progressStyle
    }

    @RequiresApi(Build.VERSION_CODES.BAKLAVA)
    private fun initializeProgressFromTrafficStates(
        progressSegmentsList: MutableList<ProgressStyle.Segment>
    ) {
        if (trafficStates.isEmpty()) {
            progressSegmentsList.add(ProgressStyle.Segment(100).setColor(Color.GRAY))
            return
        }

        var previousLength = 0

        for (trafficState in trafficStates) {

            if (currentProgress > trafficState.to){
                previousLength += trafficState.to - trafficState.from
                continue
            }

            val segmentLength = trafficState.to - trafficState.from
            val inCurrentProgress = trafficState.from <= currentProgress


            if (inCurrentProgress) {

                val completedLength = currentProgress - trafficState.from
                val remainingLength = trafficState.to - currentProgress

                if (completedLength > 0) {
                    progressSegmentsList.add(
                        ProgressStyle.Segment(completedLength + previousLength).setColor(Color.GRAY)
                    )
                }

                if (remainingLength > 0) {
                    progressSegmentsList.add(
                        ProgressStyle.Segment(remainingLength).setColor(trafficState.density.color)
                    )
                }

            } else  {
                progressSegmentsList.add(
                    ProgressStyle.Segment(segmentLength).setColor(trafficState.density.color)
                )
            }
        }

    }


    private fun buildBaseNotification(context: Context): Notification.Builder {
        val notificationBuilder = Notification.Builder(context, LiveNotificationManager.CHANNEL_ID)
            .setSmallIcon(R.drawable.android_16)
            .setLargeIcon(
                IconCompat.createWithResource(
                    context, R.drawable.ic_mercedes
                ).toIcon(context)
            )
            .setSound(null, null)
            .setColor(Color.BLACK)
            .setOngoing(true)
            .setColorized(true)

        if (currentProgress >= 100) {
            notificationBuilder
                .addAction(
                    Notification.Action.Builder(null, "View Route Summary", null).build()
                )
                .addAction(
                    Notification.Action.Builder(null, "Close Navigation", null).build()
                )
        } else {
            notificationBuilder
                .addAction(
                    Notification.Action.Builder(null, "Cancel Navigation", null).build()
                )
        }

        return notificationBuilder
    }
}
