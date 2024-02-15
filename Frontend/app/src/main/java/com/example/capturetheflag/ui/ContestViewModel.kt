package com.example.capturetheflag.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.capturetheflag.apiServices.RetrofitInstances
import com.example.capturetheflag.models.CtfState
import com.example.capturetheflag.models.NextRiddleModel
import com.example.capturetheflag.session.Session
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContestViewModel(
    app: Application
) : AndroidViewModel(app) {
    private val session = Session.getInstance(app.applicationContext)
    fun getUID() = session.getUID()
    private val id = session.getUID()
    fun onStartContest(
        eid: Int,
        tid: Int,
        startMs: Long,
        callback: (Boolean?, String?, CtfState?) -> Unit
    ) {
        RetrofitInstances.ctfServices.getCtfState(eid, tid, startMs)
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