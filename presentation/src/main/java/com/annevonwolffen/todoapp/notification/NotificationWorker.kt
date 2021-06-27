package com.annevonwolffen.todoapp.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION
import android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.annevonwolffen.todoapp.R
import com.annevonwolffen.todoapp.TasksActivity
import com.annevonwolffen.todoapp.notification.NotificationHelper.Companion.NOTIFICATION_DATA_TASKS_NUMBER

class NotificationWorker(private val appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    override fun doWork(): Result {
        sendNotification(inputData.getInt(NOTIFICATION_DATA_TASKS_NUMBER, DEFAULT_TASKS_NUMBER))
        return Result.success()
    }

    private fun sendNotification(tasksNumber: Int) {
        val intent = Intent(appContext, TasksActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(appContext, 0, intent, 0)

        val manager: NotificationManager =
            appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = createChannel()
            manager.createNotificationChannel(notificationChannel)
        }

        val notification = createNotificationBuilder(pendingIntent, tasksNumber)
        manager.notify(1, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(): NotificationChannel {
        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL,
            appContext.getString(R.string.app_name),
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = ContextCompat.getColor(appContext, R.color.colorBlue)
        notificationChannel.enableVibration(true)
        notificationChannel.vibrationPattern =
            longArrayOf(0, 100, 200, 300)
        notificationChannel.setSound(
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
            AudioAttributes.Builder().setUsage(USAGE_NOTIFICATION_RINGTONE)
                .setContentType(CONTENT_TYPE_SONIFICATION).build()
        )
        return notificationChannel
    }

    private fun createNotificationBuilder(
        pendingIntent: PendingIntent,
        tasksNumber: Int
    ): Notification {
        val notificationBuilder = NotificationCompat.Builder(appContext, NOTIFICATION_CHANNEL)
            .setChannelId(NOTIFICATION_CHANNEL)
            .setContentIntent(pendingIntent)
            .setContentTitle(
                appContext.getString(
                    R.string.notification_title,
                    appContext.resources.getQuantityString(
                        R.plurals.notification_task_count,
                        tasksNumber,
                        tasksNumber
                    )
                )
            )
            .setContentText(appContext.getString(R.string.notification_text))
            .setSmallIcon(R.drawable.ic_check_24dp)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
            .setVibrate(longArrayOf(0, 100, 200, 300))
            .setLights(ContextCompat.getColor(appContext, R.color.colorBlue), LED_ON_MS, LED_OFF_MS)
        return notificationBuilder.build()
    }

    companion object {
        private const val DEFAULT_TASKS_NUMBER = 1
        private const val NOTIFICATION_CHANNEL = "ToDoApp_channel_1"
        private const val LED_ON_MS = 1
        private const val LED_OFF_MS = 1

    }
}