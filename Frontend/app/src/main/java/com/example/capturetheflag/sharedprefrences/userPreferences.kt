package com.example.capturetheflag.sharedprefrences

import android.content.Context
import android.content.SharedPreferences

class userPreferences constructor(context: Context) {
    companion object{
        private const val SP_NAME = "ctfSharedPreference"
        private const val EMAIL = "email"
        private const val IS_LOGIN = "first_time"
        private const val TOKEN = "token"
        private const val ID = "id"
        private var instance: userPreferences? = null

        fun getInstance(context: Context): userPreferences{
            return instance?: synchronized(this){
                instance?: userPreferences(context).also { instance = it }
            }
        }
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun createSession(
        id:Int,
        email: String,
        isLogin:Boolean,
        token: String
    ){
        editor.putInt(ID, id)
        editor.putString(EMAIL, email)
        editor.putBoolean(IS_LOGIN,isLogin)
        editor.putString(TOKEN, token)
        editor.apply()
    }

    fun getUID() : Int = sharedPreferences.getInt(ID, -1)

    fun getUserEmail(): String = sharedPreferences.getString(EMAIL, "")!!

    fun isLogin(): Boolean = sharedPreferences.getBoolean(IS_LOGIN, false)

    fun logOut(){
        editor.clear()
        editor.apply()
    }
    fun getUserToken(): String? = sharedPreferences.getString(TOKEN, "")


}