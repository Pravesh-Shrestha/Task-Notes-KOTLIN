//------------------------USER VIEW MODEL-----------------------------//
package com.example.tasknotesapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasknotesapp.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _user = MutableLiveData<FirebaseUser?>()
    val user: LiveData<FirebaseUser?> get() = _user

    private val _authResult = MutableLiveData<Result<FirebaseUser?>>()
    val authResult: LiveData<Result<FirebaseUser?>> get() = _authResult

    // In AuthViewModel
    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            try {
                Log.d("AuthViewModel", "Attempting sign-up with email: $email")
                val newUser = authRepository.signUp(email, password)
                _user.value = newUser
                _authResult.value = Result.success(newUser)
                Log.d("AuthViewModel", "Sign-up successful: ${newUser?.email}")
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Sign-up failed: ${e.message}")
                _user.value = null
                _authResult.value = Result.failure(e)
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val loggedInUser = authRepository.login(email, password)
                _user.value = loggedInUser
                _authResult.value = Result.success(loggedInUser)
            } catch (e: Exception) {
                _user.value = null
                _authResult.value = Result.failure(e)
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
        _user.value = null
    }

    fun checkCurrentUser() {
        _user.value = authRepository.getCurrentUser()
    }
}