package com.example.weatherapptask_1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapptask_1.database.UserDatabase
import com.example.weatherapptask_1.model.User
import com.example.weatherapptask_1.reporitory.AppRepository
import kotlinx.coroutines.launch

class UserListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AppRepository

    // 🟢 LiveData direct from repository
    val allNotes: LiveData<List<User>>

    init {
        val dao = UserDatabase.getDatabase(application).userDao()
        repository = AppRepository(dao)
        allNotes = repository.getAllUsers()
    }


    fun insert(user: User) = viewModelScope.launch {
        repository.insert(user)
    }

    fun update(user: User) = viewModelScope.launch {
        repository.update(user)
    }

    fun delete(user: User) = viewModelScope.launch {
        repository.delete(user)
    }
}