package com.example.capturetheflag.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capturetheflag.apiServices.RetrofitInstances
import com.example.capturetheflag.models.Event
import com.example.capturetheflag.models.ResponseEventModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventViewModel:ViewModel() {
    private var eventResposeLiveData= MutableLiveData<ResponseEventModel>()
    fun get(): LiveData<ResponseEventModel>?{
        return eventResposeLiveData!!
    }
    fun getArrayListAdminEvent(): ArrayList<Event>? {
        return eventResposeLiveData.value?.event
    }
    fun getAdminEventbyId(eid:Int){
        Log.w("sebastian evm",eid.toString())
        RetrofitInstances.service.getEventbyId(eid).enqueue(object: Callback<ResponseEventModel> {
            override fun onResponse(
                call: Call<ResponseEventModel>,
                response: Response<ResponseEventModel>
            ){
                Log.w("sebastian evm",response.body().toString())

                eventResposeLiveData.value = response.body()
                Log.w("Sebastian",eventResposeLiveData.value.toString())

            }
            override fun onFailure(call: Call<ResponseEventModel>, t: Throwable) {
                Log.d("TAG", t.message.toString())
            }
        })
    }
}