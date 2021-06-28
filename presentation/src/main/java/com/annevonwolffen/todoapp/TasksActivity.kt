package com.annevonwolffen.todoapp

import android.app.Activity
import android.content.Intent
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.annevonwolffen.domain.settings.SettingsInteractor
import com.annevonwolffen.todoapp.databinding.TasksActivityBinding
import com.annevonwolffen.todoapp.model.TaskPresentationModel
import com.annevonwolffen.todoapp.notification.NotificationHelper
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlin.math.abs

class TasksActivity : AppCompatActivity(), OnAddTaskClickListener {
    private val notificationHelper: NotificationHelper by lazy {
        (applicationContext as AppDelegate).notificationHelper
    }
    private val settingsInteractor: SettingsInteractor by lazy {
        (applicationContext as AppDelegate).settingsInteractor
    }
    private val tasksViewModel: TasksViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProviderFactory { TasksViewModel(settingsInteractor) }
        )[TasksViewModel::class.java]
    }
    private lateinit var appBarLayout: AppBarLayout
    private var appBarExpanded = true
    private var doneTasksToggleOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: TasksActivityBinding = TasksActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.viewModel = tasksViewModel
        binding.onAddTaskListener = this

        setLightStatusBar()
        setUpAppbar()
        setUpRecyclerView()

        if (savedInstanceState == null) {
            tasksViewModel.loadTasks()
        }
        initObservers()
    }

    private fun setUpRecyclerView() {
        findViewById<RecyclerView>(R.id.tasks_rv).run {
            adapter = TasksAdapter(tasksViewModel, object : OnTaskClickListener {
                override fun onClickTask(task: TaskPresentationModel) {
                    startActivityForResult(
                        AddTaskActivity.newIntent(this@TasksActivity, task),
                        ADD_TASK_CODE
                    )
                }
            })
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
            tasksViewModel.onDoneTasksToggleClick()
            invalidateOptionsMenu()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpOptionsMenu(menu: Menu) {
        val doneTasksVisibilityToggle = menu.findItem(R.id.action_done_tasks_toggle)
        if (!appBarExpanded) {
            doneTasksVisibilityToggle.isVisible = true
            val drawable = ContextCompat.getDrawable(
                this,
                if (doneTasksToggleOn) R.drawable.ic_visibility_off_24dp else R.drawable.ic_visibility_24dp
            )
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_TASK_CODE && resultCode == Activity.RESULT_OK) {
            data?.getParcelableExtra<TaskPresentationModel>(ADD_TASK_KEY)
                ?.let { tasksViewModel.saveTask(it) }
            data?.getParcelableExtra<TaskPresentationModel>(DELETE_TASK_KEY)
                ?.let { tasksViewModel.onDeleteTask(it.id) }
        }
    }

    override fun onAddTask() {
        startActivityForResult(AddTaskActivity.newIntent(this), ADD_TASK_CODE)
    }

    private fun initObservers() {
        tasksViewModel.tasks.observe(
            this,
            Observer { notificationHelper.scheduleNotificationsForTasks(it.filterNot { task -> task.isDone || task.deadline == null }) })
        tasksViewModel.isDoneTasksShown.observe(this, Observer {
            doneTasksToggleOn = it
            invalidateOptionsMenu()
        })
    }

    companion object {
        private const val ADD_TASK_CODE = 1
        const val ADD_TASK_KEY = "ADD_TASK_KEY"
        const val DELETE_TASK_KEY = "DELETE_TASK_KEY"
    }
}