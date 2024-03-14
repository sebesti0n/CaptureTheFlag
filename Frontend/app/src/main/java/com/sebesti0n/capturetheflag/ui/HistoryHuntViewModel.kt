package com.sebesti0n.capturetheflag.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sebesti0n.capturetheflag.apiServices.RetrofitInstances
import com.sebesti0n.capturetheflag.models.ResponseEventModel
import com.sebesti0n.capturetheflag.session.Session
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryHuntViewModel(
    private val app:Application
): AndroidViewModel(app) {
    private var eventResposeLiveData= MutableLiveData<ResponseEventModel>()
    fun get(): LiveData<ResponseEventModel> {
        return eventResposeLiveData
    }
    private val session = Session.getInstance(app.applicationContext)
    private val id = session.getUID()
    fun getHistoryEvents() {
            RetrofitInstances.service.getPreviousEventbyId(id)
                .enqueue(object : Callback<ResponseEventModel> {
                    override fun onResponse(
                        call: Call<ResponseEventModel>,
                        response: Response<ResponseEventModel>
                    ) {
                        eventResposeLiveData.value = response.body()
                        Log.w("Sebastian", eventResposeLiveData.value.toString())

                    }

                    override fun onFailure(call: Call<ResponseEventModel>, t: Throwable) {
                        Log.d("TAG", t.message.toString())
                    }
                })
        }

}