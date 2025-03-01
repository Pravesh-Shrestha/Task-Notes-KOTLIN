package com.example.tasknotesapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasknotesapp.model.NoteModel
import com.example.tasknotesapp.model.TaskModel
import com.example.tasknotesapp.repository.NoteRepository
import com.example.tasknotesapp.repository.TaskRepository


class MainViewModel(
    private val taskRepo: TaskRepository,
    private val noteRepo: NoteRepository
) : ViewModel() {
    val tasks = MutableLiveData<List<TaskModel>?>()
    val notes = MutableLiveData<List<NoteModel>?>()
    val loading = MutableLiveData<Boolean>()

    // Add these for editing
    val currentTask = MutableLiveData<TaskModel?>()
    val currentNote = MutableLiveData<NoteModel?>()

    // Task Operations
    fun addTask(task: TaskModel, callback: (Boolean, String) -> Unit) {
        taskRepo.addTask(task, callback)
    }

    fun updateTask(taskId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit) {
        taskRepo.updateTask(taskId, data, callback)
    }

    fun deleteTask(taskId: String, callback: (Boolean, String) -> Unit) {
        taskRepo.deleteTask(taskId, callback)
    }

    fun getAllTasks() {
        loading.value = true
        taskRepo.getAllTasks { taskList, success, _ ->
            if (success) {
                tasks.value = taskList
                loading.value = false
            }
        }
    }

    fun getTaskById(taskId: String) {
        taskRepo.getTaskById(taskId) { task, success, _ ->
            if (success) currentTask.value = task
        }
    }

    // Note Operations
    fun addNote(note: NoteModel, callback: (Boolean, String) -> Unit) {
        noteRepo.addNote(note, callback)
    }

    fun updateNote(noteId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit) {
        noteRepo.updateNote(noteId, data, callback)
    }

    fun deleteNote(noteId: String, callback: (Boolean, String) -> Unit) {
        noteRepo.deleteNote(noteId, callback)
    }

    fun getAllNotes() {
        loading.value = true
        noteRepo.getAllNotes { noteList, success, _ ->
            if (success) {
                notes.value = noteList
                loading.value = false
            }
        }
    }

    fun getNoteById(noteId: String) {
        noteRepo.getNoteById(noteId) { note, success, _ ->
            if (success) currentNote.value = note
        }
    }
}