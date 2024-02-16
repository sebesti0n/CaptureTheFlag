package com.example.capturetheflag.session

import android.content.Context

class CtfSession(context: Context) {

    private val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor = sharedPref.edit()

    fun createSession(
        teamId: Int,
        questionNumber: Int
    ){
        editor.putInt(TEAM_ID, teamId)
        editor.putBoolean(isSessionActive, true)
        editor.putInt(CURRENT_QUESTION, questionNumber)
        editor.apply()
    }

    fun closeSession(){
        editor.clear()
        editor.apply()
    }


    fun setLevel(level: Int){
        editor.putInt(CURRENT_QUESTION, level)
        editor.apply()
    }

    fun getLevel(): Int = sharedPref.getInt(CURRENT_QUESTION, 0)!!
    fun getTeamId(): Int = sharedPref.getInt(TEAM_ID, 1)

    companion object{
        private const val PREF_NAME = "ctf_shared_pref"
        private const val CURRENT_QUESTION = "current_question"
        private const val isSessionActive = "is_session_active"
        private const val TEAM_ID = "team_id"

        @Volatile
        private var INSTANCE: CtfSession? = null

        fun getCtfSession(context: Context): CtfSession {
            if(INSTANCE!=null) return INSTANCE!!
            INSTANCE = CtfSession(context)
            return INSTANCE!!
        }
    }
}