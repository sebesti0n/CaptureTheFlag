package com.example.capturetheflag.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capturetheflag.apiServices.RetrofitInstances
import com.example.capturetheflag.models.ResponseEventModel
import com.example.capturetheflag.session.Session
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragmentViewModel(
    private val app:Application
): AndroidViewModel(app) {

    var eventResponseLiveData= MutableLiveData<ResponseEventModel>()

    var liveEventResponseLiveData= MutableLiveData<ResponseEventModel>()

    private val session = Session.getInstance(app.applicationContext)

    fun getUpcomingEvents() {
            RetrofitInstances.service.getupcomingEvent()
                .enqueue(object : Callback<ResponseEventModel> {
                    override fun onResponse(
                        call: Call<ResponseEventModel>,
                        response: Response<ResponseEventModel>
                    ) {
                        eventResponseLiveData.value = response.body()
                        Log.w("Sebastian viewModel", eventResponseLiveData.value.toString())

                    }

                    override fun onFailure(call: Call<ResponseEventModel>, t: Throwable) {
                        Log.d("TAG", t.message.toString())
                    }
                })
    }
    fun getLiveEvents(){
            RetrofitInstances.service.getliveEvent().enqueue(object: Callback<ResponseEventModel> {
                override fun onResponse(
                    call: Call<ResponseEventModel>,
                    response: Response<ResponseEventModel>
                ){
                    liveEventResponseLiveData.value = response.body()
                    Log.w("Sebastian",eventResponseLiveData.value.toString())

                }
                override fun onFailure(call: Call<ResponseEventModel>, t: Throwable) {
                    Log.d("TAG", t.message.toString())
                }
            })
        }
}