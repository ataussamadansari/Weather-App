package com.example.weatherapptask_1.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.weatherapptask_1.MainActivity
import com.example.weatherapptask_1.R
import com.example.weatherapptask_1.databinding.ActivityAuthBinding
import com.example.weatherapptask_1.utils.SharedPrefHelper

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            val password = binding.passEt.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                if (email.isEmpty()) {
                    binding.emailEt.error = "Email is required"
                }
                if (password.isEmpty()) {
                    binding.passEt.error = "Password is required"
                }
                return@setOnClickListener
            }

            if (email == "testapp@google.com" && password == "Test@123456") {
                // Save login session
                SharedPrefHelper.setLoggedIn(this, true)
                // ✅ Login successful
                binding.errorTv.text = ""
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                // Redirect to User List Activity
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                // ❌ Login failed
                binding.errorTv.text = "Invalid email or password"
            }
        }
    }
}