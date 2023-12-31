package com.example.capturetheflag.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capturetheflag.apiServices.RetrofitInstances
import com.example.capturetheflag.models.ResponseEventModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragmentViewModel : ViewModel() {
    private var eventResponseLiveData= MutableLiveData<ResponseEventModel>()
    private var liveEventResponseLiveData= MutableLiveData<ResponseEventModel>()
    fun get(): LiveData<ResponseEventModel> {
        return eventResponseLiveData
    }
    fun getLive(): LiveData<ResponseEventModel> {
        return liveEventResponseLiveData
    }

    fun getUpcomingEvents(){
        RetrofitInstances.service.getupcomingEvent().enqueue(object: Callback<ResponseEventModel> {
            override fun onResponse(
                call: Call<ResponseEventModel>,
                response: Response<ResponseEventModel>
            ){
                eventResponseLiveData.value = response.body()
                Log.w("Sebastian",eventResponseLiveData.value.toString())

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