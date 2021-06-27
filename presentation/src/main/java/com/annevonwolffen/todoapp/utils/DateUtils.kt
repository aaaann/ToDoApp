package com.annevonwolffen.todoapp.utils

import java.util.Date
import java.util.Calendar

fun Date.toCalendar(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar
}

fun Calendar.toDate(): Date = time
