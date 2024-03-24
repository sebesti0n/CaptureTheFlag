package com.tejasdev.repospect.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.tejasdev.repospect.apiServices.RetrofitInstances
import com.tejasdev.repospect.models.StandingModel
import com.tejasdev.repospect.session.Session
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RankingViewModel( app: Application
) : AndroidViewModel(app)  {
    private val session = Session.getInstance(app.applicationContext)
    fun getUID() = session.getUID()
    private val id = session.getUID()

    fun getleaderBoardStatus(eid:Int,callback:(StandingModel?)-> Unit){
        val call = RetrofitInstances.service
        call.getStandings(eid).enqueue(
            object:Callback<StandingModel>{
                override fun onResponse(
                    call: Call<StandingModel>,
                    response: Response<StandingModel>
                ) {
                    response.body()?.let {
                        callback(it)
                    }
                }
                override fun onFailure(call: Call<StandingModel>, t: Throwable) {
                    Log.d("Seb Standing API Error","${t.message}")
                }


            }
        )
    }
}