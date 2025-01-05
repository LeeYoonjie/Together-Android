package com.together.togetherproject.presentation.signup.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.together.togetherproject.MainActivity
import com.together.togetherproject.databinding.ActivitySignUpBinding
import com.together.togetherproject.presentation.signin.SignInActivity
import com.together.togetherproject.presentation.signup.viewmodel.SignUpViewModel

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSignUp.setOnClickListener {
            val name = binding.editTextName.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val passwordConfirm = binding.editTextPasswordConfirm.text.toString().trim()
            val birthdate = binding.editTextBirthdate.text.toString().trim()

            viewModel.signUp(name, email, password, passwordConfirm, birthdate)
        }

        binding.imageButtonClose.setOnClickListener {
            navigateToSignIn()
        }

        viewModel.signUpStatus.observe(this) { result ->
            result.onSuccess {
                navigateToMain()
            }.onFailure { error ->
                Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToSignIn() {
        finish()
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}