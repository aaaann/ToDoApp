package com.annevonwolffen.todoapp

interface TaskItemActionListener {
    fun onDoneTask(id: Long)
    fun onDeleteTask(id: Long)
}