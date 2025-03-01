//--------------------------NOTE-REPOSITORY---------------------//

package com.example.tasknotesapp.repository

import com.example.tasknotesapp.model.NoteModel

interface NoteRepository {
    fun addNote(note: NoteModel, callback: (Boolean, String) -> Unit)
    fun updateNote(noteId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit)
    fun deleteNote(noteId: String, callback: (Boolean, String) -> Unit)
    fun getAllNotes(callback: (List<NoteModel>?, Boolean, String) -> Unit)
    fun getNoteById(noteId: String, callback: (NoteModel?, Boolean, String) -> Unit)
}