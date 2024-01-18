package com.example.messenger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var resetPasswordButton: Button
    private val viewModel: ResetPasswordViewModel by viewModels()

    companion object {
        private const val EMAIL_KEY = "email"
        fun newIntent(ctx: Context, email: String): Intent {
            val intent = Intent(ctx, ResetPasswordActivity::class.java)
            intent.putExtra(EMAIL_KEY, email)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        initViews()
        observeViewModel()

        val email = intent?.getStringExtra(EMAIL_KEY)
        emailEditText.setText(email)
        resetPasswordButton.setOnClickListener {
            val emailForReset = emailEditText.text.trim().toString()
            viewModel.resetPassword(emailForReset)
        }
    }

    private fun observeViewModel() {
        viewModel.error.observe(this) { errorMessage ->
            if (errorMessage != null) Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT)
                .show()
        }
        viewModel.isSuccess.observe(this) { success ->
            if (success) Toast.makeText(
                this, getString(R.string.reset_link_sent), Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun initViews() {
        emailEditText = findViewById(R.id.resetPasswordEditText)
        resetPasswordButton = findViewById(R.id.resetPasswordButton)

    }
}