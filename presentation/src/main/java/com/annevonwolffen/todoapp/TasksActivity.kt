package com.annevonwolffen.todoapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.annevonwolffen.todoapp.databinding.TasksActivityBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlin.math.abs

class TasksActivity : AppCompatActivity() {
    private val tasksViewModel: TasksViewModel by lazy {
        ViewModelProvider(this)[TasksViewModel::class.java]
    }
    private lateinit var appBarLayout: AppBarLayout
    private var appBarExpanded = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: TasksActivityBinding = TasksActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.viewModel = tasksViewModel

        if (savedInstanceState == null) {
            tasksViewModel.loadTasks()
        }
        setLightStatusBar()
        setUpAppbar()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        findViewById<RecyclerView>(R.id.tasks_rv).run {
            adapter = TasksAdapter(tasksViewModel)
            val callback =
                ItemTouchHelperCallback(adapter as ItemTouchHelperCallback.ItemTouchHelperAdapter)
            val itemTouchHelper = ItemTouchHelper(callback)
            itemTouchHelper.attachToRecyclerView(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        setUpOptionsMenu(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_done_tasks_toggle) {
            // TODO: show smth to hide/show done tasks and update flag
            invalidateOptionsMenu()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpOptionsMenu(menu: Menu) {
        val doneTasksVisibilityToggle = menu.findItem(R.id.action_done_tasks_toggle)
        if (!appBarExpanded) {
            doneTasksVisibilityToggle.isVisible = true
            // TODO: if else dependent of done tasks visibility
            val drawable = ContextCompat.getDrawable(this, R.drawable.ic_visibility_24dp)
            drawable?.let {
                DrawableCompat.setTint(
                    it,
                    ContextCompat.getColor(this, R.color.colorBlue)
                )
            }
            doneTasksVisibilityToggle.icon = drawable
            MenuItemCompat.setContentDescription(
                doneTasksVisibilityToggle,
                getString(R.string.hide_done)
            )
        } else {
            doneTasksVisibilityToggle.isVisible = false
        }
    }

    private fun setLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun setUpAppbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val collapsingToolbar = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar)
        collapsingToolbar?.title = getString(R.string.main_page_title)
        appBarLayout = findViewById(R.id.appbar)
        appBarLayout.addOnOffsetChangedListener(OnOffsetChangedListener { _, verticalOffset ->
            //  Vertical offset == 0 indicates appBar is fully expanded.
            if (abs(verticalOffset) > 200) {
                appBarExpanded = false
                invalidateOptionsMenu()
            } else {
                appBarExpanded = true
                invalidateOptionsMenu()
            }
        })
    }
}