package com.example.capturetheflag.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capturetheflag.apiServices.RetrofitInstances
import com.example.capturetheflag.models.Event
import com.example.capturetheflag.models.EventX
import com.example.capturetheflag.models.RegisterResponse
import com.example.capturetheflag.models.ResponseEventModel
import com.example.capturetheflag.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateEventViewModel : ViewModel() {
    private var eventResposeLiveData= MutableLiveData<ResponseEventModel>()
    fun get(): LiveData<ResponseEventModel>?{
        return eventResposeLiveData!!
    }


    fun createEvent(event: EventX){
        RetrofitInstances.service.createEvent(event).enqueue(object : Callback<ResponseEventModel> {
            override fun onResponse(
                call: Call<ResponseEventModel>,
                response: Response<ResponseEventModel>
            ) {
                eventResposeLiveData.value=response.body()
            }

            override fun onFailure(call: Call<ResponseEventModel>, t: Throwable) {
                Log.d("TAG", t.message.toString())
            }

        })

    }

}