package com.example.tasknotesapp.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    suspend fun signUp(email: String, password: String): FirebaseUser? {
        auth.createUserWithEmailAndPassword(email, password).await()
        return auth.currentUser
    }

    suspend fun login(email: String, password: String): FirebaseUser? {
        auth.signInWithEmailAndPassword(email, password).await()
        return auth.currentUser
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun signOut() {
        auth.signOut()
    }
}