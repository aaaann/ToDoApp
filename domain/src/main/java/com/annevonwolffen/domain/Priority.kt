package com.annevonwolffen.domain

enum class Priority(val value: Int, val label: String) {
    UNDEFINED(0, "Нет"),
    LOW(1, "Низкий"),
    HIGH(2, "Высокий")
}