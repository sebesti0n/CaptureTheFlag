package com.example.capturetheflag.sharedprefrences

import android.content.Context
import android.content.SharedPreferences

class userPreferences private constructor(context: Context) {

    companion object{
        private const val SP_NAME = "ctfSharedPreference"
        private const val EMAIL = "email"
        private const val FIRST_TIME = "first_time"
        private const val TOKEN = "token"
    }
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

    fun saveUserCredentials(email: String, firstTime:Boolean, token: String) {
        val editor = sharedPreferences.edit()
        editor.putString(EMAIL, email)
        editor.putBoolean(FIRST_TIME,firstTime)
        editor.putString(TOKEN, token)
        editor.apply()
    }


    fun getUserEmail(): String? {
        return sharedPreferences.getString(EMAIL, null)
    }


    fun getUserToken(): String? {
        return sharedPreferences.getString(TOKEN, null)
    }

    fun getUserFirstTime():Boolean?{
        return sharedPreferences.getBoolean(FIRST_TIME,true);
    }

}