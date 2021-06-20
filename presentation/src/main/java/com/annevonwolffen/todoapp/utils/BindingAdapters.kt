package com.annevonwolffen.todoapp.utils

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.annevonwolffen.domain.Task
import com.annevonwolffen.todoapp.TasksAdapter

@BindingAdapter("visibility")
fun View.setVisibility(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("tasksData")
fun RecyclerView.setTasks(tasks: List<Task>?) {
    if (adapter == null) {
        adapter = TasksAdapter()
    }
    tasks?.takeIf { adapter is TasksAdapter }?.let {
        (adapter as TasksAdapter).submitList(tasks)
    }
}