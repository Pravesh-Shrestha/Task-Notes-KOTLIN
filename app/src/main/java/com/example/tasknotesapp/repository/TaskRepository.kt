package com.example.tasknotesapp.repository

import com.example.tasknotesapp.model.TaskModel

interface TaskRepository {
    fun addTask(task: TaskModel, callback: (Boolean, String) -> Unit)
    fun updateTask(taskId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit)
    fun deleteTask(taskId: String, callback: (Boolean, String) -> Unit)
    fun getAllTasks(callback: (List<TaskModel>?, Boolean, String) -> Unit)
    fun getTaskById(taskId: String, callback: (TaskModel?, Boolean, String) -> Unit)
}