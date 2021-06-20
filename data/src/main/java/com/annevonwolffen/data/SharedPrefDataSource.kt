package com.annevonwolffen.data

import android.content.Context
import android.content.SharedPreferences
import com.annevonwolffen.domain.Task

class SharedPrefDataSource(context: Context) : DataSource {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(TASKS_PREFS, Context.MODE_PRIVATE)

    override fun getTasks(): List<Task> = emptyList()

    private companion object {
        const val TASKS_PREFS = "TASKS_PREFS"
    }
}