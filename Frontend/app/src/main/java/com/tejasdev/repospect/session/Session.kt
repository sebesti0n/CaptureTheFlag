package com.tejasdev.repospect.session

import android.content.Context
import android.content.SharedPreferences

class Session(
    context: Context
){

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)


    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun createSession(
        name: String,
        mobile: String,
        college: String,
        id:Int,
        email: String,
        token: String,
        enrollmentID:String,
        image: String = ""
    ){
        editor.putString(USER_NAME, name)
        editor.putString(MOBILE, mobile)
        editor.putString(COLLEGE, college)
        editor.putInt(ID, id)
        editor.putString(EMAIL, email)
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(TOKEN, token)
        editor.putString(ENROLLMENT_ID,enrollmentID.uppercase())
        editor.putString(IMAGE, image)
        editor.apply()
    }

    fun getUID() : Int = sharedPreferences.getInt(ID, -1)

    fun getEnrollmentID() : String = sharedPreferences.getString(ENROLLMENT_ID, "111111")!!
    fun getEmail(): String = sharedPreferences.getString(EMAIL, "")!!

    fun getCollege(): String = sharedPreferences.getString(COLLEGE, "")!!
    fun getUserName(): String = sharedPreferences.getString(USER_NAME, "")!!

    fun isLogin(): Boolean = sharedPreferences.getBoolean(IS_LOGIN, false)

    fun getMobile(): String = sharedPreferences.getString(MOBILE, "")!!

    fun logOut(){
        editor.clear()
        editor.apply()
    }
    fun getUserToken(): String? = sharedPreferences.getString(TOKEN, "")

    companion object{
        private const val SP_NAME = "ctfSharedPreference"
        private const val EMAIL = "email"
        private const val IS_LOGIN = "first_time"
        private const val TOKEN = "token"
        private const val ID = "id"
        private const val ENROLLMENT_ID = "enrollmentID"
        private const val USER_NAME = "username"
        private const val MOBILE = "mobile"
        private const val COLLEGE = "college"
        private const val IMAGE = "image"

        @Volatile
        private var instance: Session? = null

        fun getInstance(context: Context): Session{
            return instance?: synchronized(this){
                instance?: Session(context).also { instance = it }
            }
        }
    }

}