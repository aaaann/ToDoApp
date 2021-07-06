package com.annevonwolffen.todoapp

import android.app.Application
import com.annevonwolffen.todoapp.di.AppComponent
import com.annevonwolffen.todoapp.di.DaggerAppComponent

class AppDelegate : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}