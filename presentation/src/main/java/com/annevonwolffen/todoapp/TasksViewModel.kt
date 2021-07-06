package com.annevonwolffen.todoapp

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.annevonwolffen.domain.Task
import com.annevonwolffen.domain.TasksInteractor
import com.annevonwolffen.domain.handle
import com.annevonwolffen.domain.settings.SettingsInteractor
import com.annevonwolffen.todoapp.model.TaskPresentationModel
import com.annevonwolffen.todoapp.model.mapFromDomain
import com.annevonwolffen.todoapp.model.mapToDomain
import com.annevonwolffen.todoapp.utils.CoroutineDispatchers
import com.annevonwolffen.todoapp.utils.map
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.UUID
import javax.inject.Inject

/**
 *
 */
class TasksViewModel @Inject constructor(
    private val settingsInteractor: SettingsInteractor,
    private val tasksInteractor: TasksInteractor,
    private val coroutineDispatchers: CoroutineDispatchers
) :
    ViewModel(),
    TaskItemActionListener {
    val tasks: LiveData<List<TaskPresentationModel>>
        get() = (_tasks to _isDoneTasksShown).map { tasks, doneShown ->
            val list = doneShown?.takeIf { shown -> !shown }?.let {
                tasks?.filter { task -> !task.isDone }
            } ?: tasks
            list.orEmpty()
                .sortedWith(compareBy<TaskPresentationModel, Date?>(nullsLast()) { t -> t.deadline }
                    .thenByDescending { t -> t.priority.value })
        }
    val isDoneTasksShown: LiveData<Boolean>
        get() = _isDoneTasksShown
    val doneTasksCount: LiveData<Int>
        get() = _doneTasksCount
    private val _tasks = MutableLiveData<List<TaskPresentationModel>>(emptyList())
    private val _isDoneTasksShown = MutableLiveData<Boolean>(false)
    private val _doneTasksCount = MediatorLiveData<Int>().apply {
        addSource(_tasks) {
            value = it.count { task -> task.isDone }
        }
    }
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading // TODO: add shimmers

    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable -> onError(throwable.localizedMessage) }

    init {
        _isDoneTasksShown.value = settingsInteractor.doneTasksVisibility()
    }

    fun loadTasks() {
        viewModelScope.launch(exceptionHandler) {
            _isLoading.value = true
            val result = withContext(coroutineDispatchers.ioDispatcher) {
                tasksInteractor.getAllTasks()
            }
            result.handle(::onSuccess, ::onError)
        }
    }

    fun saveTask(task: TaskPresentationModel) {
        val taskDomain = task.copy(id = task.id ?: UUID.randomUUID().toString()).mapToDomain()
        viewModelScope.launch(exceptionHandler) {
            _isLoading.value = true
            val result = withContext(coroutineDispatchers.ioDispatcher) {
                task.id?.let { tasksInteractor.updateTask(taskDomain) }
                    ?: tasksInteractor.addTask(taskDomain)
            }
            result.handle(::onSuccess, ::onError)
        }
    }

    fun onDoneTasksToggleClick() {
        val isDoneTasksVisible = settingsInteractor.doneTasksVisibility()
        settingsInteractor.setDoneTasksVisibility(!isDoneTasksVisible)
        _isDoneTasksShown.value = !isDoneTasksVisible
    }

    override fun onDoneTask(task: TaskPresentationModel) {
        _tasks.value = _tasks.value?.toMutableList()?.apply {
            val doneTask = firstOrNull { it.id == task.id }
            doneTask?.let { this[indexOf(it)] = it.copy(isDone = !it.isDone) }
        }
        viewModelScope.launch(exceptionHandler) {
            val result = withContext(coroutineDispatchers.ioDispatcher) {
                tasksInteractor.updateTask(task.copy(isDone = !task.isDone).mapToDomain())
            }
            result.handle(::onSuccess, ::onError)
        }
    }

    override fun onDeleteTask(task: TaskPresentationModel) {
        _tasks.value = _tasks.value?.toMutableList()?.apply {
            remove(firstOrNull { it.id == task.id })
        }
        viewModelScope.launch(exceptionHandler) {
            val result = withContext(coroutineDispatchers.ioDispatcher) {
                tasksInteractor.deleteTask(task.mapToDomain())
            }
            result.handle(::onSuccess, ::onError)
        }
    }

    private fun onSuccess(tasks: List<Task>) {
        _isLoading.value = false
        _tasks.value = tasks.map { task -> task.mapFromDomain() }
    }

    private fun onError(errorMessage: String?) {
        _isLoading.value = false
        Log.e(TAG, "Exception occurred: $errorMessage")
    }

    private companion object {
        const val TAG = "TasksViewModel"
    }
}