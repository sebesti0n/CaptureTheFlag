package com.example.capturetheflag.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capturetheflag.apiServices.RetrofitInstances
import com.example.capturetheflag.models.ResponseEventModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterHuntViewModel : ViewModel() {
    private var eventResposeLiveData= MutableLiveData<ResponseEventModel>()
    fun get(): LiveData<ResponseEventModel> {
        return eventResposeLiveData
    }

    fun getRegisteredEvents(uid:Int) {
            RetrofitInstances.service.getRegisteredEventbyId(uid)
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