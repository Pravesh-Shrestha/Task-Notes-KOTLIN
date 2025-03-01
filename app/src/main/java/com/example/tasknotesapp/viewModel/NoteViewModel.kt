//----------------------------NOTEVIEWMODEL--------------------------//

package com.example.tasknotesapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import android.util.Log
import com.example.tasknotesapp.model.NoteModel

class NoteViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _notes = MutableLiveData<List<NoteModel>>()
    val notes: LiveData<List<NoteModel>> get() = _notes

    fun fetchNotes(userId: String) {
        Log.d("NoteViewModel", "Fetching notes for userId: $userId")
        db.collection("users").document(userId).collection("notes")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("NoteViewModel", "Snapshot listener failed: ${e.message}", e)
                    // Fallback to one-time fetch if listener fails
                    db.collection("users").document(userId).collection("notes").get()
                        .addOnSuccessListener { querySnapshot ->
                            val noteList = querySnapshot.toObjects(NoteModel::class.java)
                            _notes.value = noteList
                            Log.d("NoteViewModel", "One-time fetch succeeded with ${noteList.size} notes: ${noteList.map { it.title }}")
                        }
                        .addOnFailureListener { e2 ->
                            Log.e("NoteViewModel", "One-time fetch failed: ${e2.message}", e2)
                        }
                    return@addSnapshotListener
                }
                val noteList = snapshot?.toObjects(NoteModel::class.java) ?: emptyList()
                _notes.value = noteList
                Log.d("NoteViewModel", "Snapshot fetch succeeded with ${noteList.size} notes: ${noteList.map { it.title }}")
            }
    }

    fun addNote(note: NoteModel) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: run {
            Log.e("NoteViewModel", "No authenticated user")
            return
        }
        viewModelScope.launch {
            db.collection("users").document(userId).collection("notes")
                .add(note)
                .addOnSuccessListener { documentReference ->
                    val noteWithId = note.copy(id = documentReference.id)
                    fetchNotes(userId) // Refresh to update UI
                    Log.d("NoteViewModel", "Note added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.e("NoteViewModel", "Failed to add note: ${e.message}", e)
                }
        }
    }

    fun deleteNote(userId: String, noteId: String) {
        viewModelScope.launch {
            db.collection("users").document(userId).collection("notes").document(noteId).delete()
                .addOnSuccessListener {
                    fetchNotes(userId) // Refresh to update UI
                    Log.d("NoteViewModel", "Note deleted with ID: $noteId")
                }
                .addOnFailureListener { e ->
                    Log.e("NoteViewModel", "Failed to delete note: ${e.message}", e)
                }
        }
    }

    fun updateNote(note: NoteModel) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            db.collection("users").document(userId).collection("notes").document(note.id)
                .set(note)
                .addOnSuccessListener {
                    fetchNotes(userId) // Refresh to update UI
                    Log.d("NoteViewModel", "Note updated with ID: ${note.id}")
                }
                .addOnFailureListener { e ->
                    Log.e("NoteViewModel", "Failed to update note: ${e.message}", e)
                }
        }
    }
}