package com.example.tasknotesapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasknotesapp.model.TaskModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import android.util.Log

class TaskViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _tasks = MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> get() = _tasks

    private fun fetchTasks(userId: String) {
        Log.d("TaskViewModel", "Fetching tasks for userId: $userId")
        db.collection("users").document(userId).collection("tasks")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("TaskViewModel", "Snapshot listener failed: ${e.message}", e)
                    // Fallback to one-time fetch if listener fails
                    db.collection("users").document(userId).collection("tasks").get()
                        .addOnSuccessListener { querySnapshot ->
                            val taskList = querySnapshot.toObjects(TaskModel::class.java)
                            _tasks.value = taskList
                            Log.d("TaskViewModel", "One-time fetch succeeded with ${taskList.size} tasks: ${taskList.map { it.title }}")
                        }
                        .addOnFailureListener { e2 ->
                            Log.e("TaskViewModel", "One-time fetch failed: ${e2.message}", e2)
                        }
                    return@addSnapshotListener
                }
                val taskList = snapshot?.toObjects(TaskModel::class.java) ?: emptyList()
                _tasks.value = taskList
                Log.d("TaskViewModel", "Snapshot fetch succeeded with ${taskList.size} tasks: ${taskList.map { it.title }}")
            }
    }

    fun addTask(task: TaskModel) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: run {
            Log.e("TaskViewModel", "No authenticated user")
            return
        }
        viewModelScope.launch {
            db.collection("users").document(userId).collection("tasks")
                .add(task)
                .addOnSuccessListener { documentReference ->
                    val taskWithId = task.copy(taskId = documentReference.id)
                    fetchTasks(userId) // Refresh to update UI
                    Log.d("TaskViewModel", "Task added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.e("TaskViewModel", "Failed to add task: ${e.message}", e)
                }
        }
    }

    fun updateTask(task: TaskModel) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            if (task.taskId.isNotEmpty()) {
                db.collection("users").document(userId).collection("tasks").document(task.taskId)
                    .set(task)
                    .addOnSuccessListener {
                        fetchTasks(userId) // Refresh to update UI
                        Log.d("TaskViewModel", "Task updated with ID: ${task.taskId}")
                    }
                    .addOnFailureListener { e ->
                        Log.e("TaskViewModel", "Failed to update task: ${e.message}", e)
                    }
            } else {
                Log.w("TaskViewModel", "Cannot update task: ID is empty")
            }
        }
    }

    fun deleteTask(userId: String, taskId: String) {
        viewModelScope.launch {
            db.collection("users").document(userId).collection("tasks").document(taskId).delete()
                .addOnSuccessListener {
                    fetchTasks(userId) // Refresh to update UI
                    Log.d("TaskViewModel", "Task deleted with ID: $taskId")
                }
                .addOnFailureListener { e ->
                    Log.e("TaskViewModel", "Failed to delete task: ${e.message}", e)
                }
        }
    }
}