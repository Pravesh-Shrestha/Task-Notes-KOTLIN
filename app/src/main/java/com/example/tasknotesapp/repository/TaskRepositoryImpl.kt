package com.example.tasknotesapp.repository

import com.example.tasknotesapp.model.TaskModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TaskRepositoryImpl : TaskRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val userId get() = auth.currentUser?.uid ?: ""
    private val tasksRef get() = db.collection("users").document(userId).collection("tasks")

    override fun addTask(task: TaskModel, callback: (Boolean, String) -> Unit) {
        val id = tasksRef.document().id
        task.taskId = id
        tasksRef.document(id).set(task)
            .addOnSuccessListener { callback(true, "Task added successfully") }
            .addOnFailureListener { callback(false, it.message ?: "Error adding task") }
    }

    override fun updateTask(taskId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit) {
        tasksRef.document(taskId).update(data)
            .addOnSuccessListener { callback(true, "Task updated successfully") }
            .addOnFailureListener { callback(false, it.message ?: "Error updating task") }
    }

    override fun deleteTask(taskId: String, callback: (Boolean, String) -> Unit) {
        tasksRef.document(taskId).delete()
            .addOnSuccessListener { callback(true, "Task deleted successfully") }
            .addOnFailureListener { callback(false, it.message ?: "Error deleting task") }
    }

    override fun getAllTasks(callback: (List<TaskModel>?, Boolean, String) -> Unit) {
        tasksRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                callback(null, false, error.message ?: "Error fetching tasks")
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val tasks = snapshot.toObjects(TaskModel::class.java)
                callback(tasks, true, "Tasks fetched successfully")
            }
        }
    }

    override fun getTaskById(taskId: String, callback: (TaskModel?, Boolean, String) -> Unit) {
        tasksRef.document(taskId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                callback(null, false, error.message ?: "Error fetching task")
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val task = snapshot.toObject(TaskModel::class.java)
                callback(task, true, "Task fetched successfully")
            } else {
                callback(null, false, "Task not found")
            }
        }
    }
}