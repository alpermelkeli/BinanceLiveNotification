package com.alpermelkeli.livenotification.notifications.transaction

import com.alpermelkeli.livenotification.R
import android.app.Notification
import android.app.Notification.ProgressStyle
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.IconCompat
import com.alpermelkeli.livenotification.notifications.LiveNotificationManager

enum class TransactionState(val delay: Long) {
    INITIALIZING(5000) {
        @RequiresApi(Build.VERSION_CODES.BAKLAVA)
        override fun buildNotification(context: Context): Notification.Builder {
            return buildBaseNotification(context, INITIALIZING)
                .setContentTitle("You transfer request has been received")
                .setContentText("Transfer waiting for confirmation")
                .setLargeIcon(
                    IconCompat.createWithResource(
                        context, R.drawable.ic_binance
                    ).toIcon(context)
                )
                .setStyle(buildBaseProgressStyle(context, INITIALIZING))
        }
    },
    TRANSACTION_STARTED(9000) {
        @RequiresApi(Build.VERSION_CODES.BAKLAVA)
        override fun buildNotification(context: Context): Notification.Builder {
            return buildBaseNotification(context, TRANSACTION_STARTED)
                .setContentTitle("Your ETH transaction has been started")
                .setContentText("Waiting for confirmation")
                .setLargeIcon(
                    IconCompat.createWithResource(
                        context, R.drawable.ic_binance
                    ).toIcon(context)
                )
                .setStyle(buildBaseProgressStyle(context, TRANSACTION_STARTED))
        }
    },
    TRANSACTION_CONFIRMED(13000) {
        @RequiresApi(Build.VERSION_CODES.BAKLAVA)
        override fun buildNotification(context: Context): Notification.Builder {
            return buildBaseNotification(context, TRANSACTION_CONFIRMED)
                .setContentTitle("Your ETH transaction has been confirmed")
                .setContentText("Waiting for completion")
                .setStyle(
                    buildBaseProgressStyle(context, TRANSACTION_CONFIRMED)
                )
                .setLargeIcon(
                    IconCompat.createWithResource(
                        context, R.drawable.ic_binance
                    ).toIcon(context)
                )
        }
    },
    TRANSACTION_COMPLETED(18000) {
        @RequiresApi(Build.VERSION_CODES.BAKLAVA)
        override fun buildNotification(context: Context): Notification.Builder {
            return buildBaseNotification(context, TRANSACTION_COMPLETED)
                .setContentTitle("Your ETH transaction has been completed")
                .setContentText("You can now use your funds")
                .setStyle(
                    buildBaseProgressStyle(context, TRANSACTION_COMPLETED)
                )
                .setLargeIcon(
                    IconCompat.createWithResource(
                        context, R.drawable.ic_binance
                    ).toIcon(context)
                )
        }
    };

    @RequiresApi(Build.VERSION_CODES.BAKLAVA)
    protected fun buildBaseProgressStyle(context: Context, transactionState: TransactionState): ProgressStyle {
        val progressStyle = ProgressStyle()
        val progressPointsList: MutableList<ProgressStyle.Point> = mutableListOf()
        val progressSegmentsList: MutableList<ProgressStyle.Segment> = mutableListOf()

        initializeProgressPointsAndSegments(progressPointsList, progressSegmentsList)

        // Set the progress tracker icon based on the transaction state
        when (transactionState) {
            INITIALIZING -> {
                highlightProgressUpTo(0, progressPointsList, progressSegmentsList)
                progressStyle
                    .setProgressPoints(progressPointsList)
                    .setProgressSegments(progressSegmentsList)
                    .setProgressTrackerIcon(
                        IconCompat.createWithResource(
                            context, R.drawable.ic_etherium
                        ).toIcon(context)
                    ).setProgress(25)
            }
            TRANSACTION_STARTED -> {
                highlightProgressUpTo(1, progressPointsList, progressSegmentsList)
                progressStyle.setProgressPoints(progressPointsList)
                    .setProgressSegments(progressSegmentsList)
                    .setProgressTrackerIcon(
                        IconCompat.createWithResource(
                            context, R.drawable.ic_etherium
                        ).toIcon(context)
                    ).setProgress(50)
            }
            TRANSACTION_CONFIRMED -> {
                highlightProgressUpTo(2, progressPointsList, progressSegmentsList)
                progressStyle.setProgressPoints(progressPointsList)
                    .setProgressSegments(progressSegmentsList)
                    .setProgressTrackerIcon(
                        IconCompat.createWithResource(
                            context, R.drawable.ic_etherium
                        ).toIcon(context)
                    ).setProgress(75)
            }
            TRANSACTION_COMPLETED -> {
                highlightProgressUpTo(3, progressPointsList, progressSegmentsList)
                progressStyle
                    .setProgressPoints(progressPointsList)
                    .setProgressSegments(progressSegmentsList)
                    .setProgressTrackerIcon(
                        IconCompat.createWithResource(
                            context, R.drawable.ic_etherium
                        ).toIcon(context)
                    ).setProgress(100)
            }
        }
        return progressStyle
    }

    @RequiresApi(Build.VERSION_CODES.BAKLAVA)
    private fun highlightProgressUpTo(
        index: Int,
        points: MutableList<ProgressStyle.Point>,
        segments: MutableList<ProgressStyle.Segment>
    ) {
        val completedColor = Color.GREEN
        val fullyTransparent = Color.parseColor("#FF627EEA")

        for (i in 0 until index) {
            points[i].setColor(completedColor)
        }

        points[index].setColor(fullyTransparent)

        for (i in 0..index) {
            segments[i].setColor(completedColor)
        }
    }

    @RequiresApi(Build.VERSION_CODES.BAKLAVA)
    private fun initializeProgressPointsAndSegments(
        progressPointsList: MutableList<ProgressStyle.Point>,
        progressSegmentsList: MutableList<ProgressStyle.Segment>
    ) {
        val pointColor = Color.BLACK
        val segmentColor = Color.BLACK

        /*First segment*/
        progressSegmentsList.add(ProgressStyle.Segment(25).setColor(segmentColor))
        /*First point*/
        progressPointsList.add(ProgressStyle.Point(25).setColor(pointColor))
        /*Second segment*/
        progressSegmentsList.add(ProgressStyle.Segment(25).setColor(segmentColor))
        /*Second point*/
        progressPointsList.add(ProgressStyle.Point(50).setColor(pointColor))
        /*Third segment*/
        progressSegmentsList.add(ProgressStyle.Segment(25).setColor(segmentColor))
        /*Third point*/
        progressPointsList.add(ProgressStyle.Point(75).setColor(pointColor))
        /*Fourth segment*/
        progressSegmentsList.add(ProgressStyle.Segment(25).setColor(segmentColor))
        /*Fourth point*/
        progressPointsList.add(ProgressStyle.Point(100).setColor(pointColor))
    }

    protected fun buildBaseNotification(context: Context, transactionState: TransactionState): Notification.Builder {
        val notificationBuilder = Notification.Builder(context, LiveNotificationManager.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_binance)
            .setColor(Color.BLACK)
            .setOngoing(true)
            .setColorized(true)

        when (transactionState) {
            INITIALIZING -> {}
            TRANSACTION_STARTED -> {}
            TRANSACTION_CONFIRMED -> {}
            TRANSACTION_COMPLETED ->
                notificationBuilder
                    .addAction(
                        Notification.Action.Builder(null, "Review Transaction", null).build()
                    )
                    .addAction(
                        Notification.Action.Builder(null, "Close", null).build()
                    )
        }
        return notificationBuilder
    }

    abstract fun buildNotification(context: Context): Notification.Builder
}