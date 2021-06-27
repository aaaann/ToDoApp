package com.annevonwolffen.todoapp.notification

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import androidx.work.OneTimeWorkRequestBuilder
import com.annevonwolffen.todoapp.model.TaskPresentationModel
import com.annevonwolffen.todoapp.utils.toCalendar
import java.lang.System.currentTimeMillis
import java.util.concurrent.TimeUnit

class NotificationHelper(private val context: Context) {

    fun scheduleNotificationsForTasks(tasks: List<TaskPresentationModel>) {
        val grouping = tasks.groupingBy { it.deadline }.eachCount()
        grouping.forEach { (date, tasksNumber) ->
            val data = Data.Builder().putInt(NOTIFICATION_DATA_TASKS_NUMBER, tasksNumber).build()
            val notifyTime =
                date?.toCalendar(NOTIFY_HOUR, NOTIFY_MINUTE, NOTIFY_SECOND)?.timeInMillis
            val currentTime = currentTimeMillis()
            notifyTime?.let {
                if (notifyTime > currentTime) {
                    scheduleNotification(it - currentTime, data)
                }
            }
        }
    }

    private fun scheduleNotification(delay: Long, data: Data) {
        val notificationWork = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()
        WorkManager
            .getInstance(context)
            .beginUniqueWork(NOTIFICATION_WORK, ExistingWorkPolicy.REPLACE, notificationWork)
            .enqueue()
    }

    companion object {
        const val NOTIFICATION_DATA_TASKS_NUMBER = "NOTIFICATION_DATA_TASKS_NUMBER"
        const val NOTIFICATION_WORK = "ToDoApp_notification_work"
        const val NOTIFY_HOUR = 7 // TODO: take from user settings
        const val NOTIFY_MINUTE = 0
        const val NOTIFY_SECOND = 0
    }
}