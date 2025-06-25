package com.example.weatherapptask_1.reporitory

import androidx.lifecycle.LiveData
import com.example.weatherapptask_1.dao.UserDao
import com.example.weatherapptask_1.model.User

class AppRepository(private val userDao: UserDao) {

    fun getAllUsers(): LiveData<List<User>> {
        return userDao.getAllUsers()
    }

    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    suspend fun update(user: User) {
        userDao.update(user)
    }

    suspend fun delete(user: User) {
        userDao.delete(user)
    }
}