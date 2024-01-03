package com.example.capturetheflag.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capturetheflag.apiServices.RetrofitInstances
import com.example.capturetheflag.models.Event
import com.example.capturetheflag.models.ResponseEventModel
import com.example.capturetheflag.models.StatusModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventViewModel:ViewModel() {
    private var eventResposeLiveData= MutableLiveData<ResponseEventModel>()
    private var registrationStatusofuser = MutableLiveData<Int>()
    private var onOpeningRegistrationStatusofuser = MutableLiveData<Int>()

    fun get(): LiveData<ResponseEventModel>?{
        return eventResposeLiveData!!
    }
    fun getStatus() : LiveData<Int>{
        return registrationStatusofuser
    }
    fun onOpenStatus() : LiveData<Int>{
        return onOpeningRegistrationStatusofuser
    }

    fun getAdminEventbyId(eid:Int){
        Log.w("sebastian evm",eid.toString())
            RetrofitInstances.service.getEventbyId(eid)
                .enqueue(object : Callback<ResponseEventModel> {
                    override fun onResponse(
                        call: Call<ResponseEventModel>,
                        response: Response<ResponseEventModel>
                    ) {
                        Log.w("sebastian evm", response.body().toString())

                        eventResposeLiveData.value = response.body()
                        Log.w("Sebastian", eventResposeLiveData.value.toString())

                    }

                    override fun onFailure(call: Call<ResponseEventModel>, t: Throwable) {
                        Log.d("TAG", t.message.toString())
                    }
                })

    }
    fun registerUserForEvent(uid:Int,eid:Int){
            RetrofitInstances.service.getStatusRegistration(uid,eid)
                .enqueue(object: Callback<StatusModel>{
                    override fun onResponse(
                        call: Call<StatusModel>,
                        response: Response<StatusModel>
                    ) {
                        registrationStatusofuser.value= response.body()?.is_registered
                    }

                    override fun onFailure(call: Call<StatusModel>, t: Throwable) {
                        Log.e("NetworkError",t.toString())

                    }

                })
    }
    fun getFirstStatus(uid:Int,eid:Int) {
        RetrofitInstances.service.onOpenStatusRegistration(uid,eid)
            .enqueue(object: Callback<StatusModel>{
                override fun onResponse(
                    call: Call<StatusModel>,
                    response: Response<StatusModel>
                ) {
                   onOpeningRegistrationStatusofuser.value= response.body()?.is_registered
                }

                override fun onFailure(call: Call<StatusModel>, t: Throwable) {
                    Log.e("NetworkError",t.toString())

                }

            })
    }
}