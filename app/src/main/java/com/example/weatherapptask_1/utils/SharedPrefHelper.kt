package com.example.weatherapptask_1.utils

import android.content.Context

object SharedPrefHelper {
    fun setLoggedIn(context: Context, isLoggedIn: Boolean) {
        val prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(Constants.IS_LOGGED_IN, isLoggedIn).apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        val prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(Constants.IS_LOGGED_IN, false)
    }

    fun logout(context: Context) {
        setLoggedIn(context, false)
    }
}
