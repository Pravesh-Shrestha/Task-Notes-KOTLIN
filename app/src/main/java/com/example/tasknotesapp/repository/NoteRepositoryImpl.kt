//----------------------------NOTE-CRUD-FUNCTIONS USED-----------------------s

package com.example.tasknotesapp.repository

import com.example.tasknotesapp.model.NoteModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NoteRepositoryImpl : NoteRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val userId get() = auth.currentUser?.uid ?: ""
    private val notesRef get() = db.collection("users").document(userId).collection("notes")

    override fun addNote(note: NoteModel, callback: (Boolean, String) -> Unit) {
        val id = notesRef.document().id
        note.id = id
        notesRef.document(id).set(note)
            .addOnSuccessListener { callback(true, "Note added successfully") }
            .addOnFailureListener { callback(false, it.message ?: "Error adding note") }
    }

    override fun updateNote(noteId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit) {
        notesRef.document(noteId).update(data)
            .addOnSuccessListener { callback(true, "Note updated successfully") }
            .addOnFailureListener { callback(false, it.message ?: "Error updating note") }
    }

    override fun deleteNote(noteId: String, callback: (Boolean, String) -> Unit) {
        notesRef.document(noteId).delete()
            .addOnSuccessListener { callback(true, "Note deleted successfully") }
            .addOnFailureListener { callback(false, it.message ?: "Error deleting note") }
    }

    override fun getAllNotes(callback: (List<NoteModel>?, Boolean, String) -> Unit) {
        notesRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                callback(null, false, error.message ?: "Error fetching notes")
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val notes = snapshot.toObjects(NoteModel::class.java)
                callback(notes, true, "Notes fetched successfully")
            }
        }
    }

    override fun getNoteById(noteId: String, callback: (NoteModel?, Boolean, String) -> Unit) {
        notesRef.document(noteId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                callback(null, false, error.message ?: "Error fetching note")
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val note = snapshot.toObject(NoteModel::class.java)
                callback(note, true, "Note fetched successfully")
            } else {
                callback(null, false, "Note not found")
            }
        }
    }
}