package com.example.tasknotesapp.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tasknotesapp.R
import com.example.tasknotesapp.databinding.ActivityAddTaskBinding
import com.example.tasknotesapp.model.TaskModel
import com.example.tasknotesapp.repository.NoteRepositoryImpl
import com.example.tasknotesapp.repository.TaskRepositoryImpl
import com.example.tasknotesapp.viewModel.AuthViewModel
import com.example.tasknotesapp.viewModel.MainViewModel


class AddTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var viewModel: MainViewModel
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel.checkCurrentUser()
        authViewModel.user.observe(this) { user ->
            if (user == null) {
                finish()
                return@observe
            }
        }

        val taskRepo = TaskRepositoryImpl()
        viewModel = MainViewModel(taskRepo, NoteRepositoryImpl())

        val taskId = intent.getStringExtra("taskId")
        if (taskId != null) {
            binding.addTaskButton.text = "Update Task"
            viewModel.getTaskById(taskId)
            viewModel.currentTask.observe(this) { task ->
                task?.let {
                    binding.taskTitleEdit.setText(it.title)
                    binding.taskDescEdit.setText(it.description)
                    binding.taskCompletedCheckbox.isChecked = it.isCompleted
                }
            }
        } else {
            binding.addTaskButton.text = "Add Task"
        }

        binding.addTaskButton.setOnClickListener {
            val title = binding.taskTitleEdit.text.toString()
            val description = binding.taskDescEdit.text.toString()
            val isCompleted = binding.taskCompletedCheckbox.isChecked

            if (title.isEmpty()) {
                Toast.makeText(this, "Title is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val task = TaskModel("", title, description, isCompleted)

            if (taskId == null) {
                viewModel.addTask(task) { success, message ->
                    handleResult(success, message)
                }
            } else {
                val updates = mutableMapOf<String, Any>(
                    "title" to title,
                    "description" to description,
                    "isCompleted" to isCompleted
                )
                viewModel.updateTask(taskId, updates) { success, message ->
                    handleResult(success, message)
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun handleResult(success: Boolean, message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        if (success) finish()
    }
}