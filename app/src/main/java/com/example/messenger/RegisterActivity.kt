package com.example.messenger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var signUpButton: Button

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initViews()
        observeViewModel()
        signUpButton.setOnClickListener {

            val email = getTrimmedText(emailEditText)
            val password = getTrimmedText(passwordEditText)
            val name = getTrimmedText(nameEditText)
            val lastName = getTrimmedText(lastNameEditText)
            val age = getTrimmedText(ageEditText).toInt()
            viewModel.signUp(email, password, name, lastName, age)
        }
    }

    private fun getTrimmedText(editText: EditText) = editText.text.toString().trim()

    private fun observeViewModel() {
        viewModel.error.observe(this) {
            it?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.user.observe(this) {
            startActivity(UsersActivity.newIntent(this, it.uid))
            finish()
        }
    }

    private fun initViews() {
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        nameEditText = findViewById(R.id.nameEditText)
        lastNameEditText = findViewById(R.id.last_name)
        ageEditText = findViewById(R.id.ageEditText)
        signUpButton = findViewById(R.id.signUpButton)
    }

    companion object {
        fun newIntent(ctx: Context): Intent {
            return Intent(ctx, RegisterActivity::class.java)
        }
    }
}