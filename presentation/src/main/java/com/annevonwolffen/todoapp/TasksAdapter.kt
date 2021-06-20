package com.annevonwolffen.todoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.annevonwolffen.domain.Task

class TasksAdapter : ListAdapter<Task, TasksAdapter.ViewHolder>(DiffUtilCallback()) {

    class DiffUtilCallback : DiffUtil.ItemCallback<Task>() {

        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = when (viewType) {
            TOP -> DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(parent.context),
                R.layout.task_top_list_item,
                parent,
                false
            )
            BOTTOM -> DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(parent.context),
                R.layout.task_bottom_list_item,
                parent,
                false
            )
            else -> DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(parent.context),
                R.layout.task_list_item,
                parent,
                false
            )
        }
        return ViewHolder(binding.root)
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TOP
            currentList.size - 1 -> BOTTOM
            else -> MIDDLE
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(task: Task) {
            val binding = DataBindingUtil.getBinding<ViewDataBinding>(view)
            binding?.let {
                it.setVariable(BR.task, task)
                it.executePendingBindings()
            }
        }
    }

    private companion object {
        const val TOP = 0
        const val MIDDLE = 1
        const val BOTTOM = 2
    }
}