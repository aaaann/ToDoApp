package com.annevonwolffen.todoapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.annevonwolffen.domain.Priority
import com.annevonwolffen.domain.Task

/**
 *
 */
class TasksViewModel : ViewModel(), TaskItemActionListener {
    val tasks: LiveData<List<Task>>
        get() = _tasks
    private val _tasks = MutableLiveData<List<Task>>(emptyList())

    fun loadTasks() {
        _tasks.value = listOf(
                Task(id = 0, title = "Закончить первую часть проекта ШМР", deadline = "27. июня 2021 23:59", priority = Priority.HIGH),
                Task(id = 1, title = "Работа: пофиксить баг с удалением задания", deadline = "21. июня 2021"),
                Task(id = 2, title = "ПЦР тест на коронавирус: Кутузовский пр., 32 к 1, вход со стороны улицы Кульнева, справа от первого входа в Сбер", deadline = "22. июня 2021 09:00", priority = Priority.LOW),
                Task(id = 3, title = "ПЦР тест на коронавирус: Кутузовский пр., 32 к 1, вход со стороны улицы Кульнева, справа от первого входа в Сбер", deadline = "22. июня 2021 09:00", priority = Priority.LOW),
                Task(id = 4, title = "ПЦР тест на коронавирус: Кутузовский пр., 32 к 1, вход со стороны улицы Кульнева, справа от первого входа в Сбер", deadline = "22. июня 2021 09:00", priority = Priority.LOW),
                Task(id = 5, title = "ПЦР тест на коронавирус: Кутузовский пр., 32 к 1, вход со стороны улицы Кульнева, справа от первого входа в Сбер", deadline = "22. июня 2021 09:00", priority = Priority.LOW),
                Task(id = 6, title = "ПЦР тест на коронавирус: Кутузовский пр., 32 к 1, вход со стороны улицы Кульнева, справа от первого входа в Сбер", deadline = "22. июня 2021 09:00", priority = Priority.LOW),
                Task(id = 7, title = "ПЦР тест на коронавирус: Кутузовский пр., 32 к 1, вход со стороны улицы Кульнева, справа от первого входа в Сбер", deadline = "22. июня 2021 09:00", priority = Priority.LOW),
                Task(id = 8, title = "ПЦР тест на коронавирус: Кутузовский пр., 32 к 1, вход со стороны улицы Кульнева, справа от первого входа в Сбер", deadline = "22. июня 2021 09:00", priority = Priority.LOW),
                Task(id = 9, title = "ПЦР тест на коронавирус: Кутузовский пр., 32 к 1, вход со стороны улицы Кульнева, справа от первого входа в Сбер", deadline = "22. июня 2021 09:00", priority = Priority.LOW),
                Task(id = 10, title = "ПЦР тест на коронавирус: Кутузовский пр., 32 к 1, вход со стороны улицы Кульнева, справа от первого входа в Сбер", deadline = "22. июня 2021 09:00", priority = Priority.LOW),
                Task(id = 11, title = "ПЦР тест на коронавирус: Кутузовский пр., 32 к 1, вход со стороны улицы Кульнева, справа от первого входа в Сбер", deadline = "22. июня 2021 09:00", priority = Priority.LOW),
                Task(id = 12, title = "ПЦР тест на коронавирус: Кутузовский пр., 32 к 1, вход со стороны улицы Кульнева, справа от первого входа в Сбер", deadline = "22. июня 2021 09:00", priority = Priority.LOW),
                Task(id = 13, title = "ПЦР тест на коронавирус: Кутузовский пр., 32 к 1, вход со стороны улицы Кульнева, справа от первого входа в Сбер", deadline = "22. июня 2021 09:00", priority = Priority.LOW),
                Task(id = 14, title = "ПЦР тест на коронавирус: Кутузовский пр., 32 к 1, вход со стороны улицы Кульнева, справа от первого входа в Сбер", deadline = "22. июня 2021 09:00", priority = Priority.LOW)
        )
    }

    override fun onDoneTask(id: Long) {
        _tasks.value = _tasks.value?.toMutableList()?.apply {
            val doneTask = firstOrNull { it.id == id }
            doneTask?.let { this[indexOf(it)] = it.copy(isDone = !it.isDone) }
        }
        // TODO: later send request to server
    }

    override fun onDeleteTask(id: Long) {
        _tasks.value = _tasks.value?.toMutableList()?.apply {
            remove(firstOrNull { it.id == id })
        }
    }
}