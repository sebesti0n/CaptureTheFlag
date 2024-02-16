package com.example.capturetheflag.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.capturetheflag.apiServices.RetrofitInstances
import com.example.capturetheflag.models.CtfState
import com.example.capturetheflag.models.NextRiddleModel
import com.example.capturetheflag.models.RiddleModel
import com.example.capturetheflag.room.CtfDatabase
import com.example.capturetheflag.session.CtfSession
import com.example.capturetheflag.session.Session
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContestViewModel(
    app: Application
) : AndroidViewModel(app) {
    private val userSession = Session.getInstance(app.applicationContext)
    private val session = CtfSession.getCtfSession(app.applicationContext)
    private val db: CtfDatabase = CtfDatabase.getDatabase(app)
    private val riddleDao = db.riddleDao()
    private val ctfStateDao = db.CtfTeamStateDao()

    fun getRegId():String = userSession.getEnrollmentID()

    fun getTeamId(): Int = session.getTeamId()
    fun getLevel(): Int = session.getLevel()
    fun setLevel(level: Int) = session.setLevel(level)
    fun getRegistrationId(): String = userSession.getEnrollmentID()

    

    fun createSession(
        teamId: Int,
        questionNumber: Int
    ){
        session.createSession(
            teamId = teamId,
            questionNumber = questionNumber
        )
    }


    fun onStartContest(
        eid: Int,
        rid: String,
        startMs: Long,
        callback: (Boolean?, String?, CtfState?) -> Unit
    ) {
        RetrofitInstances.ctfServices.getCtfState(eid, rid, startMs)
            .enqueue(object : Callback<CtfState> {

                override fun onResponse(call: Call<CtfState>, response: Response<CtfState>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            callback(it.success, it.message, it)
                        }
                    } else {
                        response.body()?.let {
                            callback(it.success, it.message, it)
                        }
                    }
                }

                override fun onFailure(call: Call<CtfState>, t: Throwable) {
                    callback(false, t.message, null)
                }

            })

    }

    fun checkIfDataCached(): Boolean{
        return when(riddleDao.countRiddle()){
            0 -> false
            else -> true
        }
    }

    fun cacheData(list: List<RiddleModel>){
        riddleDao.insertRiddles(list)
    }

    fun getRiddles(): List<RiddleModel> = riddleDao.getRiddles()




    fun submitRiddleResponse(
        eid: Int,
        tid: Int,
        sumitAt: Long,
        callback: (Boolean?, String?, Int?) -> Unit
    ) {
        val call = RetrofitInstances.ctfServices
        call.submitRiddle(eid, tid, sumitAt).enqueue(object : Callback<NextRiddleModel> {
            override fun onResponse(
                call: Call<NextRiddleModel>,
                response: Response<NextRiddleModel>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback(true, "ok", it.next)
                    }

                } else {
                    callback(false, "Failed to Submit", -1)
                }
            }

            override fun onFailure(call: Call<NextRiddleModel>, t: Throwable) {
                callback(false, "Internal Server Error", -1)
            }

        })
    }


}