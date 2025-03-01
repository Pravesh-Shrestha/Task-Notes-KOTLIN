//----------------Main FILE------------------
package com.example.tasknotesapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasknotesapp.R
import com.example.tasknotesapp.adapter.NoteAdapter
import com.example.tasknotesapp.adapter.TaskAdapter
import com.example.tasknotesapp.databinding.ActivityMainBinding
import com.example.tasknotesapp.repository.NoteRepositoryImpl
import com.example.tasknotesapp.repository.TaskRepositoryImpl
import com.example.tasknotesapp.viewModel.AuthViewModel
import com.example.tasknotesapp.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var noteAdapter: NoteAdapter
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        authViewModel.checkCurrentUser()
        authViewModel.user.observe(this) { user ->
            if (user == null) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                return@observe
            }
        }

        val taskRepo = TaskRepositoryImpl()
        val noteRepo = NoteRepositoryImpl()
        viewModel = MainViewModel(taskRepo, noteRepo)

        taskAdapter = TaskAdapter(this, ArrayList())
        noteAdapter = NoteAdapter(this, ArrayList())

        binding.taskRecyclerView.apply {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        binding.noteRecyclerView.apply {
            adapter = noteAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        viewModel.tasks.observe(this) { tasks ->
            tasks?.let { taskAdapter.updateData(it) }
        }
        viewModel.notes.observe(this) { notes ->
            notes?.let { noteAdapter.updateData(it) }
        }
        viewModel.loading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.getAllTasks()
        viewModel.getAllNotes()

        binding.addTaskFab.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }
        binding.addNoteFab.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }
//---------------TASK-MANAGEMENT-CRUD-MVVM------------------------------------------
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val id = taskAdapter.getTaskId(position)

                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Delete Task")
                    .setMessage("Are you sure you want to delete this task?")
                    .setPositiveButton("Yes") { _, _ ->
                        viewModel.deleteTask(id) { success, message ->
                            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("No") { _, _ ->
                        taskAdapter.notifyItemChanged(position)
                    }
                    .setCancelable(false)
                    .show()
            }
        }).attachToRecyclerView(binding.taskRecyclerView)

        //---------------NOTE-MANAGEMENT-CRUD-MVVM------------------------------------------

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val id = noteAdapter.getNoteId(position)

                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Delete Note")
                    .setMessage("Are you sure you want to delete this note?")
                    .setPositiveButton("Yes") { _, _ ->
                        viewModel.deleteNote(id) { success, message ->
                            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("No") { _, _ ->
                        noteAdapter.notifyItemChanged(position)
                    }
                    .setCancelable(false)
                    .show()
            }
        }).attachToRecyclerView(binding.noteRecyclerView)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                authViewModel.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}