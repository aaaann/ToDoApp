package com.annevonwolffen.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.annevonwolffen.data.remote.TaskServerModel
import com.annevonwolffen.domain.Priority
import com.annevonwolffen.domain.Task
import java.util.Date

@Entity(tableName = "TASKS")
data class TaskDbModel(
    @PrimaryKey val id: String,
    val title: String,
    val deadline: Date? = null,
    @ColumnInfo(name = "is_done")
    val isDone: Boolean = false,
    val priority: Priority = Priority.UNDEFINED,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
    @ColumnInfo(name = "is_deleted")
    val isDeleted: Int = 0,
    @ColumnInfo(name = "is_dirty")
    val isDirty: Int = 0
)

fun TaskDbModel.toDomain(): Task =
    Task(id, title, deadline, isDone, priority, createdAt, updatedAt)

fun TaskDbModel.toServer(): TaskServerModel =
    TaskServerModel(
        id,
        title,
        priority.serverName,
        isDone,
        deadline?.time ?: 0,
        createdAt,
        updatedAt
    )

fun Task.toDb(): TaskDbModel =
    TaskDbModel(id, title, deadline, isDone, priority, createdAt, updatedAt)