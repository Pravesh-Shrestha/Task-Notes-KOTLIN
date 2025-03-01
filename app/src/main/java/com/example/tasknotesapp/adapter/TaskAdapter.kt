package com.example.tasknotesapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tasknotesapp.R
import com.example.tasknotesapp.model.TaskModel
import com.example.tasknotesapp.ui.activity.AddTaskActivity

class TaskAdapter(
    private val context: Context,
    private var tasks: ArrayList<TaskModel>
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.taskTitle)
        val description: TextView = itemView.findViewById(R.id.taskDescription)
        val completed: CheckBox = itemView.findViewById(R.id.taskCompleted)
        val editBtn: TextView = itemView.findViewById(R.id.editTask)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.title.text = task.title
        holder.description.text = task.description
        holder.completed.isChecked = task.isCompleted

        holder.editBtn.setOnClickListener {
            val intent = Intent(context, AddTaskActivity::class.java).apply {
                putExtra("taskId", task.taskId)
            }
            context.startActivity(intent)
        }
    }

    fun updateData(newTasks: List<TaskModel>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }

    fun getTaskId(position: Int): String = tasks[position].taskId
}