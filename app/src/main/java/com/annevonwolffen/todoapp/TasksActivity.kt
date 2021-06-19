package com.annevonwolffen.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.annevonwolffen.todoapp.databinding.TasksActivityBinding

class TasksActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: TasksActivityBinding = TasksActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this
    }
}