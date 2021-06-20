package com.annevonwolffen.domain

/**
 * Доменная модель задачи
 * @property
 */
data class Task(
    val id: Long,
    val title: String,
    val deadline: String? = "",
    val isDone: Boolean = false,
    val priority: Priority = Priority.UNDEFINED
)