package com.annevonwolffen.todoapp.model

import com.annevonwolffen.domain.Task

fun TaskPresentationModel.mapToTaskDomainModel(): Task = Task(id, title, deadline, isDone, priority)