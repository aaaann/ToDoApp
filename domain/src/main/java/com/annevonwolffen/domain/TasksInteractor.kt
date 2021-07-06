package com.annevonwolffen.domain

interface TasksInteractor {
    suspend fun getAllTasks(): Result<List<Task>>
    suspend fun addTask(task: Task): Result<List<Task>>
    suspend fun updateTask(task: Task): Result<List<Task>>
    suspend fun deleteTask(task: Task): Result<List<Task>>
}