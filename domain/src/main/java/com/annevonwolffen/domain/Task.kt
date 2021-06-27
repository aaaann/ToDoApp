package com.annevonwolffen.domain

import java.util.Date

/**
 * Доменная модель задачи
 * @property
 */
data class Task(
    val id: Long = 0,
    val title: String,
    val deadline: Date? = null,
    val isDone: Boolean = false,
    val priority: Priority = Priority.UNDEFINED
)