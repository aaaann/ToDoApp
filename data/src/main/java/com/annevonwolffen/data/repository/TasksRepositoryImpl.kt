package com.annevonwolffen.data.repository

import android.util.Log
import com.annevonwolffen.data.database.TasksDao
import com.annevonwolffen.data.database.toDb
import com.annevonwolffen.data.database.toDomain
import com.annevonwolffen.data.database.toServer
import com.annevonwolffen.data.remote.SynchronizeRequest
import com.annevonwolffen.data.remote.TasksService
import com.annevonwolffen.data.remote.toDb
import com.annevonwolffen.data.remote.toServer
import com.annevonwolffen.domain.Result
import com.annevonwolffen.domain.Task
import com.annevonwolffen.domain.TasksRepository
import retrofit2.Response

class TasksRepositoryImpl(
    private val tasksDao: TasksDao,
    private val tasksService: TasksService
) : TasksRepository {
    override suspend fun getAllTasks(): Result<List<Task>> {
        synchronize()
        handleRequest(
            request = { tasksService.getAllTasks() },
            onSuccess = { items ->
                tasksDao.insert(items.map { it.toDb() })
                // Обновляем удаленные на сервере, но не удаленные в базе
                tasksDao.updateDeleted(items.map { it.id })
            })

        return Result.Success(tasksDao.selectAll().map { it.toDomain() })
    }

    override suspend fun addTask(task: Task): Result<List<Task>> {
        tasksDao.insert(task.toDb())
        handleRequest(
            { tasksService.addTask(task.toServer()) },
            { tasksDao.insert(it.toDb()) },
            { tasksDao.insert(task.toDb().copy(isDirty = 1)) }
        )

        return getAllTasks() // TODO: think if needed to update on add
    }

    override suspend fun updateTask(task: Task): Result<List<Task>> {
        tasksDao.insert(task.toDb())
        handleRequest(
            { tasksService.updateTask(task.id, task.toServer()) },
            { tasksDao.insert(it.toDb()) },
            { tasksDao.insert(task.toDb().copy(isDirty = 1)) })

        return getAllTasks()
    }

    override suspend fun deleteTask(task: Task): Result<List<Task>> {
        tasksDao.insert(task.toDb().copy(isDeleted = 1))
        handleRequest(
            { tasksService.deleteTask(task.id) },
            { tasksDao.insert(it.toDb().copy(isDeleted = 1)) },
            { tasksDao.insert(task.toDb().copy(isDeleted = 1, isDirty = 1)) })

        return getAllTasks()
    }

    /**
     * Обновить данные на сервере
     * (переотправить таски из базы, по которым запрос на сервер ранее сфейлился)
     */
    private suspend fun synchronize() {
        tasksDao.selectDirty().groupBy { it.isDeleted }.let { items ->
            if (items.isNotEmpty()) {
                val request = SynchronizeRequest(
                    items[1]?.map { it.id } ?: emptyList(),
                    items[0]?.map { it.toServer() } ?: emptyList()
                )
                handleRequest(
                    { tasksService.synchronizeTasks(request) },
                    { tasksDao.updateDirty(items.values.flatten().map { it.id }) }
                )
            }
        }
    }

    private suspend fun <T> handleRequest(
        request: suspend () -> Response<T>,
        onSuccess: suspend (T) -> Unit = {},
        onError: suspend () -> Unit = {}
    ): Result<*> {
        return try {
            val response = request.invoke()
            if (response.isSuccessful) {
                response.body()?.let {
                    onSuccess.invoke(it)
                    Result.Success(it)
                } ?: Result.Error("$TAG: request returned empty body")
            } else {
                onError.invoke()
                Log.e(TAG, logErrorMessage("${response.code()} ${response.message()}"))
                Result.Error(logErrorMessage("${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            onError.invoke()
            Log.e(TAG, e.localizedMessage ?: e.toString())
            Result.Error(e.localizedMessage ?: e.toString())
        }
    }

    private fun logErrorMessage(errorMessage: String): String {
        return "Error occurred: $errorMessage"
    }

    private companion object {
        const val TAG = "TasksRepository"
    }
}