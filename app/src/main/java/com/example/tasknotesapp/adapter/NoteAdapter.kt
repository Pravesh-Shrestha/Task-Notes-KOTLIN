//-------------------------------TASK-ADAPTER/VIEW----------------------------------//

package com.example.tasknotesapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tasknotesapp.R
import com.example.tasknotesapp.model.NoteModel
import com.example.tasknotesapp.ui.activity.AddNoteActivity

class NoteAdapter(
    private val context: Context,
    private var notes: ArrayList<NoteModel>
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.noteTitle)
        val content: TextView = itemView.findViewById(R.id.noteContent)
        val editBtn: TextView = itemView.findViewById(R.id.editNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.title.text = note.title
        holder.content.text = note.content

        holder.editBtn.setOnClickListener {
            val intent = Intent(context, AddNoteActivity::class.java).apply {
                putExtra("noteId", note.id)
            }
            context.startActivity(intent)
        }
    }

    fun updateData(newNotes: List<NoteModel>) {
        notes.clear()
        notes.addAll(newNotes)
        notifyDataSetChanged()
    }

    fun getNoteId(position: Int): String = notes[position].id
}