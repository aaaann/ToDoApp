package com.annevonwolffen.todoapp.utils

import android.graphics.Paint
import android.view.View
import android.widget.TextView
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
    tasks?.takeIf { adapter is TasksAdapter }?.let {
        (adapter as TasksAdapter).submitList(tasks)
    }
}

@BindingAdapter("isCrossedOut")
fun TextView.setCrossedOut(isCrossedOutNeeded: Boolean) {
    paintFlags = if (isCrossedOutNeeded) {
        paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        paintFlags and (Paint.STRIKE_THRU_TEXT_FLAG.inv())
    }
}