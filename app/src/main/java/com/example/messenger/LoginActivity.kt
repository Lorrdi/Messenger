package com.example.messenger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity


class LoginActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private lateinit var editTextLogin: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var forgotPassButton: Button
    private lateinit var registerButton: Button

    private val viewModel: LoginViewModel by viewModels()

    companion object {
        fun newIntent(ctx: Context): Intent {
            return Intent(ctx, LoginActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()

        observeViewModel()

        setupClickListeners()

    }

    private fun setupClickListeners() {
        loginButton.setOnClickListener {
            val login = editTextLogin.text.trim().toString()
            val password = editTextPassword.text.trim().toString()

            viewModel.login(login, password)
        }
        forgotPassButton.setOnClickListener {
            val intent = ResetPasswordActivity.newIntent(this, editTextLogin.text.trim().toString())
            startActivity(intent)
        }
        registerButton.setOnClickListener {
            val intent = RegisterActivity.newIntent(this)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        viewModel.getUser().observe(this) { firebaseUser ->
            if (firebaseUser != null) {
                startActivity(UsersActivity.newIntent(this, firebaseUser.uid))
                finish()
            }
        }
        viewModel.getError().observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT)?.show()
        }
    }

    private fun initViews() {
        editTextLogin = findViewById(R.id.editTextLogin)
        editTextPassword = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.loginButton)
        forgotPassButton = findViewById(R.id.forgotPasswordButton)
        registerButton = findViewById(R.id.registerButton)
    }

}