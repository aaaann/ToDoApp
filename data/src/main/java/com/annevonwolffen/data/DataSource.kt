package com.annevonwolffen.data

import android.app.ActivityManager
import com.annevonwolffen.domain.Task

interface DataSource {
    fun getTasks(): List<Task>
}