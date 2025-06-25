package com.example.weatherapptask_1.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapptask_1.databinding.ActivityAddUserBinding
import com.example.weatherapptask_1.model.User
import com.example.weatherapptask_1.viewmodel.UserListViewModel

class AddUserActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddUserBinding
    private lateinit var userListViewModel: UserListViewModel

    private var selectedUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(16, systemBars.top, 16, systemBars.bottom)
            insets
        }

        userListViewModel = ViewModelProvider(this).get(UserListViewModel::class.java)

        val userId = intent.getIntExtra("userId", -1)

        // If userId is valid, we are in edit mode
        if (userId != -1) {
            userListViewModel.allNotes.observe(this) { users ->
                selectedUser = users.find { it.id == userId }
                selectedUser?.let {
                    binding.firstNameEt.setText(it.firstName)
                    binding.lastNameEt.setText(it.lastName)
                    binding.emailEt.setText(it.email)
                    binding.saveBtn.text = "Update"
                }
            }
        }


        // cancel btn
        binding.cancelBtn.setOnClickListener {
            finish()
        }

        // save & update btn
        binding.saveBtn.setOnClickListener {
            val firstName = binding.firstNameEt.text.toString().trim()
            val lastName = binding.lastNameEt.text.toString().trim()
            val email = binding.emailEt.text.toString().trim()

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
                if (firstName.isEmpty()) {
                    binding.firstNameEt.error = "First Name is required!"
                }
                if (lastName.isEmpty()) {
                    binding.lastNameEt.error = "Last Name is required!"
                }
                if (email.isEmpty()) {
                    binding.emailEt.error = "Email is required!"
                }
                return@setOnClickListener
            }

            if (selectedUser != null) {
                //Update existing user
                val updatedUser = selectedUser!!.copy(
                    firstName = firstName,
                    lastName = lastName,
                    email = email
                )
                userListViewModel.update(updatedUser)
            } else {
                // Insert new user
                val user = User(firstName = firstName, lastName = lastName, email = email)
                userListViewModel.insert(user)
            }

            finish()
//            clearEditText()
        }

    }

    fun clearEditText() {
        binding.firstNameEt.text?.clear()
        binding.lastNameEt.text?.clear()
        binding.emailEt.text?.clear()
        setResult(RESULT_OK)
        finish()
    }
}