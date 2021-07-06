package com.annevonwolffen.domain

class TasksInteractorImpl(private val repo: TasksRepository) : TasksInteractor {
    override suspend fun getAllTasks(): Result<List<Task>> = repo.getAllTasks()

    override suspend fun addTask(task: Task): Result<List<Task>> {
        val updateTime = getCurrentTime()
        return repo.addTask(task.copy(createdAt = updateTime, updatedAt = updateTime))
    }

    override suspend fun updateTask(task: Task): Result<List<Task>> =
        repo.updateTask(task.copy(updatedAt = getCurrentTime()))

    override suspend fun deleteTask(task: Task): Result<List<Task>> =
        repo.deleteTask(task.copy(updatedAt = getCurrentTime()))

    private fun getCurrentTime(): Long {
        return System.currentTimeMillis()
    }
}