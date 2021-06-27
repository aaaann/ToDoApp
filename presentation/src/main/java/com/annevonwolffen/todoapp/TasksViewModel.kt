package com.annevonwolffen.todoapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.annevonwolffen.domain.Priority
import com.annevonwolffen.todoapp.model.TaskPresentationModel
import java.util.*

/**
 *
 */
class TasksViewModel : ViewModel(), TaskItemActionListener {
    val tasks: LiveData<List<TaskPresentationModel>>
        get() = _tasks
    private val _tasks = MutableLiveData<List<TaskPresentationModel>>(emptyList())

    fun loadTasks() {
        _tasks.value = listOf(
            TaskPresentationModel(
                id = 1,
                title = "Закончить первую часть проекта ШМР",
                deadline = Date(),
                priority = Priority.HIGH
            ),
            TaskPresentationModel(
                id = 2,
                title = "Работа: пофиксить баг с удалением задания",
                deadline = Date()
            ),
            TaskPresentationModel(
                id = 3,
                title = "ПЦР тест на коронавирус: Кутузовский пр., 32 к 1, вход со стороны улицы Кульнева, справа от первого входа в Сбер",
                deadline = Date(),
                priority = Priority.LOW
            ),
            TaskPresentationModel(
                id = 4,
                title = "ПЦР тест на коронавирус: Кутузовский пр., 32 к 1, вход со стороны улицы Кульнева, справа от первого входа в Сбер",
                priority = Priority.LOW
            ),
            TaskPresentationModel(
                id = 5,
                title = "ПЦР тест на коронавирус: Кутузовский пр., 32 к 1, вход со стороны улицы Кульнева, справа от первого входа в Сбер",
                priority = Priority.LOW
            ),
            TaskPresentationModel(
                id = 6,
                title = "ПЦР тест на коронавирус: Кутузовский пр., 32 к 1, вход со стороны улицы Кульнева, справа от первого входа в Сбер",
                priority = Priority.LOW
            ),
            TaskPresentationModel(
                id = 7,
                title = "ПЦР тест на коронавирус: Кутузовский пр., 32 к 1, вход со стороны улицы Кульнева, справа от первого входа в Сбер",
                priority = Priority.LOW
            ),
            TaskPresentationModel(
                id = 8,
                title = "ПЦР тест на коронавирус: Кутузовский пр., 32 к 1, вход со стороны улицы Кульнева, справа от первого входа в Сбер",
                priority = Priority.LOW
            ),
            TaskPresentationModel(
                id = 9,
                title = "ПЦР тест на коронавирус: Кутузовский пр., 32 к 1, вход со стороны улицы Кульнева, справа от первого входа в Сбер",
                priority = Priority.LOW
            ),
            TaskPresentationModel(
                id = 10,
                title = "ПЦР тест на коронавирус: Кутузовский пр., 32 к 1, вход со стороны улицы Кульнева, справа от первого входа в Сбер",
                priority = Priority.LOW
            ),
            TaskPresentationModel(
                id = 11,
                title = "ПЦР тест на коронавирус: Кутузовский пр., 32 к 1, вход со стороны улицы Кульнева, справа от первого входа в Сбер",
                priority = Priority.LOW
            ),
            TaskPresentationModel(
                id = 12,
                title = "ПЦР тест на коронавирус: Кутузовский пр., 32 к 1, вход со стороны улицы Кульнева, справа от первого входа в Сбер",
                priority = Priority.LOW
            ),
            TaskPresentationModel(
                id = 13,
                title = "ПЦР тест на коронавирус: Кутузовский пр., 32 к 1, вход со стороны улицы Кульнева, справа от первого входа в Сбер",
                priority = Priority.LOW
            ),
            TaskPresentationModel(
                id = 14,
                title = "ПЦР тест на коронавирус: Кутузовский пр., 32 к 1, вход со стороны улицы Кульнева, справа от первого входа в Сбер",
                priority = Priority.LOW
            ),
            TaskPresentationModel(
                id = 15,
                title = "ПЦР тест на коронавирус: Кутузовский пр., 32 к 1, вход со стороны улицы Кульнева, справа от первого входа в Сбер",
                priority = Priority.LOW
            )
        )
    }

    fun saveTask(task: TaskPresentationModel) {
        // TODO: send to server or db, add to list
        _tasks.value = _tasks.value?.toMutableList()?.apply {
            val oldTask = firstOrNull { it.id == task.id }
            oldTask?.let { this[indexOf(it)] = task }
                ?: add(task.copy(id = (this.size + 1).toLong()))
        }
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