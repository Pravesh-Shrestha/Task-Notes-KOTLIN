package com.example.tasknotesapp.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tasknotesapp.R
import com.example.tasknotesapp.databinding.ActivityAddNoteBinding
import com.example.tasknotesapp.model.NoteModel
import com.example.tasknotesapp.repository.NoteRepositoryImpl
import com.example.tasknotesapp.repository.TaskRepositoryImpl
import com.example.tasknotesapp.viewModel.AuthViewModel
import com.example.tasknotesapp.viewModel.MainViewModel


class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var viewModel: MainViewModel
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel.checkCurrentUser()
        authViewModel.user.observe(this) { user ->
            if (user == null) {
                finish()
                return@observe
            }
        }

        val noteRepo = NoteRepositoryImpl()
        viewModel = MainViewModel(TaskRepositoryImpl(), noteRepo)

        val noteId = intent.getStringExtra("noteId")
        if (noteId != null) {
            binding.addNoteButton.text = "Update Note"
            viewModel.getNoteById(noteId)
            viewModel.currentNote.observe(this) { note ->
                note?.let {
                    binding.noteTitleEdit.setText(it.title)
                    binding.noteContentEdit.setText(it.content)
                }
            }
        } else {
            binding.addNoteButton.text = "Add Note"
        }

        binding.addNoteButton.setOnClickListener {
            val title = binding.noteTitleEdit.text.toString()
            val content = binding.noteContentEdit.text.toString()

            if (title.isEmpty()) {
                Toast.makeText(this, "Title is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val note = NoteModel("", title, content)

            if (noteId == null) {
                viewModel.addNote(note) { success, message ->
                    handleResult(success, message)
                }
            } else {
                val updates = mutableMapOf<String, Any>(
                    "title" to title,
                    "content" to content
                )
                viewModel.updateNote(noteId, updates) { success, message ->
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