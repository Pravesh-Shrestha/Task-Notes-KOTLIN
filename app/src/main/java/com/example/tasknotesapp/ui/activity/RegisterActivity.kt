//----------------REGISTRATION PAGE-------------------------//
package com.example.tasknotesapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tasknotesapp.databinding.ActivityRegisterBinding
import com.example.tasknotesapp.viewModel.AuthViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.signUp(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginRedirectButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // -------Observe authentication result------------------
        authViewModel.authResult.observe(this) { result ->
            result.fold(
                onSuccess = { user ->
                    if (user != null) {
                        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                },
                onFailure = { exception ->
                    Toast.makeText(this, "Registration Failed: ${exception.message}", Toast.LENGTH_LONG).show()
                }
            )
        }
    }
}

//------------------NAVIGATE TO LOGIN/MAIN-------------//