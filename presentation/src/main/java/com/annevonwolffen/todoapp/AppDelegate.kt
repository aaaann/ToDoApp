package com.annevonwolffen.todoapp

import android.app.Application
import com.annevonwolffen.data.SharedPrefSettingsStorage
import com.annevonwolffen.domain.settings.SettingsInteractor
import com.annevonwolffen.domain.settings.SettingsInteractorImpl
import com.annevonwolffen.todoapp.notification.NotificationHelper

class AppDelegate : Application() {
    val settingsInteractor: SettingsInteractor by lazy {
        val settingsStorage = SharedPrefSettingsStorage(applicationContext)
        SettingsInteractorImpl(settingsStorage)
    }

    val notificationHelper: NotificationHelper by lazy {
        NotificationHelper(applicationContext)
    }
}